package com.fire.api.service;

import com.fire.api.request.MqttPayParam;
import com.fire.dto.response.BaseResponse;

public interface MqttMessageService {

    BaseResponse payMessage(MqttPayParam payParam) throws Exception;
}
