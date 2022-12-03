package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.admin.entity.SupervisionStatistical;
import com.fire.admin.dto.StatisticalDto;
import com.fire.admin.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @date: 2022-02-09 16:38
 */
public interface StatisticalMapper extends BaseMapper<SupervisionStatistical> {

    /**
     * 顶部数据
     *
     * @return TitleDataVo
     */
    TitleDataVo queryTitleData(@Param("supervisionIds") Set<Long> supervisionIds);

    List<RegulatoryMerchantDataVo.EachMerchant> queryRegulatoryMerchantData(@Param("supervisionIds") Set<Long> supervisionIds, @Param("param") StatisticalDto param);

    List<PublicInformationDataVo> queryPublicInformationData(@Param("supervisionIds") Set<Long> supervisionIds, @Param("param") StatisticalDto param);

    List<ActiveMerchantDataVo> queryActiveMerchantData(@Param("supervisionIds") Set<Long> supervisionIds, @Param("param") StatisticalDto param);

    List<PolicyPreachDataVo> queryPolicyPreachData(@Param("supervisionIds") Set<Long> supervisionIds, @Param("param") StatisticalDto param);

}
