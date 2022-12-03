package com.fire.api.controller;


import com.fire.api.request.MqttPayParam;
import com.fire.api.service.MqttMessageService;
import com.fire.dto.response.BaseResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



@Api(tags = "VoiceMessage")
@RestController
@RequestMapping("/voice")
public class MqttMessage {

    @Resource
    private MqttMessageService mqttMessageService;

    @PostMapping("/message")
    @ApiOperation(value = "Mqtt dynamicMessage")
    public BaseResponse dynamicMessage(@RequestBody MqttPayParam pay) throws Exception {
//        MqttMessage message = new MqttMessage(msg.getBytes("GBK"));
//        message.setQos(2);
//        sampleClient.publish("861714050000847", message);
        return mqttMessageService.payMessage(pay);

    }
}