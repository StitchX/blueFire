package com.blue.nongxin.service.impl;

import com.blue.nongxin.config.BankConfig;
import com.blue.nongxin.dto.JsonRootBean;
import com.blue.nongxin.service.BankOrderService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.constants.RocketTopicConstant;
import com.fire.dto.entity.OrderList;
import com.scrcu.opensdk.ScrcuRequest;
import com.scrcu.opensdk.ScrcuResponse;
import com.scrcu.opensdk.ScrcuSdk;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fire.dto.constants.RocketTagConstant.CALLBACK_TAG;
import static com.fire.dto.constants.RocketTagConstant.MAKE_TAG;
import static com.fire.dto.enums.RedisKey.ORDER_ID_INCR;
import static com.fire.dto.enums.RedisKey.ORDER_INFO;

/**
 * @author : [QGC]
 * @version : [v1.0]
 * @createTime : [2022/2/23 13:23]
 */
@Service
@Slf4j
public class BankOrderServiceImpl  implements BankOrderService {

    private static final BankConfig config = new BankConfig();


    @Resource
    private JedisCluster jedisCluster;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Map<String,Object> makeOrder(String mchtNo,String orderAmt, String openId, String orderType,String mchId,String mchName,String bankCode, String bankName) {
        HashMap<String, Object> resMap = new HashMap<>();
        try {
            //创建sdk对象--传入appid，appSecret,行方加密公钥，合作方解密私钥，行方验签公钥,合作方加签私钥。
            ScrcuSdk sdk = ScrcuSdk.getInstance(config.getAPPID(), config.getAPPSECRET(),config.getOPENEPUBK(),config.getAPPDPRIK(),config.getOPENVPUBK(),config.getAPPSPRIK());
            //设置自定义域名--网关为例
            sdk.setDomainName(config.getHTTP());
            //设置请求body参数--以map为例,可以使用实体类或者json格式字符串
            HashMap<String, Object> body =new HashMap<>();
            //商户请求流水号，32个字符以内、可能包含字母、数字；需保证在商户端不重复
            Long orderId = jedisCluster.incr(ORDER_ID_INCR.key());
            body.put("reqSsn", orderId);
            //请求时间：格式"yyyyMMddHHmmss"
            SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss" );
            Date d= new Date();
            String timeStr = sdf.format(d);
            body.put("reqTime", timeStr);
            //商户号，验收测试阶段由验收对接人员提供，验收测试阶段前按照示例上送即可，生产环境商户号需在地方联社申请获得 139765100273858
            body.put("mchtNo",mchtNo);
            //商户的终端设备编号，只支持8位固定长度字母或者数字，没有可以填8个0
            body.put("mchtTermId", "00000000");
            //交易发生地区，行政区划分代码 511381
            body.put("txnAreaInfo", "511381");
            //交易结果通知地址，支持http或者https默认端口，不支持自定义端口
            body.put("notifyUrl", config.getCALLBACK());
            //有效时间，单位为秒：超过该时间订单自动关闭
            body.put("validTime", "600");
            //订单金额，单位为元，精确到小数点后两位；取值范围0.01~100000000
            body.put("orderAmt", orderAmt);
            //订单描述
            body.put("orderDesc", "智慧放心码");
            //商品编码
            body.put("goodsNo", "10000001");
            //商品名称
            body.put("goodsName", "智慧放心码");
            //商品数量
            body.put("goodsNum", "1");
            //商品单价
            body.put("goodsPrice", orderAmt);
            ScrcuRequest<HashMap<String, Object>> request=new ScrcuRequest<>(body);
            //设置头部参数
            //请求流水号，由合作方生成，每次唯一。生成规则：yyyyMMddHHmmssSSS+appId+3位随机数
            SimpleDateFormat sdf2 =new SimpleDateFormat("yyyyMMddHHmmssSSS" );
            Date d2= new Date();
            String timeStr2 = sdf2.format(d2);
            int i=(int)(Math.random()*900)+100;
            request.setRequestParam(request, "userId", timeStr2 + config.getAPPID() + i);
            //商户的终端设备编号，只支持8位固定长度字母或者数字，没有可以填8个0
            request.setRequestParam(request, "devFpId", "00000000");

            ScrcuResponse response = sdk.call("/qrcodepay/OPC321560202000100000100", request, config.getVERSION());
            //获取response里面的数据---不同形式
            //body
            Map<String, Object> bodyData = response.getBody();
            String responseString = response.toString();
            log.info("农信银行下单返回数据：" + responseString);
            if ("0000000000".equals(bodyData.get("respCode"))){
                //发送到mq
                OrderList orderList = new OrderList();
                orderList.setOrderId(orderId);
                BigDecimal BigDecAmt = new BigDecimal(orderAmt);
                BigDecimal outBigDecAmt = BigDecAmt.multiply(new BigDecimal(100));
                String orderAmt100 = outBigDecAmt.stripTrailingZeros().toPlainString();
                orderList.setPrice(Integer.valueOf(orderAmt100));
                orderList.setBankCode(bankCode);
                orderList.setBankName(bankName);
                orderList.setMerchantId(Long.valueOf(mchId));
                orderList.setOpenId(openId);
                if (Strings.isNotBlank(orderType)) {
                   orderList.setType(Integer.valueOf(orderType));
                }
                orderList.setStatus(1);
                orderList.setCreateTime(d);
                orderList.setMerchantName(mchName);
                orderList.setBankOrderSn(String.valueOf(bodyData.get("respSsn")));
                orderList.setMakeOrderSn(orderId.toString());

                ObjectMapper om = new ObjectMapper();
                String orderListStr = om.writeValueAsString(orderList);

                //订单入redis
                long hKey = orderId / 100000;
                jedisCluster.hset(ORDER_INFO.key() + hKey, String.valueOf(orderId), orderListStr);

                //发送MQ消息
                rocketMQTemplate.syncSendOrderly(RocketTopicConstant.ORDER + ":" + MAKE_TAG, MessageBuilder.withPayload(orderListStr).build(), String.valueOf(orderId));
                log.info("农信银行下单成功发送MQ数据:".concat(orderListStr));
                //构造返回
                resMap.put("respCode","00");
                resMap.put("origReqSsn",orderId);
                resMap.put("reqTime", timeStr);
                resMap.put("respMsg",bodyData.get("respMsg"));
                resMap.put("resCentet",bodyData.get("token"));
                resMap.put("resType","1");
            } else {
                resMap.put("respCode",bodyData.get("respCode"));
                resMap.put("origReqSsn",orderId);
                resMap.put("reqTime", timeStr);
                resMap.put("respMsg",bodyData.get("respMsg"));
                resMap.put("resCentet","");
                resMap.put("resType","");
            }
        } catch (Exception e){
            log.error("农信银行下单错误",e);
            resMap.put("origReqSsn","");
            resMap.put("reqTime", "");
            resMap.put("respCode","99");
            resMap.put("respMsg","发送或接收数据出错");
            resMap.put("resCentet","");
            resMap.put("resType","");
        }
        return resMap;
    }

