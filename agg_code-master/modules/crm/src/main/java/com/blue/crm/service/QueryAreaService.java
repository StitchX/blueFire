package com.blue.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.blue.crm.enums.AreaLevelEnum;
import com.blue.crm.mapper.AddressInfoMapper;
import com.blue.crm.request.QueryAreaParam;
import com.blue.crm.vo.QueryAreaVo;
import com.fire.dto.enums.Status;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 区域查询接口
 */
@Slf4j
@Service
public class QueryAreaService {

    @Resource
    private AddressInfoMapper addressInfoMapper;

    public BaseRestResponse<List<QueryAreaVo>> queryAreaByParentCode(QueryAreaParam queryAreaParam) {
        log.info("查询区域请求参数：{}", JSONUtil.parse(queryAreaParam));
        String parentCode = queryAreaParam.getParentCode();
        int level = queryAreaParam.getLevel();
        //校验 parentCode 是否为空
        if (StringUtils.isEmpty(parentCode)) {
            BaseRestResponse error = BaseRestResponse.error(Status.PARAM_LOSS.status(), Status.PARAM_LOSS.message());
            log.info("查询区域请求参数：{},返回参数：{}", JSONUtil.parse(queryAreaParam), JSONUtil.parse(error));
            return error;
        }
        List<QueryAreaVo> areaVoList = addressInfoMapper.queryByParentCodeAndLevel(parentCode, level);
        //查询市级返回为空时，返回省级如北京市
        if (CollectionUtil.isEmpty(areaVoList) && level == AreaLevelEnum.CITY.getLevel()) {
            areaVoList = addressInfoMapper.queryByCodeAndLevel(parentCode, AreaLevelEnum.PROVENCE.getLevel());
        }
        BaseRestResponse baseRestResponse = new BaseRestResponse(areaVoList);
        log.info("查询区域请求参数：{},返回参数：{}", JSONUtil.parse(queryAreaParam), JSONUtil.parse(baseRestResponse));
        return baseRestResponse;
    }
}
