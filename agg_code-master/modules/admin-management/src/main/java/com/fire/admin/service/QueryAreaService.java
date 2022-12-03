package com.fire.admin.service;

import com.fire.admin.dto.QueryAreaDto;
import com.fire.admin.vo.QueryAreaVo;
import com.fire.dto.response.BaseRestResponse;

import java.util.List;

/**
 *
 * 区域查询接口
 */
public interface QueryAreaService {

    /**
     * 根据父级id查询下一级区域
     *
     * @param queryAreaDto  查询参数父级id
     * @return 返回下一级区域
     */
    BaseRestResponse<List<QueryAreaVo>> queryAreaByParentCode(QueryAreaDto queryAreaDto);
}