    @Override
    public Map<String,Object> getOrder(String mchtNo,String origReqSsn,String reqSsn,String origReqTime) {
        HashMap<String, Object> resMap = new HashMap<>();
        try {
            //创建sdk对象--传入appid，appSecret,行方加密公钥，合作方解密私钥，行方验签公钥,合作方加签私钥。
            ScrcuSdk sdk = ScrcuSdk.getInstance(config.getAPPID(), config.getAPPSECRET(), config.getOPENEPUBK(), config.getAPPDPRIK(), config.getOPENVPUBK(), config.getAPPSPRIK());
            //设置自定义域名--网关为例
            sdk.setDomainName(config.getHTTP());
            //设置请求body参数--以map为例,可以使用实体类或者json格式字符串
            HashMap<String, Object> body = new HashMap<>();
            //商户请求流水号，32个字符以内、可能包含字母、数字；需保证在商户端不重复
            body.put("origReqSsn", origReqSsn);
            body.put("reqSsn", reqSsn);
            //请求时间：格式"yyyyMMddHHmmss"
            SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss" );
            Date d= new Date();
            String timeStr = sdf.format(d);
            body.put("reqTime", timeStr);
            //商户号，验收测试阶段由验收对接人员提供，验收测试阶段前按照示例上送即可，生产环境商户号需在地方联社申请获得 139765100270698
            body.put("mchtNo", mchtNo);
            //原支付或者退款交易的请求时间，原支付或者退款交易请求报文的reqTime字段
            body.put("origReqTime", origReqTime);

            ScrcuRequest<HashMap<String, Object>> request = new ScrcuRequest<>(body);
            //设置头部参数
            //请求流水号，由合作方生成，每次唯一。生成规则：yyyyMMddHHmmssSSS+appId+3位随机数
            SimpleDateFormat sdf2 =new SimpleDateFormat("yyyyMMddHHmmssSSS" );
            Date d2= new Date();
            String timeStr2 = sdf2.format(d2);
            int i=(int)(Math.random()*900)+100;
            request.setRequestParam(request, "userId", timeStr2 + config.getAPPID() + i);
            //商户的终端设备编号，只支持8位固定长度字母或者数字，没有可以填8个0
            request.setRequestParam(request, "devFpId", "00000000");

            ScrcuResponse response = sdk.call("/qrcodepay/OPC321560202000100000300", request, config.getVERSION());
            //获取response里面的数据---不同形式
            //body
            Map<String, Object> bodyData = response.getBody();
            //String bodyDataString = response.getBodyString();
            //header
            //ResponseHeader header = response.getHeader();
            //responseString
            String responseString = response.toString();
            log.info("农信银行查询订单返回数据：" + responseString);
            resMap.put("respCode",bodyData.get("respCode"));
            resMap.put("respMsg",bodyData.get("respMsg"));
            resMap.put("txnType",bodyData.get("txnType"));
            resMap.put("txnAmt",bodyData.get("payAmt"));
            resMap.put("orderState",bodyData.get("orderState"));
            if ("0000000000".equals(bodyData.get("respCode"))){
                resMap.put("respCode","00");
                if ("00".equals(bodyData.get("orderState"))){
                    resMap.put("orderState","02");
                } else if("10".equals(bodyData.get("orderState"))){
                    resMap.put("orderState","01");
                } else if("11".equals(bodyData.get("orderState"))) {
                    resMap.put("orderState", "01");
                } else if("12".equals(bodyData.get("orderState"))) {
                    resMap.put("orderState", "01");
                }else if("03".equals(bodyData.get("orderState"))) {
                    resMap.put("orderState", "04");
                } else {
                    resMap.put("orderState", "03");
                }
            }
        } catch (Exception e) {
            log.error("农信银行查询错误",e);
            resMap.put("respCode","99");
            resMap.put("respMsg","查询订单失败错误");
            resMap.put("txnType","");
            resMap.put("txnAmt","");
            resMap.put("orderState","");
        }
        return resMap;
    }

