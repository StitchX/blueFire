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
            throw new BaseException("??????????????????????????????!");
        }
        int count = baseMapper.deleteById(noticeDto.getNoticeId());

        StringBuilder desc = new StringBuilder();

        try {
            if (count > 0) {
                desc.append("??????");
            } else {
                desc.append("??????");
            }
        } catch (Throwable throwable) {
            desc.append("??????");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("???" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "???????????????:" + noticeDto.getNoticeTitle() + "?????????").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("??????")
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
     * @description: ?????????????????????
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
            // ????????????????????????
            Integer readDefault = map.get(n.toString().concat("_").concat(NoticeReadEnum.NOTICE_READ_DEFAULT.code()));
            // ??????
            Integer readNum = map.get(n.toString().concat("_").concat(NoticeReadEnum.NOTICE_READ.code()));
            // ????????????????????????????????????O ???
            if (ObjectUtil.isEmpty(readDefault) || readDefault == 0L) {
                resultMap.put(n, new BigDecimal(100));
            }
            // ????????????????????????????????????0???
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
     * @description: ????????????
     * @return: void
     * @author: liuliu
     * @date: 2022-02-23 15:40
     */
    @Transactional(rollbackFor = Exception.class)
    @Override

    public boolean insertNotice(NoticeDto noticeDto) {
        NoticeInfo noticeInfo = buildNoticeInfo(noticeDto);
        // ???????????????????????????????????????
        if (NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            createNoticeSchedule(noticeDto.getSendTime(), noticeInfo);
            noticeInfo.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        }
        // ????????????
        boolean flag = this.save(noticeInfo);
        // ??????????????????????????????????????????????????????
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            noticeDetailsService.saveNoticeDetailBatch(noticeInfo.getNoticeDetailsInfos());
        }

        StringBuilder desc = new StringBuilder();
        try {
            if (flag) {
                desc.append("??????");
            } else {
                desc.append("??????");
            }
        } catch (Throwable throwable) {
            desc.append("??????");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("???" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "???????????????:" + noticeDto.getNoticeTitle() + "?????????").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("??????")
                    .params(JSONUtil.toJsonStr(noticeDto))
                    .build();

            logInfoService.saveLogInfo(logInfo);
        }

        return flag;
    }

    /**
     * @param noticeDto
     * @description: ?????? noticeDto ?????? NoticeInfo??????
     * @return: com.fire.admin.entity.NoticeInfo
     * @author: liuliu
     * @date: 2022-03-01 11:36
     */
    private NoticeInfo buildNoticeInfo(NoticeDto noticeDto) {
        if (StrUtil.isEmpty(noticeDto.getNoticeTitle().trim())) {
            throw new BaseException("????????????????????????");
        }
        if (StrUtil.isEmpty(noticeDto.getContent())) {
            throw new BaseException("????????????????????????");
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
        // ???????????????????????????????????????
        build.setNoticeCondition(ObjectUtil.isNotEmpty(noticeCondition) ? JSONUtil.parseObj(noticeCondition).toString() : null);
        // ????????????
        build.setAdjunctFile(ObjectUtil.isNotEmpty(noticeDto.getAdjunctFile()) ? JSONUtil.parseArray(noticeDto.getAdjunctFile()).toString() : null);
        // ????????????
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            build.setSendTime(build.getCreateTime());
            build.setStatus(NoticeStatusEnum.SUCCESS_SEND_NOTICE.sendCode());
        }
        return build;
    }

    /**
     * @param categoryIds
     * @description: ?????????????????????????????????
     * @return: java.util.Set<java.lang.Long>
     * @author: liuliu
     * @date: 2022-03-09 7:47
     */
    private Set<Long> getChildCategoryIds(Set<Long> categoryIds) {
        Set<Long> allCategory = new HashSet<>();
        if (CollectionUtil.isNotEmpty(categoryIds)) {
            categoryIds.forEach(categoryId -> {
                // ????????????????????????????????????????????????
                List<Long> ids = categoryService.selectCategoryIds(categoryId);
                allCategory.addAll(ids);
            });
        }
        return allCategory;
    }

    private List<NoticeDetailsInfo> nowSendNotice(Set<Long> merchantIds, Long noticeId, Timestamp sendTime) {
        if (CollectionUtil.isEmpty(merchantIds)) {
            throw new BaseException("?????????????????????????????????????????????????????????");
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
     * @description: ??????????????????
     * @return: void
     * @author: liuliu
     * @date: 2022-03-04 14:02
     */
    private void createNoticeSchedule(String sendTime, NoticeInfo info) {
        if (StrUtil.isEmpty(sendTime)) {
            throw new BaseException("???????????????????????????????????????????????????");
        }
        if (DateUtil.formatLocalDateTimeStr(sendTime).isBefore(LocalDateTime.now())) {
            throw new BaseException("?????????????????????????????????????????????????????????????????????");
        }
        // ??????????????????
        NoticeScheduleDto noticeScheduleDto = NoticeScheduleDto
                .builder().noticeId(info.getNoticeId())
                .noticeTitle(info.getNoticeTitle().concat("_").concat(info.getNoticeId().toString()))
                .sendTime(sendTime).build();
        String jobName = scheduleService.scheduleFixTimeJob(SendNoticeJob.class, cn.hutool.core.date.DateUtil.parse(noticeScheduleDto.getSendTime(), DatePattern.NORM_DATETIME_FORMAT), noticeScheduleDto);
        log.info("????????????????????????????????????{} ?????????????????????{}", jobName, noticeScheduleDto.getSendTime());
    }

    /**
     * @param noticeDto
     * @description: ????????????????????????????????????????????????
     * @return: com.fire.admin.dto.NoticeDto.NoticeCondition
     * @author: liuliu
     * @date: 2022-03-01 19:22
     */
    private NoticeDto.NoticeCondition createNoticeCondition(NoticeDto noticeDto, NoticeInfo build) {
        Set<Integer> noticeScopeList = noticeDto.getNoticeScope();
        // ?????????????????????????????????????????????
        PreSecurityUser user = SecurityUtil.getUser();
        Set<Long> supervisionIds = user.getSupervisionIds();
        log.info("??????????????????????????????????????????{}", supervisionIds);
        if (CollectionUtil.isEmpty(supervisionIds)) {
            throw new BaseException("?????????????????????????????????????????????");
        }
        Set<Long> choseSupervisionIds = noticeDto.getSupervisionIds();
        Set<Long> merchantIds = new HashSet<>();

        // ?????????????????????????????????
        Set<Long> categoryIds = getChildCategoryIds(noticeDto.getCategoryIds());
        noticeDto.setQueryCateGoryIds(ObjectUtil.isNotEmpty(categoryIds) ? categoryIds : new HashSet<>());

        if (CollectionUtil.isNotEmpty(noticeDto.getMerchantIds())) {
            merchantIds.addAll(noticeDto.getMerchantIds());
        }
        List<NoticeDetailsInfo> noticeDetailsInfos = null;
        // ??????????????????????????????????????????????????????
        if (NoticeSendTypeEnum.NOW_SEND.sendCode().equals(noticeDto.getSendType())) {
            NoticemMerchantDto queryMerchant = createQueryMerchant(noticeScopeList, supervisionIds, choseSupervisionIds, categoryIds);
            if (CollectionUtil.isNotEmpty(queryMerchant.getSupervisionIds()) || CollectionUtil.isNotEmpty(queryMerchant.getQueryCategoryIds()) || queryMerchant.getAwayFromSchool().equals(true)) {
                List<Merchant> records = merchantService.queryMerchantBySupervisionIds(queryMerchant).getRecords();
                if (CollectionUtil.isNotEmpty(records)) {
                    Set<Long> collect = records.stream().map(Merchant::getMerchantId).collect(Collectors.toSet());
                    merchantIds.addAll(collect);
                }
            }
            // ??????????????????????????????????????????
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
     * @param noticeScopeList     ??? ??????????????????
     * @param supervisionIds      ??? ??????????????????????????????
     * @param chooeSupervisionIds ??? ????????????????????????
     * @param categoryId          ??????????????????????????????
     * @description: ????????????????????????
     * @return: com.fire.admin.dto.NoticemMerchantDto
     * @author: liuliu
     * @date: 2022-03-09 10:36
     */
    private NoticemMerchantDto createQueryMerchant(Set<Integer> noticeScopeList, Set<Long> supervisionIds, Set<Long> chooeSupervisionIds, Set<Long> categoryId) {

        NoticemMerchantDto merchantDto = NoticemMerchantDto.builder()
                .awayFromSchool(noticeScopeList.contains(NoticeScopeEnum.SCHOOL_AWAY_FIFTY_MERCHANT.scope()) ? true : false)
                .queryCategoryIds(noticeScopeList.contains(NoticeScopeEnum.AREA_CATEGORY_MERCHANT.scope()) ? getChildCategoryIds(categoryId) : new HashSet<>())
                .pageDTO(PageDTO.builder().current(-1L).size(-1L).build()).build();
        // ?????? ??????????????????????????????????????????????????????
        if (noticeScopeList.contains(NoticeScopeEnum.AREA_CATEGORY_MERCHANT.scope())) {
            merchantDto.setSupervisionIds(CollectionUtil.isNotEmpty(chooeSupervisionIds) ? chooeSupervisionIds : new HashSet<>());
        }
        // ????????????
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
     * @description: ????????????????????????
     * @return: com.fire.admin.dto.NoticeScheduleExecuteDto
     * @author: liuliu
     * @date: 2022-03-09 12:30
     */
    private NoticeScheduleExecuteDto executeQueryMerchant(NoticeInfo noticeInfo) {
        NoticeDto.NoticeCondition noticeCondition = formatNoticeCondition(noticeInfo.getNoticeCondition());
        Set<Integer> noticeScopeList = noticeCondition.getNoticeScope();
        // ????????????????????????????????????????????????????????????
        Set<Long> supervisionIds = supervisionBureauService.querySupervisionBureau(noticeInfo.getSupervisionId());
        // ??????????????????????????????
        Set<Long> chooeSupervisionIds = noticeCondition.getSupervisionIds();

        Set<Long> childCategoryIds = CollectionUtil.isNotEmpty(noticeCondition.getCategoryId()) ? getChildCategoryIds(noticeCondition.getCategoryId()) : new HashSet<Long>();
        // ????????????????????????
        NoticemMerchantDto merchantDto = createQueryMerchant(noticeScopeList, supervisionIds, chooeSupervisionIds, childCategoryIds);

        Set<Long> sendMerchantIds = noticeCondition.getMerchantIds();
        log.info("???????????????????????????{}", JSONUtil.parseObj(merchantDto));
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
     * @description: ????????????id??????????????????
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

        // ????????????
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
                            // ??????????????????????????????
                            .flag(CollectionUtil.isNotEmpty(chooseSupervisionIds) ? chooseSupervisionIds.contains(id) : false).build();
                    list.add(noticeSupervisionBureau);
                });
                notiDetaVo.setNoticeChooseSupervisionBureau(list);
            }
            // ????????????????????????????????????????????????????????????
            if (CollectionUtil.isNotEmpty(noticeCondition.getCategoryId())) {
                List<NotiDetaVo.CategoryNotice> categoryNotices = Lists.newArrayList();
                noticeCondition.getCategoryId().forEach(id -> {
                    NotiDetaVo.CategoryNotice notice = NotiDetaVo.CategoryNotice.builder().categoryId(id).categoryName(CategoryData.getCategory(id)).build();
                    categoryNotices.add(notice);
                });
                notiDetaVo.setCategorys(categoryNotices);
            }
        }
        // ????????????
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
            throw new BaseException("??????????????????????????????");
        }

        if (this.getById(noticeDto.getNoticeId()).getStatus().equals(SUCCESS_SEND_NOTICE.sendCode())) {
            throw new BaseException("??????????????????????????????!");
        }
        // ???????????????????????????????????????????????????????????????????????????10?????????????????????
        if (NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            if (StrUtil.isBlank(noticeDto.getSendTime())) {
                throw new BaseException("????????????????????????????????????");
            }
            Timestamp timestamp = Timestamp.valueOf(noticeDto.getSendTime());
            long time = timestamp.getTime();
            long timeMillis = System.currentTimeMillis();
            long sendDate = time - timeMillis;
            log.info("?????????????????????{} ?????????????????????{} ??????????????????????????????{}", timeMillis, time, sendDate);
            if (sendDate < 600000) {
                throw new BaseException("???????????????????????????10???????????????????????????");
            }
        }

        NoticeInfo info = buildNoticeInfo(noticeDto);
        info.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        info.setNoticeId(noticeDto.getNoticeId());
        info.setCreateTime(null);
        info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        info.setUpdateAuthor(SecurityUtil.getUser().getUsername());

        boolean flag = this.updateById(info);
        // ??????????????????????????????????????????
        if (flag && NoticeSendTypeEnum.TIMING_SEND_NOTICE.sendCode().equals(noticeDto.getSendType())) {
            createNoticeSchedule(noticeDto.getSendTime(), info);
            info.setSendTime(Timestamp.valueOf(noticeDto.getSendTime()));
        }

        StringBuilder desc = new StringBuilder();

        try {
            if (flag) {
                desc.append("??????");
            } else {
                desc.append("??????");
            }
        } catch (Throwable throwable) {
            desc.append("??????");
        } finally {
            String username = SecurityUtil.getUser().getUsername();
            LogInfo logInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username.concat("???" + DateUtil.formatLocalDateTime(LocalDateTime.now()) + "???????????????:" + noticeDto.getNoticeTitle() + "?????????").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("??????")
                    .params(JSONUtil.toJsonStr(noticeDto))
                    .build();

            logInfoService.saveLogInfo(logInfo);
        }

        return flag;

    }


}
