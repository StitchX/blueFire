package com.blue.consumer.rest;


import com.blue.consumer.dto.*;
import com.blue.consumer.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.response.BaseRestResponse;
import com.fire.utils.IPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.fire.dto.enums.Status.FAILURE;

@Api(tags = "消费者端界面信息获取")
@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerInformationRest {


    @Resource
    private ConsumerService consumerService;


    @ApiModelProperty(value = "获取消费者端首页信息接口")
    @PostMapping("/getHomePage")
    public BaseRestResponse<ConsumerHomePageResponse> makeHomePageInfo(@RequestBody ConsumerHomePageRequest homePageRequest, HttpServletRequest request) {

        BaseRestResponse<ConsumerHomePageResponse> homePageInfo = new BaseRestResponse<>();
        ObjectMapper om = new ObjectMapper();

        try {
            String clientIp = IPUtil.getIpAddr(request);
            log.info("ConsumerInformationRest makeHomePageInfo ip is : {} , request : [{}]", clientIp, om.writeValueAsString(homePageRequest));

            homePageInfo = consumerService.getHomePageInfo(homePageRequest);

        } catch (Exception e) {
            log.error("获取商户首页信息异常：" + homePageRequest.getMerchantId(), e);
            homePageInfo.setStatus(FAILURE.status());
            homePageInfo.setMessage(FAILURE.message());
        }

        try {
            log.info("ConsumerInformationRest makeHomePageInfo request : [{}],response : [{}]", om.writeValueAsString(homePageRequest), om.writeValueAsString(homePageInfo));
        } catch (JsonProcessingException ignored) {

        }

        return homePageInfo;
    }

    @ApiModelProperty(value = "获取消费者端经营信息接口")
    @PostMapping("/getBusinessInfo")
    public BaseRestResponse<BusinessInfoResponse> makeBusinessInfo(@RequestBody ConsumerCommonRequest commonRequest, HttpServletRequest request) {
        BaseRestResponse<BusinessInfoResponse> baseResp = new BaseRestResponse<>();
        ObjectMapper om = new ObjectMapper();
        try {
            String clientIp = IPUtil.getIpAddr(request);
            log.info("ConsumerInformationRest makeBusinessInfo ip is : {} , request : [{}]", clientIp, om.writeValueAsString(commonRequest));

            baseResp = consumerService.getBusinessInfo(commonRequest);

        } catch (Exception e) {
            log.error("获取商户经营信息异常：" + commonRequest.getMerchantId(), e);
            baseResp.setStatus(FAILURE.status());
            baseResp.setMessage(FAILURE.message());
        }

        try {
            log.info("ConsumerInformationRest makeBusinessInfo request : [{}],response : [{}]", om.writeValueAsString(commonRequest), om.writeValueAsString(baseResp));
        } catch (JsonProcessingException ignored) {

        }
        return baseResp;
    }

    @ApiModelProperty(value = "获取消费者端许可证信息接口")
    @PostMapping("/getFoodLicenseInfo")
    public BaseRestResponse<FoodLicenseInfoResponse> makeFoodLicenseInfo(@RequestBody ConsumerCommonRequest commonRequest, HttpServletRequest request) {
        BaseRestResponse<FoodLicenseInfoResponse> baseResp = new BaseRestResponse<>();
        ObjectMapper om = new ObjectMapper();
        try {
            String clientIp = IPUtil.getIpAddr(request);
            log.info("ConsumerInformationRest makeFoodLicenseInfo ip is : {} , request : [{}]", clientIp, om.writeValueAsString(commonRequest));

            baseResp = consumerService.getFoodLicenseInfo(commonRequest);

        } catch (Exception e) {
            log.error("获取商户许可证信息异常：" + commonRequest.getMerchantId(), e);
            baseResp.setStatus(FAILURE.status());
            baseResp.setMessage(FAILURE.message());
        }

        try {
            log.info("ConsumerInformationRest makeFoodLicenseInfo request : [{}],response : [{}]", om.writeValueAsString(commonRequest), om.writeValueAsString(baseResp));
        } catch (JsonProcessingException ignored) {

        }
        return baseResp;
    }



}
