package com.fire.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.mapper.AddressInfoMapper;
import com.fire.admin.dto.QueryAreaDto;
import com.fire.admin.service.QueryAreaService;
import com.fire.admin.vo.QueryAreaVo;
import com.fire.dto.enums.Status;
import com.fire.dto.response.BaseRestResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
@Data
public class QueryAreaServiceImpl implements QueryAreaService {

    @Resource
    private AddressInfoMapper addressInfoMapper;

    @Override
    public BaseRestResponse<List<QueryAreaVo>> queryAreaByParentCode(QueryAreaDto queryAreaDto) {

        ObjectMapper ob = new ObjectMapper();

        try {
            log.info("查询区域请求参数：{}", ob.writeValueAsString(queryAreaDto));
        } catch (JsonProcessingException ignored) {

        }

        BaseRestResponse<List<QueryAreaVo>> baseRestResponse = new BaseRestResponse<>();

        String parentCode = queryAreaDto.getParentCode();


        try {

            /**
             * 校验 parentCode 是否为空
             */
            if (StringUtils.isEmpty(parentCode)) {
                baseRestResponse.setStatus(Status.PARAM_LOSS.status());
                baseRestResponse.setMessage(Status.PARAM_LOSS.message());

                log.info("查询区域请求参数：{},返回参数：{}", ob.writeValueAsString(queryAreaDto), ob.writeValueAsString(baseRestResponse));
                return baseRestResponse;

            }


            List<QueryAreaVo> areaVoList = addressInfoMapper.queryByParentCode(parentCode);

            baseRestResponse.setStatus(Status.SUCCESS.status());
            baseRestResponse.setMessage(Status.SUCCESS.message());
            baseRestResponse.setData(areaVoList);

            log.info("查询区域请求参数：{},返回参数：{}", ob.writeValueAsString(queryAreaDto), ob.writeValueAsString(baseRestResponse));
        } catch (Exception e) {
                log.error("查询区域异常,父级行政编码: ".concat(parentCode), e);

        }

        return baseRestResponse;
    }
}
