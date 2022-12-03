package com.blue.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.crm.entity.AddressInfo;
import com.blue.crm.vo.QueryAreaVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地区表(AddressInfo)表数据库访问层
 *
 * @author ZJQ  2022-01-26 11:48:16
 */
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {

    /**
     * 通过父级code查询区域
     *
     * @param parentCode  父级code
     * @return  下一级行政区域
     */
    List<QueryAreaVo> queryByParentCodeAndLevel(@Param("parentCode") String parentCode, @Param("level") int level);

    /**
     * @description 通过code和level查询区域
     * @author fcq
     * @date 2022/3/1 17:48
     * @version v2.0.1.crm
     */
    List<QueryAreaVo> queryByCodeAndLevel(@Param("parentCode") String parentCode, @Param("level") int level);
}

