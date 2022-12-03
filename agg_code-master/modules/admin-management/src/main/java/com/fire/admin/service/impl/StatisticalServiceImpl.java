package com.fire.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.SupervisionStatistical;
import com.fire.admin.mapper.StatisticalMapper;
import com.fire.admin.dto.StatisticalDto;
import com.fire.admin.service.StatisticalService;
import com.fire.admin.util.DateUtil;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @date: 2022-02-09 16:37
 */
@Service
@Slf4j
public class StatisticalServiceImpl extends ServiceImpl<StatisticalMapper, SupervisionStatistical> implements StatisticalService {

    @Override
    public TitleDataVo titleData() {
        Set<Long> supervisionIds = getSupervisionIds();

        log.info("监管(局)所ID列表是：【{}】", JSONUtil.parseArray(supervisionIds));

        TitleDataVo titleDataVo = baseMapper.queryTitleData(supervisionIds);

        if (CollectionUtils.isEmpty(supervisionIds) || ObjectUtil.isEmpty(titleDataVo)) {
            return TitleDataVo.builder()
                    .totalRegulatoryMerchantCount(0)
                    .todayRegulatoryMerchantCount(0)
                    .totalPubulicInformationCount(0)
                    .todayPubilicInformationCount(0)
                    .yearRegulatoryRemindCount(0L)
                    .yearPolicyPreachCount(0)
                    .greenCount(0)
                    .yellowCount(0)
                    .redCount(0)
                    .build();
        }

        return titleDataVo;
    }

    @Override
    public RegulatoryMerchantDataVo regulatoryMerchantData(StatisticalDto param) {

        log.info("请求参数是：【{}】", JSONUtil.parse(param));

        Set<Long> supervisionIds = getSupervisionIds();

        log.info("监管(局)所ID列表是：【{}】", JSONUtil.parseArray(supervisionIds));

        List<RegulatoryMerchantDataVo.EachMerchant> eachMerchants;
        List<RegulatoryMerchantDataVo.EachMerchant> collect = Lists.newArrayList();

        eachMerchants = baseMapper.queryRegulatoryMerchantData(supervisionIds, param);

        if (CollectionUtils.isNotEmpty(eachMerchants)) {
            collect = eachMerchants.stream().filter(eachMerchant -> !eachMerchant.getMerchantCount().equals(0)).collect(Collectors.toList());
        }

        return RegulatoryMerchantDataVo.builder()
                .totalMerchantCount(eachMerchants.stream().mapToInt(RegulatoryMerchantDataVo.EachMerchant::getMerchantCount).sum())
                .eachMerchants(collect)
                .build();

    }

    @Override
    public List<PublicInformationDataVo> publicInformationData(StatisticalDto param) {
        Set<Long> supervisionIds = getSupervisionIds();

        log.info("监管(局)所ID列表是：【{}】", JSONUtil.parseArray(supervisionIds));

        if (CollectionUtils.isEmpty(supervisionIds)) {
            return Lists.newArrayList();
        }

        List<PublicInformationDataVo> voList = baseMapper.queryPublicInformationData(supervisionIds, param);

        return CollectionUtils.isEmpty(voList) ? Lists.newArrayList() : voList;

    }

    @Override
    public List<ActiveMerchantDataVo> activeMerchantData(StatisticalDto param) {
        log.info("请求参数是：【{}】", JSONUtil.parse(param));

        Set<Long> supervisionIds = getSupervisionIds();

        log.info("监管(局)所ID列表是：【{}】", JSONUtil.parseArray(supervisionIds));

        if (CollectionUtils.isEmpty(supervisionIds)) {
            return Lists.newArrayList();
        }

        List<ActiveMerchantDataVo> dataVos = Lists.newArrayList();

        if (ObjectUtil.isNotEmpty(param.getWeekTime())) {
            LocalDate localDate = LocalDate.now();
            String date = DateUtil.formatLocalDate(localDate);

            String selectedMonth = param.getWeekTime();

            String firstDayOfThisMonth = DateUtil.getFirstDayOfMonth(date);
            String lastDayOfThisMonth = DateUtil.getLastDayOfMonth(date);

            String firstDayOfNotThisMonth = DateUtil.getFirstDayOfMonth(selectedMonth);
            String lastDayOfNotThisMonth = DateUtil.getLastDayOfMonth(selectedMonth);

            //选择的日期是当月
            if (selectedMonth.split("-")[1].equals(date.split("-")[1])) {
                param.setStartTime(firstDayOfThisMonth);
                param.setEndTime(date);

                dataVos = baseMapper.queryActiveMerchantData(supervisionIds, param);

            } else {//选择的日期不是当月
                param.setStartTime(firstDayOfNotThisMonth);
                param.setEndTime(lastDayOfNotThisMonth);

                dataVos = baseMapper.queryActiveMerchantData(supervisionIds, param);

            }
        }

        return CollectionUtils.isEmpty(dataVos) ? Lists.newArrayList() : dataVos;
    }

    public List<PolicyPreachDataVo> policyPreachData(StatisticalDto param) {
        log.info("请求参数是：【{}】", JSONUtil.parse(param));

        Set<Long> supervisionIds = getSupervisionIds();

        log.info("监管(局)所ID列表是：【{}】", JSONUtil.parseArray(supervisionIds));

        if (CollectionUtils.isEmpty(supervisionIds)) {
            return Lists.newArrayList();
        }

        List<PolicyPreachDataVo> voList = baseMapper.queryPolicyPreachData(supervisionIds, param);

        return CollectionUtils.isEmpty(voList) ? Lists.newArrayList() : voList;

    }

    //获取当前登录用户所属的监管(局)所id
    public Set<Long> getSupervisionIds() {
        return SecurityUtil.getUser().getSupervisionIds();
    }
}
