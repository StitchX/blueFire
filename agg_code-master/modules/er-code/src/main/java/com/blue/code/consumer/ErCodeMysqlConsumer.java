package com.blue.code.consumer;

import com.blue.code.service.CreateErCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.mqDto.CodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.fire.dto.constants.RocketTopicConstant.MAKE_ER_CODE;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "ErCodeMysql", topic = MAKE_ER_CODE, consumeMode = ConsumeMode.ORDERLY)
public class ErCodeMysqlConsumer  implements RocketMQListener<MessageExt> {

    @Resource
    private CreateErCode createErCode;


    @Override
    public void onMessage(MessageExt msg) {

        String s = new String(msg.getBody());
        log.info("ErCodeMysqlConsumer consumer message: " + s);
        ObjectMapper om = new ObjectMapper();

        try {
            CodeMessage codeMessage = om.readValue(s,CodeMessage.class);

            createErCode.makeMerchantInfoCode(codeMessage);
            createErCode.makeMerchantPayCode(codeMessage);
        } catch (Exception e) {
            log.error("创建商户二维码消费json解析异常：",e);
        }


    }
}
