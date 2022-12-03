package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.*;
import com.fire.admin.entity.Merchant;
import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.entity.NoticeInfo;
import com.fire.admin.mapper.NoticeMapper;
import com.fire.admin.service.*;
import com.fire.admin.task.CategoryData;
import com.fire.admin.task.SendNoticeJob;
import com.fire.admin.task.SupervisionBureauData;
import com.fire.admin.util.DateUtil;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.NotiDetaVo;
import com.fire.admin.vo.NoticeVo;
import com.fire.admin.vo.OssUploading;
import com.fire.common.exception.BaseException;
import com.fire.dto.enums.NoticeReadEnum;
import com.fire.dto.enums.NoticeScopeEnum;
import com.fire.dto.enums.NoticeSendTypeEnum;
import com.fire.dto.enums.NoticeStatusEnum;
import com.fire.dto.log.LogInfo;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.fire.dto.enums.NoticeStatusEnum.SUCCESS_SEND_NOTICE;

/**
 * @author: admin
 * @Description:
 * @date: 2022-02-21 22:17
 * @Modified By:
 */
@Service
@Slf4j
@NoArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeInfo> implements NoticeService {

    private MerchantService merchantService;

    private CategoryService categoryService;

    private ScheduleService scheduleService;

    private NoticeDetailsService noticeDetailsService;

    private SupervisionBureauService supervisionBureauService;

    private LogInfoService logInfoService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public NoticeServiceImpl(MerchantService merchantService, CategoryService categoryService, ScheduleService scheduleService, NoticeDetailsService noticeDetailsService, SupervisionBureauService supervisionBureauService, LogInfoService logInfoService) {
        this.merchantService = merchantService;
        this.categoryService = categoryService;
        this.scheduleService = scheduleService;
        this.noticeDetailsService = noticeDetailsService;
        this.supervisionBureauService = supervisionBureauService;
        this.logInfoService = logInfoService;
    }

    @Override
    public int deleteNoticeById(NoticeDto noticeDto) {
        NoticeInfo noticeInfo = this.getById(noticeDto.getNoticeId());

        if (noticeInfo.getStatus().equals(SUCCESS_SEND_NOTICE.sendCode())) {
            throw new BaseException("已发送的通知不能删除!");
        }
        int count = baseMapper.deleteById(noticeDto.getNoticeId());

        StringBuilder desc = new StringBuilder();

        try {
            if (count > 0) {
                desc.append("成功");
            } else {
                desc.append("失败");
            }
        } catch (Throwable throwable) {
            desc.append("失败");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("在" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "删除标题为:" + noticeDto.getNoticeTitle() + "的通知").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("通知")
                    .params(JSONUtil.toJsonStr(noticeDto))
                    .build();

            logInfoService.saveLogInfo(logInfo);
        }

        return count;
    }

    @Override
    public IPage<NoticeVo> noticeList(NoticeDto param) {
        PreSecurityUser user = SecurityUtil.getUser();
        SecurityUtil.getUser().getSupervisionIds().add(user.getSupervisionId());
        Set<Long> supervisionIds = user.getSupervisionIds();
        param.setSupervisionIds(supervisionIds);
        Page<NoticeVo> page = new Page<>();
        if (ObjectUtil.isNotEmpty(param.getCurrent()) && ObjectUtil.isNotEmpty(param.getSize())) {
            page.setCurrent(param.getCurrent());
            page.setSize(param.getSize());
        }
        IPage<NoticeVo> noticeVoIPage = baseMapper.queryNoticeList(page, param);
        if (CollectionUtil.isNotEmpty(noticeVoIPage.getRecords())) {
            List<Long> noticeIds = noticeVoIPage.getRecords().stream().map(NoticeVo::getNoticeId).collect(Collectors.toList());
            Map<Long, BigDecimal> readPercent = getNoticeReadPercent(noticeIds);
            noticeVoIPage.getRecords().forEach(d -> {
                BigDecimal percent = readPercent.get(d.getNoticeId());
                if (ObjectUtil.isNotEmpty(percent)) {
                    d.setReadPercent(percent);
                }
            });
        }
        return noticeVoIPage;
    }

    /**
     * @param noticeIds
     * @description: 计算通知阅读率
     * @return: java.util.Map<java.lang.Long, java.math.BigDecimal>
     * @author: liuliu
     * @date: 2022-03-24 18:06
     */
    private Map<Long, BigDecimal> getNoticeReadPercent(List<Long> noticeIds) {
        Map<Long, BigDecimal> resultMap = new HashMap<>(10);
        Map<String, Integer> map = noticeDetailsService.selectNoticeDetailPercent(noticeIds);
        if (CollectionUtil.isEmpty(map)) {
            return null;
        }
        noticeIds.forEach(n -> {
            // 获取不必读的数量
            Integer readDefault = map.get(n.toString().concat("_").concat(NoticeReadEnum.NOTICE_READ_DEFAULT.code()));
            // 必读
            Integer readNum = map.get(n.toString().concat("_").concat(NoticeReadEnum.NOTICE_READ.code()));
            // 如果不必读为空或者数量为O 时
            if (ObjectUtil.isEmpty(readDefault) || readDefault == 0L) {
                resultMap.put(n, new BigDecimal(100));
            }
            // 如果必读的数量为空或者为0时
            if (ObjectUtil.isEmpty(readNum) || readNum == 0L) {
                resultMap.put(n, new BigDecimal(0));
            }
            if (ObjectUtil.isNotEmpty(readDefault) && ObjectUtil.isNotEmpty(readNum)) {
                BigDecimal read = new BigDecimal(readNum).divide(new BigDecimal(readDefault += readNum), 4, RoundingMode.DOWN).multiply(new BigDecimal(100));
                resultMap.put(n, read);
            }
        });
        return resultMap;
    }

    /**
     * @param noticeDto
     * @description: 新增通知
     * @return: void
     * @author: liuliu
     * @date: 2022-02-23 15:40
     */
    @Transactional(rollbackFor = Exception.class)
    @Override

    public boolean insertNotice(NoticeDto noticeDto) {
        NoticeInfo noticeInfo = buildNoticeInfo(noticeDto);
        // 发送定时任务，创建任务调度
        if (NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            createNoticeSchedule(noticeDto.getSendTime(), noticeInfo);
            noticeInfo.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        }
        // 新增通知
        boolean flag = this.save(noticeInfo);
        // 如果立即发送就立即查询符合条件的商户
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            noticeDetailsService.saveNoticeDetailBatch(noticeInfo.getNoticeDetailsInfos());
        }

        StringBuilder desc = new StringBuilder();
        try {
            if (flag) {
                desc.append("成功");
            } else {
                desc.append("失败");
            }
        } catch (Throwable throwable) {
            desc.append("失败");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("在" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "新增标题为:" + noticeDto.getNoticeTitle() + "的通知").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("通知")
                    .params(JSONUtil.toJsonStr(noticeDto))
                    .build();

            logInfoService.saveLogInfo(logInfo);
        }

        return flag;
    }

    /**
     * @param noticeDto
     * @description: 根据 noticeDto 构建 NoticeInfo对象
     * @return: com.fire.admin.entity.NoticeInfo
     * @author: liuliu
     * @date: 2022-03-01 11:36
     */
    private NoticeInfo buildNoticeInfo(NoticeDto noticeDto) {
        if (StrUtil.isEmpty(noticeDto.getNoticeTitle().trim())) {
            throw new BaseException("通知标题不能为空");
        }
        if (StrUtil.isEmpty(noticeDto.getContent())) {
            throw new BaseException("通知内容不能为空");
        }
        PreSecurityUser user = SecurityUtil.getUser();
        NoticeInfo build = NoticeInfo.builder()
                .noticeId(ObjectUtil.isNull(noticeDto.getNoticeId()) ? System.currentTimeMillis() + Long.parseLong(RandomUtil.randomNumbers(4)) : noticeDto.getNoticeId())
                .noticeTitle(noticeDto.getNoticeTitle())
                .readFlag(noticeDto.getReadFlag())
                .noticeType(noticeDto.getNoticeType())
                .content(noticeDto.getContent())
                .createTime(new Timestamp(System.currentTimeMillis()))
                .createAuthor(user.getUsername())
                .issuingAgency(SupervisionBureauData.getSupervisionBureauName(user.getSupervisionId()))
                .supervisionId(user.getSupervisionId())
                .build();

        build.setSupervisionName(build.getIssuingAgency());
        NoticeDto.NoticeCondition noticeCondition = createNoticeCondition(noticeDto, build);
        build.setNoticeDetailsInfos(noticeCondition.getNoticeDetailsInfos());
        // 处理条件，将条件写入数据库
        build.setNoticeCondition(ObjectUtil.isNotEmpty(noticeCondition) ? JSONUtil.parseObj(noticeCondition).toString() : null);
        // 附件处理
        build.setAdjunctFile(ObjectUtil.isNotEmpty(noticeDto.getAdjunctFile()) ? JSONUtil.parseArray(noticeDto.getAdjunctFile()).toString() : null);
        // 立即发送
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            build.setSendTime(build.getCreateTime());
            build.setStatus(NoticeStatusEnum.SUCCESS_SEND_NOTICE.sendCode());
        }
        return build;
    }

    /**
     * @param categoryIds
     * @description: 根据行业类别获取子类别
     * @return: java.util.Set<java.lang.Long>
     * @author: liuliu
     * @date: 2022-03-09 7:47
     */
    private Set<Long> getChildCategoryIds(Set<Long> categoryIds) {
        Set<Long> allCategory = new HashSet<>();
        if (CollectionUtil.isNotEmpty(categoryIds)) {
            categoryIds.forEach(categoryId -> {
                // 获取前端传递每个行业类别的子类别
                List<Long> ids = categoryService.selectCategoryIds(categoryId);
                allCategory.addAll(ids);
            });
        }
        return allCategory;
    }

    private List<NoticeDetailsInfo> nowSendNotice(Set<Long> merchantIds, Long noticeId, Timestamp sendTime) {
        if (CollectionUtil.isEmpty(merchantIds)) {
            throw new BaseException("没有符合条件的商户通知无法创建以及发送");
        }
        List<NoticeDetailsInfo> list = new ArrayList<>();
        merchantIds.forEach(id -> {
            NoticeDetailsInfo detailsInfo = NoticeDetailsInfo.builder().noticeId(noticeId)
                    .merchantId(id)
                    .supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .msgSendTime(sendTime).build();
            list.add(detailsInfo);
        });
        //  noticeDetailsService.saveNoticeDetailBatch(list);
        return list;
    }

    /**
     * @param
     * @description: 构建定时任务
     * @return: void
     * @author: liuliu
     * @date: 2022-03-04 14:02
     */
    private void createNoticeSchedule(String sendTime, NoticeInfo info) {
        if (StrUtil.isEmpty(sendTime)) {
            throw new BaseException("创建定时发送通知，发送时间不能为空");
        }
        if (DateUtil.formatLocalDateTimeStr(sendTime).isBefore(LocalDateTime.now())) {
            throw new BaseException("通知创建失败，通知发送时间必须大于当前系统时间");
        }
        // 创建定时任务
        NoticeScheduleDto noticeScheduleDto = NoticeScheduleDto
                .builder().noticeId(info.getNoticeId())
                .noticeTitle(info.getNoticeTitle().concat("_").concat(info.getNoticeId().toString()))
                .sendTime(sendTime).build();
        String jobName = scheduleService.scheduleFixTimeJob(SendNoticeJob.class, cn.hutool.core.date.DateUtil.parse(noticeScheduleDto.getSendTime(), DatePattern.NORM_DATETIME_FORMAT), noticeScheduleDto);
        log.info("任务创建成功，任务名称：{} 任务执行时间：{}", jobName, noticeScheduleDto.getSendTime());
    }

    /**
     * @param noticeDto
     * @description: 构建查询条件并根据条件查询到商户
     * @return: com.fire.admin.dto.NoticeDto.NoticeCondition
     * @author: liuliu
     * @date: 2022-03-01 19:22
     */
    private NoticeDto.NoticeCondition createNoticeCondition(NoticeDto noticeDto, NoticeInfo build) {
        Set<Integer> noticeScopeList = noticeDto.getNoticeScope();
        // 获取账户下的所有账户监管所编号
        PreSecurityUser user = SecurityUtil.getUser();
        Set<Long> supervisionIds = user.getSupervisionIds();
        log.info("当前账户获取监管所的编号为：{}", supervisionIds);
        if (CollectionUtil.isEmpty(supervisionIds)) {
            throw new BaseException("该账户没有绑定监管所或者监管局");
        }
        Set<Long> choseSupervisionIds = noticeDto.getSupervisionIds();
        Set<Long> merchantIds = new HashSet<>();

        // 根据行业类别获取子类别
        Set<Long> categoryIds = getChildCategoryIds(noticeDto.getCategoryIds());
        noticeDto.setQueryCateGoryIds(ObjectUtil.isNotEmpty(categoryIds) ? categoryIds : new HashSet<>());

        if (CollectionUtil.isNotEmpty(noticeDto.getMerchantIds())) {
            merchantIds.addAll(noticeDto.getMerchantIds());
        }
        List<NoticeDetailsInfo> noticeDetailsInfos = null;
        // 如果立即发送就立即查询符合条件的商户
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            NoticemMerchantDto queryMerchant = createQueryMerchant(noticeScopeList, supervisionIds, choseSupervisionIds, categoryIds);
            if (CollectionUtil.isNotEmpty(queryMerchant.getSupervisionIds()) || CollectionUtil.isNotEmpty(queryMerchant.getQueryCategoryIds()) || queryMerchant.getAwayFromSchool().equals(true)) {
                List<Merchant> records = merchantService.queryMerchantBySupervisionIds(queryMerchant).getRecords();
                if (CollectionUtil.isNotEmpty(records)) {
                    Set<Long> collect = records.stream().map(Merchant::getMerchantId).collect(Collectors.toSet());
                    merchantIds.addAll(collect);
                }
            }
            // 往消息详情发送数据，批量发送
            noticeDetailsInfos = nowSendNotice(merchantIds, build.getNoticeId(), build.getCreateTime());
        }

        return NoticeDto.NoticeCondition.builder()
                .supervisionIds(choseSupervisionIds)
                .categoryId(noticeDto.getCategoryIds())
                .merchantIds(merchantIds)
                .sendType(noticeDto.getSendType())
                .noticeScope(noticeDto.getNoticeScope())
                .noticeDetailsInfos(noticeDetailsInfos).build();
    }

    /**
     * @param noticeScopeList     ： 通知范围集合
     * @param supervisionIds      ： 该账户下所有的监管所
     * @param chooeSupervisionIds ： 通知选中的监管所
     * @param categoryId          ：通知选中的行业类别
     * @description: 构建商户查询参数
     * @return: com.fire.admin.dto.NoticemMerchantDto
     * @author: liuliu
     * @date: 2022-03-09 10:36
     */
    private NoticemMerchantDto createQueryMerchant(Set<Integer> noticeScopeList, Set<Long> supervisionIds, Set<Long> chooeSupervisionIds, Set<Long> categoryId) {

        NoticemMerchantDto merchantDto = NoticemMerchantDto.builder()
                .awayFromSchool(noticeScopeList.contains(NoticeScopeEnum.SCHOOL_AWAY_FIFTY_MERCHANT.scope()) ? true : false)
                .queryCategoryIds(noticeScopeList.contains(NoticeScopeEnum.AREA_CATEGORY_MERCHANT.scope()) ? getChildCategoryIds(categoryId) : new HashSet<>())
                .pageDTO(PageDTO.builder().current(-1L).size(-1L).build()).build();
        // 根据 根据行业跟监管所选中商户的监管所编号
        if (noticeScopeList.contains(NoticeScopeEnum.AREA_CATEGORY_MERCHANT.scope())) {
            merchantDto.setSupervisionIds(CollectionUtil.isNotEmpty(chooeSupervisionIds) ? chooeSupervisionIds : new HashSet<>());
        }
        // 所有商户
        if (noticeScopeList.contains(NoticeScopeEnum.ALL_MERCHANT.scope())) {
            merchantDto.setSupervisionIds(CollectionUtil.isNotEmpty(supervisionIds) ? supervisionIds : new HashSet<>());
        }

        if (noticeScopeList.contains(NoticeScopeEnum.ALL_MERCHANT.scope()) && noticeScopeList.contains(NoticeScopeEnum.AREA_CATEGORY_MERCHANT.scope())) {
            supervisionIds.addAll(chooeSupervisionIds);
            merchantDto.setSupervisionIds(supervisionIds);
        }

        if (noticeScopeList.contains(NoticeScopeEnum.SCHOOL_AWAY_FIFTY_MERCHANT.scope())) {
            merchantDto.setSupervisionIds(supervisionIds);
        }

        return merchantDto;
    }

    /**
     * @param noticeInfo
     * @description: 根据条件获取商户
     * @return: com.fire.admin.dto.NoticeScheduleExecuteDto
     * @author: liuliu
     * @date: 2022-03-09 12:30
     */
    private NoticeScheduleExecuteDto executeQueryMerchant(NoticeInfo noticeInfo) {
        NoticeDto.NoticeCondition noticeCondition = formatNoticeCondition(noticeInfo.getNoticeCondition());
        Set<Integer> noticeScopeList = noticeCondition.getNoticeScope();
        // 获取该监管局或者监管所子监管局或者监管所
        Set<Long> supervisionIds = supervisionBureauService.querySupervisionBureau(noticeInfo.getSupervisionId());
        // 添加通知选中的监管所
        Set<Long> chooeSupervisionIds = noticeCondition.getSupervisionIds();

        Set<Long> childCategoryIds = CollectionUtil.isNotEmpty(noticeCondition.getCategoryId()) ? getChildCategoryIds(noticeCondition.getCategoryId()) : new HashSet<Long>();
        // 构建商户查询参数
        NoticemMerchantDto merchantDto = createQueryMerchant(noticeScopeList, supervisionIds, chooeSupervisionIds, childCategoryIds);

        Set<Long> sendMerchantIds = noticeCondition.getMerchantIds();
        log.info("查询商户的参数为：{}", JSONUtil.parseObj(merchantDto));
        if (CollectionUtil.isNotEmpty(merchantDto.getSupervisionIds()) || CollectionUtil.isNotEmpty(merchantDto.getQueryCategoryIds()) || merchantDto.getAwayFromSchool().equals(true)) {
            List<Merchant> records = merchantService.queryMerchantBySupervisionIds(merchantDto).getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                sendMerchantIds.addAll(records.stream().map(Merchant::getMerchantId).collect(Collectors.toSet()));
                if (CollectionUtil.isNotEmpty(noticeCondition.getMerchantIds())) {
                    sendMerchantIds.addAll(noticeCondition.getMerchantIds());
                }
            }
        }
        return NoticeScheduleExecuteDto.builder().merchantIds(sendMerchantIds).supervisionId(noticeInfo.getSupervisionId()).build();
    }

    @Override
    public NoticeScheduleExecuteDto querySendNoticeById(Long noticeId) {
        NoticeInfo noticeInfo = baseMapper.sendNoticeById(noticeId);
        if (ObjectUtil.isNotEmpty(noticeInfo)) {
            return executeQueryMerchant(noticeInfo);
        }
        return null;
    }


    /**
     * @param noticeDto
     * @description: 根据通知id获取通知详情
     * @return: com.fire.admin.vo.NotiDetaVo
     * @author: liuliu
     * @date: 2022-03-01 16:03
     */
    @Override
    public NotiDetaVo queryNoticeDetailsById(NoticeDto noticeDto) {
        NoticeInfo noticeInfo = baseMapper.queryDetailsById(noticeDto.getNoticeId());
        NotiDetaVo notiDetaVo = NotiDetaVo.builder().noticeId(noticeInfo.getNoticeId())
                .noticeTitle(noticeInfo.getNoticeTitle())
                .readFlag(noticeInfo.getReadFlag())
                .readPercent(noticeInfo.getReadPercent())
                .noticeType(noticeInfo.getNoticeType())
                .issuingAgency(noticeInfo.getIssuingAgency())
                .status(noticeInfo.getStatus())
                .content(noticeInfo.getContent())
                .supervisionId(noticeInfo.getSupervisionId())
                .sendTime(noticeInfo.getSendTime()).build();

        // 设置条件
        NoticeDto.NoticeCondition noticeCondition = StrUtil.isNotEmpty(noticeInfo.getNoticeCondition()) ? formatNoticeCondition(noticeInfo.getNoticeCondition()) : null;
        if (ObjectUtil.isNotEmpty(noticeCondition)) {
            notiDetaVo.setNoticeScope(noticeCondition.getNoticeScope());
            notiDetaVo.setSendType(noticeCondition.getSendType());
            Set<Long> supervisionIds = SecurityUtil.getUser().getSupervisionIds();
            if (CollectionUtil.isNotEmpty(supervisionIds)) {
                Set<Long> chooseSupervisionIds = noticeCondition.getSupervisionIds();
                List<NotiDetaVo.NoticeSupervisionBureau> list = Lists.newArrayList();
                supervisionIds.forEach(id -> {
                    NotiDetaVo.NoticeSupervisionBureau noticeSupervisionBureau = NotiDetaVo.NoticeSupervisionBureau.builder().supervisionId(id)
                            .supervisionName(SupervisionBureauData.getSupervisionBureauName(id))
                            // 如果存在表示已经选中
                            .flag(CollectionUtil.isNotEmpty(chooseSupervisionIds) ? chooseSupervisionIds.contains(id) : false).build();
                    list.add(noticeSupervisionBureau);
                });
                notiDetaVo.setNoticeChooseSupervisionBureau(list);
            }
            // 这里要返回行业类别名称，通过行业编号获取
            if (CollectionUtil.isNotEmpty(noticeCondition.getCategoryId())) {
                List<NotiDetaVo.CategoryNotice> categoryNotices = Lists.newArrayList();
                noticeCondition.getCategoryId().forEach(id -> {
                    NotiDetaVo.CategoryNotice notice = NotiDetaVo.CategoryNotice.builder().categoryId(id).categoryName(CategoryData.getCategory(id)).build();
                    categoryNotices.add(notice);
                });
                notiDetaVo.setCategorys(categoryNotices);
            }
        }
        // 附件转换
        notiDetaVo.setAdjunctFile(StrUtil.isNotEmpty(noticeInfo.getAdjunctFile()) ? JSONUtil.parseArray(noticeInfo.getAdjunctFile()).toList(OssUploading.class) : null);
        return notiDetaVo;
    }

    private NoticeDto.NoticeCondition formatNoticeCondition(String noticeCondition) {
        return JSONUtil.toBean(noticeCondition, NoticeDto.NoticeCondition.class);
    }

    /**
     * @param noticeDto
     * @description:
     * @return: boolean
     * @author: liuliu
     * @date: 2022-03-01 11:32
     */
    @Override
    public boolean updateNotice(NoticeDto noticeDto) {
        if (ObjectUtil.isEmpty(noticeDto.getNoticeId())) {
            throw new BaseException("请选择需要修改的通知");
        }

        if (this.getById(noticeDto.getNoticeId()).getStatus().equals(SUCCESS_SEND_NOTICE.sendCode())) {
            throw new BaseException("已发送的通知不能编辑!");
        }
        // 设置发送时间，并且做如果发送时间离当前系统时间只有10分钟就不能修改
        if (NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            if (StrUtil.isBlank(noticeDto.getSendTime())) {
                throw new BaseException("定时任务必须填写发送时间");
            }
            Timestamp timestamp = Timestamp.valueOf(noticeDto.getSendTime());
            long time = timestamp.getTime();
            long timeMillis = System.currentTimeMillis();
            long sendDate = time - timeMillis;
            log.info("当前系统时间：{} ，发送时间为：{} 离发送时间的秒数为：{}", timeMillis, time, sendDate);
            if (sendDate < 600000) {
                throw new BaseException("通知离发送时间小于10分钟，无法修改通知");
            }
        }

        NoticeInfo info = buildNoticeInfo(noticeDto);
        info.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        info.setNoticeId(noticeDto.getNoticeId());
        info.setCreateTime(null);
        info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        info.setUpdateAuthor(SecurityUtil.getUser().getUsername());

        boolean flag = this.updateById(info);
        // 如果修改成功，就重新创建任务
        if (flag && NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            createNoticeSchedule(noticeDto.getSendTime(), info);
            info.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        }

        StringBuilder desc = new StringBuilder();

        try {
            if (flag) {
                desc.append("成功");
            } else {
                desc.append("失败");
            }
        } catch (Throwable throwable) {
            desc.append("失败");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("在" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "修改标题为:" + noticeDto.getNoticeTitle() + "的通知").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("通知")
                    .params(JSONUtil.toJsonStr(noticeDto))
                    .build();

            logInfoService.saveLogInfo(logInfo);
        }

        return flag;

    }


}
