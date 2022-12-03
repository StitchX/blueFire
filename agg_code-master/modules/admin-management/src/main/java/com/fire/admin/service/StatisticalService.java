package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.entity.SupervisionStatistical;
import com.fire.admin.dto.StatisticalDto;
import com.fire.admin.vo.*;

import java.util.List;

/**
 * @Description:
 * @date: 2022-02-09 16:14
 */
public interface StatisticalService extends IService<SupervisionStatistical> {

    /**
     * 顶部抬头数据接口
     *
     * @return TitleDataVo
     */
    TitleDataVo titleData();

    /**
     * 纳入监管商户分析接口
     *
     * @return RegulatoryMerchantDataVo
     */
    RegulatoryMerchantDataVo regulatoryMerchantData(StatisticalDto param);

    /**
     * 信用信息公示接口
     *
     * @return List<PublicInformationDataVo>
     */
    List<PublicInformationDataVo> publicInformationData(StatisticalDto param);

    /**
     * 三码合一活跃商户接口
     *
     * @return List<ActiveMerchantDataVo>
     */
    List<ActiveMerchantDataVo> activeMerchantData(StatisticalDto param);

    /**
     * 监管通知下达次数接口
     *
     * @param param
     * @return
     */
    List<PolicyPreachDataVo> policyPreachData(StatisticalDto param);

}
