package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.admin.entity.AddressInfo;
import com.fire.admin.vo.QueryAreaVo;

import java.util.List;

/**
 * 地区表(AddressInfo)表数据库访问层
 *
 * @author DK  2022-01-26 11:48:16
 */
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {

    /**
     * 通过父级code查询区域
     *
     * @param parentCode  父级code
     * @return  下一级行政区域
     */
    List<QueryAreaVo> queryByParentCode(String parentCode);




}