    @Override
    public void orderCallBack(String data) {
        try {
            ScrcuSdk sdk = ScrcuSdk.getInstance(config.getAPPID(), config.getAPPSECRET(),config.getOPENEPUBK(),config.getAPPDPRIK(),config.getOPENVPUBK(),config.getAPPSPRIK());
            String callBackJson = sdk.getCallBackJson(data);
            log.info("农信银行支付回调：" + callBackJson );
            //TODO 发送到OR

            ObjectMapper om = new ObjectMapper();
            JsonRootBean jsonRootBean = om.readValue(callBackJson, JsonRootBean.class);
            String reqSsn = jsonRootBean.getOrigReqSsn();
            //暂时发到MQ
            long hKey = Long.parseLong(reqSsn) / 100000;
            String orderListStr = jedisCluster.hget(ORDER_INFO.key() + hKey, reqSsn);
            if (Strings.isBlank(orderListStr)){
                log.info("农信银行支付回调错误！查询redis失败");
                return;
            }
            OrderList orderList = om.readValue(orderListStr, OrderList.class);
            orderList.setCallbackTime(new Date());
            if ("0".equals(jsonRootBean.getOrderState())){
                orderList.setStatus(2);
                orderList.setOtherOrderId(jsonRootBean.getTpamTxnSsn());
                orderList.setBankOrderId(jsonRootBean.getPagyTxnSsn());
            } else {
                orderList.setStatus(3);
            }
            orderListStr = om.writeValueAsString(orderList);
            //订单存入redis
            jedisCluster.hset(ORDER_INFO.key() + hKey, reqSsn, orderListStr);
            //发送MQ消息
            rocketMQTemplate.syncSendOrderly(RocketTopicConstant.ORDER + ":" + CALLBACK_TAG, MessageBuilder.withPayload(orderListStr).build(), reqSsn);
            log.info("农信银行回调成功发送MQ数据:".concat(orderListStr));
        } catch (Exception e){
            log.error("农信银行支付回调错误！：",e);
        }
    }

    }
