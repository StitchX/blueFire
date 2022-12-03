package com.blue.crm.component;

import com.fire.dto.constants.RocketTopicConstant;
import com.fire.dto.mqDto.CodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/11 11:15]
 */

@Component
@Slf4j
public class ErCodeProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void onewaySend(Long merchantId, String merchantName, String supervisionName){

        log.info("添加商户完成, 向MQ发送ErCode消息 ...");

        CodeMessage codeMessage = CodeMessage.builder()
                .merchantId(merchantId)
                .merchantName(merchantName)
                .supervisionName(supervisionName).build();
        rocketMQTemplate.sendOneWay(RocketTopicConstant.ER_CODE, codeMessage);
    }
}
