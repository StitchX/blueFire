package com.blue.consumer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.blue.consumer.config.ConsumerConfig;
import com.blue.consumer.dto.*;
import com.blue.consumer.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.cacheDto.RedisMerchantInfo;
import com.fire.dto.mqDto.ScanCode;
import com.fire.dto.response.BaseRestResponse;
import com.fire.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.fire.dto.constants.RocketTopicConstant.SCAN;
import static com.fire.dto.enums.RedisKey.MERCHANT_INFO;
import static com.fire.dto.enums.Status.*;

/**
 * @author DK 2022/2/28 15:17
 */
@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final ObjectMapper om = new ObjectMapper();

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private JedisCluster jedisCluster;

    @Resource
    private ConsumerConfig consumerConfig;

    /**
     * 获取首页信息接口
     *
     * @param homePageRequest 首页信息请求参数
     * @return 首页数据
     */
    @Override
    public BaseRestResponse<ConsumerHomePageResponse> getHomePageInfo(ConsumerHomePageRequest homePageRequest) {

        BaseRestResponse<ConsumerHomePageResponse> pageResp = new BaseRestResponse<>();

        String merchantId = homePageRequest.getMerchantId();


        //校验商户id
        if (StringUtils.isBlank(merchantId)) {
            pageResp.setStatus(MERCHANT_ID_LOSS.status());
            pageResp.setMessage(MERCHANT_ID_LOSS.message());

            return pageResp;
        }


        //扫码存储
        try {
            ScanCode scanCode = ScanCode.builder()
                    .createTime(DateUtils.strformatDatetime(LocalDateTime.now()))
                    .merchantId(Long.parseLong(merchantId))
                    .openId(homePageRequest.getOpenId())
                    .terminal(Integer.valueOf(homePageRequest.getTerminalType()))
                    .build();

            String scanStr = om.writeValueAsString(scanCode);

            rocketMQTemplate.syncSend(SCAN, MessageBuilder.withPayload(scanStr).build());

            log.info("ConsumerService getHomePageInfo scan code send message to rocketMQ message [{}] ", scanStr);
        } catch (Exception e) {
            log.error("扫码订单存储发送mq异常：", e);
        }


        //从redis获取商户信息
        String merchantString = getRedisMerchant(merchantId);


        //redis获取商户信息为空
        if (StringUtils.isEmpty(merchantString)) {
            pageResp.setStatus(MERCHANT_NOT_EXISTS.status());
            pageResp.setMessage(MERCHANT_NOT_EXISTS.message());
            return pageResp;
        }

        try {
            RedisMerchantInfo redisMerchant = om.readValue(merchantString, RedisMerchantInfo.class);
            //商户所属区域
            String areaId = redisMerchant.getAreaId();

            //食品安全险
            String isInsurance = redisMerchant.getIsInsurance() == null ? "" : redisMerchant.getIsInsurance().toString();

            //从配置文件中取出要显示未加入食品险的区域
            List<String> insuranceAreaList = consumerConfig.getInsuranceArea();

            // 未加入食品安全险的商户区域不在要显示食品险的区域
            if (isInsurance.equals("0") && !insuranceAreaList.contains(areaId)) {
                isInsurance = "";
            }


            //商户信息
            MerchantInfo merchantInfo = MerchantInfo.builder()
                    .isInsurance(isInsurance)
                    .merchantName(redisMerchant.getMerchantName())
                    .representName(redisMerchant.getRepresentName())
                    .riskLevel(redisMerchant.getRiskLevel())
                    .socialCreditCode(redisMerchant.getSocialCreditCode())
                    .supervisionName(redisMerchant.getSupervisionName())
                    .build();


            //健康码信息
            String healthCodeType = redisMerchant.getHealthCodeType() == null ? "" : redisMerchant.getHealthCodeType();

            //健康码地址为腾讯云线上地址，logo 图片名为 health_健康码类型 + .png(即health_1.png,health_2.png,health_3.png)
            String healthCodeUrl = consumerConfig.getHealthUrl().concat("health_").concat(healthCodeType).concat(".png");

            //从配置文件中取出不显示健康码区域
            List<String> healthAreaList = consumerConfig.getHealthCodeArea();
            //该商户区域在配置的不显示健康码区域中
            if (healthAreaList.contains(areaId)) {
                healthCodeType = "0";
                healthCodeUrl = "";
            }


            HealthInfo healthInfo = HealthInfo.builder()
                    .healthCodeType(healthCodeType)
                    .healthCodeUrl(healthCodeUrl)
                    .build();

            //银行信息
            //银行logo地址为腾讯云线上地址，logo 图片名为 bank + 银行类型 +  .png(如bank_1.png,bank_2.png)
            String bankUrl = "";
            if (!ObjectUtil.isEmpty(redisMerchant.getBankId())) {
                bankUrl = consumerConfig.getBankUrl().concat("bank_").concat(redisMerchant.getBankId().toString()).concat(".png");
            }


            BankInfo bankInfo = BankInfo.builder()
                    .bankLogoUrl(bankUrl)
                    .bankName(redisMerchant.getBankName())
                    .build();

            ConsumerHomePageResponse homePageResponse = ConsumerHomePageResponse.builder()
                    .merchantInfo(merchantInfo)
                    .healthInfo(healthInfo)
                    .bankInfo(bankInfo)
                    .shufflingUrls(redisMerchant.getShufflingUrls())
                    .build();

            pageResp.setData(homePageResponse);
            pageResp.setMessage(SUCCESS.status());
            pageResp.setMessage(SUCCESS.message());

        } catch (JsonProcessingException e) {
            log.error("获取商户信息json转换异常：", e);

            pageResp.setStatus(MERCHANT_NOT_EXISTS.status());
            pageResp.setMessage(MERCHANT_NOT_EXISTS.message());
            return pageResp;
        }


        return pageResp;
    }

    /**
     * 获取 经营信息数据
     * @param commonRequest 请求参数
     * @return   经营信息查询数据
     */
    @Override
    public BaseRestResponse<BusinessInfoResponse> getBusinessInfo(ConsumerCommonRequest commonRequest) {

        BaseRestResponse<BusinessInfoResponse> baseResp = new BaseRestResponse<>();
        String merchantId = commonRequest.getMerchantId();

        //校验商户id
        if (StringUtils.isBlank(merchantId)) {
            baseResp.setStatus(MERCHANT_ID_LOSS.status());
            baseResp.setMessage(MERCHANT_ID_LOSS.message());

            return baseResp;
        }

        String merchantString = getRedisMerchant(merchantId);

        //redis获取商户信息为空
        if (StringUtils.isEmpty(merchantString)) {
            baseResp.setStatus(MERCHANT_NOT_EXISTS.status());
            baseResp.setMessage(MERCHANT_NOT_EXISTS.message());
            return baseResp;
        }

        try {

            RedisMerchantInfo redisMerchant = om.readValue(merchantString, RedisMerchantInfo.class);
            String phoneNo = redisMerchant.getLinkedPhone();
            if (phoneNo.length() == 11) {  //手机号码不为空，对中间4位进行脱敏处理
                String repStr = phoneNo.substring(3, 7);
                phoneNo = phoneNo.replace(repStr, "****");
            }

            BusinessInfoResponse businessInfo = BusinessInfoResponse.builder()
                    .businessUrl(redisMerchant.getBusinessUrl())
                    .businessAddress(redisMerchant.getBusinessAddress())
                    .industryClassification(redisMerchant.getIndustryClassification())
                    .linkedPhone(phoneNo)
                    .merchantName(redisMerchant.getMerchantName())
                    .socialCreditCode(redisMerchant.getSocialCreditCode())
                    .supervisionName(redisMerchant.getSupervisionName())
                    .representName(redisMerchant.getRepresentName())
                    .build();

            baseResp.setData(businessInfo);

            baseResp.setStatus(SUCCESS.status());
            baseResp.setMessage(SUCCESS.message());

        } catch (JsonProcessingException e) {
            log.error("获取商户经营信息json转换异常：", e);
            baseResp.setStatus(FAILURE.status());
            baseResp.setMessage(FAILURE.message());
        }

        return baseResp;
    }

    /**
     * 获取食品许可证接口
     * @param commonRequest 请求参数
     * @return 食品许可证图片url
     */
    @Override
    public BaseRestResponse<FoodLicenseInfoResponse> getFoodLicenseInfo(ConsumerCommonRequest commonRequest) {

        BaseRestResponse<FoodLicenseInfoResponse> baseResp = new BaseRestResponse<>();

        String merchantId = commonRequest.getMerchantId();
        //校验商户id
        if (StringUtils.isBlank(merchantId)) {
            baseResp.setStatus(MERCHANT_ID_LOSS.status());
            baseResp.setMessage(MERCHANT_ID_LOSS.message());

            return baseResp;
        }

        String merchantString = getRedisMerchant(merchantId);

        //redis获取商户信息为空
        if (StringUtils.isEmpty(merchantString)) {
            baseResp.setStatus(MERCHANT_NOT_EXISTS.status());
            baseResp.setMessage(MERCHANT_NOT_EXISTS.message());
            return baseResp;
        }


        try {
            RedisMerchantInfo redisMerchant = om.readValue(merchantString, RedisMerchantInfo.class);

            FoodLicenseInfoResponse foodLicense = FoodLicenseInfoResponse.builder()
                    .foodLicenseUrl(redisMerchant.getFoodLicenseUrl())
                    .build();

            baseResp.setData(foodLicense);

            baseResp.setStatus(SUCCESS.status());
            baseResp.setMessage(SUCCESS.message());
        } catch (JsonProcessingException e) {
            log.error("获取商户经营信息json转换异常：", e);
            baseResp.setStatus(FAILURE.status());
            baseResp.setMessage(FAILURE.message());
        }

        return baseResp;
    }


    /**
     * 从redis 获取商户信息
     * @param merchantId 商户id
     * @return 商户信息数据
     */
    private String getRedisMerchant(String merchantId) {
        //从redis获取商户信息
        String redisIndex = MERCHANT_INFO.key() + Long.parseLong(merchantId) / 10000;
        String merchantStr = jedisCluster.hget(redisIndex, merchantId);
        log.info("ConsumerServiceImpl getRedisMerchant merchantId is : {},redisIndex is : {}, redis merchantInfo is : {}", merchantId, redisIndex, merchantStr);
        return merchantStr;
    }


    //@PostConstruct
//    private void makeRedisMerchant(){
//        String merchantId = "1501769486351486977";
//        String redisMerchant = getRedisMerchant(merchantId);
//
//        try {
//            RedisMerchantInfo merchantInfo =  om.readValue(redisMerchant,RedisMerchantInfo.class);
//            merchantInfo.setHealthCodeType("0");
//            merchantInfo.setFoodLicenseUrl(merchantInfo.getBusinessUrl());
//            List<String> list = new ArrayList<>(1){{
//                add("https://image.supervision.bluefire.top/consumer/shuffling/common.png");
//            }};
//            merchantInfo.setShufflingUrls(list);
//            merchantInfo.setSupervisionId(1111111111L);
//            merchantInfo.setSupervisionName("达州市达川区监管所");
//            merchantInfo.setMerchantName("测试商户");
//            merchantInfo.setAreaId("51");
//            merchantInfo.setIsInsurance(0);
//            merchantInfo.setBankId(5);
//            merchantInfo.setHealthCodeType("1");
//
//            String redisIndex = MERCHANT_INFO.key() + Long.parseLong(merchantId) / 10000;
//            jedisCluster.hset(redisIndex, merchantId,om.writeValueAsString(merchantInfo));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }


}
