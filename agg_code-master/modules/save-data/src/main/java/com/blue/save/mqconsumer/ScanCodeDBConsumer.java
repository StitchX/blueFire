package com.blue.save.mqconsumer;

import com.fire.dto.entity.ScanCodeCount;
import com.blue.save.service.ScanCodeCountService;
import com.fire.dto.constants.RocketTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费扫码记录消息存储到mysql
 * @date 2022/3/9 14:37
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.SCAN,
        consumerGroup = "SCAN-DB")
public class ScanCodeDBConsumer implements RocketMQListener<ScanCodeCount> {

    @Autowired
    private ScanCodeCountService scanCodeCountService;

    @Override
    public void onMessage(ScanCodeCount scanCodeCount) {
        log.info("[ScanCodeCountInsertConsumer: onMessage][消息内容：{}]", scanCodeCount);
        if(scanCodeCount.getMerchantId() == null){
            return;
        }
        scanCodeCount.setCreateTime(DateFormatter.format(scanCodeCount.getCreateTime()));
        boolean result = scanCodeCountService.insert(scanCodeCount);
        if(!result){
            log.warn("[ScanCodeCountInsertConsumer: onMessage][插入消息失败, 消息内容：{}]", scanCodeCount);
        }
    }
}
