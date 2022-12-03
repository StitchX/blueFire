package com.fire.api.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.fire.api.config.PayMessage;
import com.fire.api.request.MqttPayParam;
import com.fire.api.service.MqttMessageService;
import com.fire.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class MqttMessageServiceImpl implements MqttMessageService {

    /**
     * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     */
    @Value("${mqtt.qosLevel:2}")
    private int qosLevel;
    /**
     * groupId 在 MQ4IOT 控制台申请
     */
    @Value("${mqtt.groupID:null}")
    private String groupID;
    /**
     * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。
     * 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
     */
    @Value("${mqtt.parentTopic:null}")
    private String parentTopic;

    @Value("${mqtt.isAlibaba:false}")
    private boolean isAlibaba;

    @Value("${salt:cjggmt4cewa0b8gt1s8s74ec41l3rb0z}")
    private String salt;

//    @Data
//    @AllArgsConstructor
//    static class Device{
//        private String IMEI;
//        private String IccID;
//    }
//    private static Map<String,Device> dates = new HashMap<>();

//    static {
//        dates.put("123", new Device("861714050000847", "898604A0192170057922"));
//        payType.put("10000", "收款");
//        payType.put("10001", "微信收款");
//        payType.put("10002", "支付宝收款");
//        payType.put("10003", "银联支付收款");
//        payType.put("10004", "重庆银行收款");
//        payType.put("10005", "农业银行收款");
//        payType.put("10006", "天府银行收款");
//        payType.put("10007", "建设银行收款");
//        payType.put("10008", "放心监管码收款");
//    }

    @Resource
    private PayMessage payMessage;

    @Resource
    private MqttClient mqttClient;

    public BaseResponse payMessage(MqttPayParam payParam){
        long now = System.currentTimeMillis();
        if (Math.abs(now - payParam.getPayToAccTime()) > 60000) {
            return new BaseResponse("10","时间超限");
        }

        String signStr = salt
                +payParam.getMerchantID()
                +payParam.getOrderID()
                +payParam.getPrice()
                +payParam.getType()
                +payParam.getPayToAccTime();
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes(StandardCharsets.UTF_8));
        if (! payParam.getSign().equals(sign)){
            return new BaseResponse("11","鉴权错误");
        }

        PayMessage.Device device = payMessage.getDevices().get(payParam.getMerchantID());
        if (device == null){
            return new BaseResponse("12","无法获取到商户所对应的音响设备");
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        String str = device.getIMEI() + '|' +
//                "1007" + '|' +
//                payParam.getOrderID() + '|' +
//                payParam.getPrice() + '|' +
//                payParam.getType();
//
//        byte[] msg = null;
//        try {
//            msg = str.getBytes("GBK");
//        } catch (UnsupportedEncodingException e) {
//            return new BaseResponse("14","消息编码异常");
//        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JSONObject map = new JSONObject();
        map.put("type","tts_dynamic"); // zkc 设备厂商定死
        map.put("msgid", payParam.getOrderID());
        String txt = payMessage.getPayTypes().get(payParam.getType());
        if (txt == null){
            return new BaseResponse("13","无此项协议");
        }
        map.put("txt", txt+payParam.getPrice()+"元");

//        map.put("ttspara","&volume=100&speech_rate=0&pitch_rate=0&voice=ruoxi"); // 其他参数 产考zkc文档
        byte[] msg;
        try {
            msg = map.toString().getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return new BaseResponse("14","消息编码异常");
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MqttMessage message = new MqttMessage(msg);
        message.setQos(qosLevel);
        try{
            if (isAlibaba){
                mqttClient.publish(parentTopic+"/p2p/"+groupID+"@@@"+device.getIMEI(), message);
            }else {
                mqttClient.publish(device.getIMEI(), message);
            }
        }catch (Exception ignored){
            return new BaseResponse("15","发送消息异常");
        }

        return new BaseResponse("00","OK");
    }
}
