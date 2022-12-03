package com.fire.admin.consumer;

import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.ScanCodeCount;
import com.fire.admin.entity.WxUserInfo;
import com.fire.admin.service.impl.ScanCodeCountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:30]
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.SCAN,
        consumerGroup = RocketTopicConstant.SCAN + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class ScanCodeCountInsertConsumer implements RocketMQListener<ScanCodeCount> {
    @Autowired
    private ScanCodeCountServiceImpl scanCodeCountService;

    @Override
    public void onMessage(ScanCodeCount scanCodeCount) {
        log.info("[ScanCodeCountInsertConsumer: onMessage][消息内容：{}]", scanCodeCount);
        if(scanCodeCount.getMerchantId() == null){
            return;
        }
        boolean result = scanCodeCountService.save(scanCodeCount);
        if(!result){
            log.warn("[ScanCodeCountInsertConsumer: onMessage][插入消息失败, 消息内容：{}]", scanCodeCount);
        }
    }
}
