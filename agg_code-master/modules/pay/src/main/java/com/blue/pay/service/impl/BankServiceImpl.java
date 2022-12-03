package com.blue.pay.service.impl;


import com.blue.pay.config.NacosBanks;
import com.blue.pay.request.MakeBankParam;
import com.blue.pay.response.MakeBankResp;
import com.blue.pay.service.BankService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.cacheDto.RedisMerchantInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fire.dto.enums.RedisKey.MERCHANT_INFO;


/**
 * @author: QGC
 * @Description:
 * @date: 2022-03-09 15:10
 */
@Slf4j
@Service
public class BankServiceImpl  implements BankService {

    @Resource
    private JedisCluster jedisCluster;

    private final NacosBanks nacosBanks;

    @Resource
    private RestTemplate restTemplate;

    public BankServiceImpl(NacosBanks nacosBanks) {
        this.nacosBanks = nacosBanks;
    }

    /**
     * 银行下单
     *
     * @param param 下单参数
     */
    @Override
    public MakeBankResp makeOrder(MakeBankParam param) {
        MakeBankResp makeBankResp = new MakeBankResp();
        ObjectMapper om = new ObjectMapper();

        String businessId = param.getBusinessId();
        //校验商户id
        if (StringUtils.isBlank(businessId)) {
            makeBankResp.setRespCode("98");
            makeBankResp.setRespMsg("下单错误,商户号不能为空");
            return  makeBankResp;
        }
        //校验下单金额
        if (StringUtils.isBlank(param.getOrderAmt())){
            makeBankResp.setRespCode("97");
            makeBankResp.setRespMsg("下单错误,金额不能为空");
            return  makeBankResp;
        }
        //从redis获取商户信息
        String redisIndex = MERCHANT_INFO.key() + Long.parseLong(businessId) / 10000;
        String merchantString = jedisCluster.hget(redisIndex, businessId);

        //redis获取商户信息为空
        if (StringUtils.isEmpty(merchantString)) {
            //TODO 查询数据库

            makeBankResp.setRespCode("96");
            makeBankResp.setRespMsg("下单错误,商户号查询失败");
            return  makeBankResp;
        }

        try {
            RedisMerchantInfo redisMerchant = om.readValue(merchantString, RedisMerchantInfo.class);
            int bankId = redisMerchant.getBankId();
//            int bankId = 5;
            String bankUrl = "";
            System.out.println("bankUrl:".concat(bankUrl));
            //用ID判断并分发到其他银行模块
            List<NacosBanks.BankSet> bankList = nacosBanks.getBankList();
            for (NacosBanks.BankSet bankSet : bankList) {
                if (bankId == bankSet.getBankId()){
                    bankUrl = bankSet.getBankUrl();
                    break;
                }
            }
            //判读URL
            if (Strings.isBlank(bankUrl)){
                makeBankResp.setRespCode("95");
                makeBankResp.setRespMsg("下单错误,银行配置URL有误");
                return  makeBankResp;
            }
            //post调对应银行接口
            StringBuffer url = new StringBuffer(bankUrl)
                    .append("?orderAmt={orderAmt}")
                    .append("&mchId={mchId}")
                    .append("&mchtNo={mchtNo}")
                    .append("&openId={openId}")
                    .append("&orderType={orderType}")
                    .append("&mchName={mchName}")
                    .append("&bankCode={bankCode}")
                    .append("&bankName={bankName}")
                    .append("&secretKey={secretKey}");

            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("orderAmt",param.getOrderAmt());
            paramMap.put("mchId",param.getBusinessId());
            paramMap.put("mchtNo",redisMerchant.getBankMerchantNum()); //685165121327426
            paramMap.put("openId",param.getOpenId());
            paramMap.put("orderType",param.getOrderType());
            paramMap.put("mchName",redisMerchant.getMerchantName());
            paramMap.put("bankCode",redisMerchant.getBankId());
            paramMap.put("bankName",redisMerchant.getBankName());
            paramMap.put("secretKey",redisMerchant.getSecretKey());

            log.info("调用银行接口：".concat(bankUrl).concat("调用参数").concat(om.writeValueAsString(paramMap)));
            ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.POST, null, String.class, paramMap);
            String res = response.getBody();
            if (res != null) {
                log.info("下单响应：".concat(res));
                makeBankResp = om.readValue(res, MakeBankResp.class);
                return makeBankResp;
            }

        } catch (JsonProcessingException e) {
            log.error("获取商户信息异常：", e);
            makeBankResp.setRespCode("99");
            makeBankResp.setRespMsg("下单错误,下单异常");
            return  makeBankResp;
        }

        return makeBankResp;
    }


}