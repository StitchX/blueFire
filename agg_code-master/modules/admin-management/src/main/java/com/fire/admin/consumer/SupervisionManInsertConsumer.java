package com.fire.admin.consumer;

import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.SupervisionMan;
import com.fire.admin.service.impl.SupervisionManServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.SUPERVISION_MAN,
        consumerGroup = RocketTopicConstant.SUPERVISION_MAN + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class SupervisionManInsertConsumer implements RocketMQListener<SupervisionMan>  {
    @Autowired
    private SupervisionManServiceImpl supervisionManService;

    @Override
    public void onMessage(SupervisionMan supervisionMan) {
        log.info("[SupervisionManInsertConsumer: onMessage][消息内容：{}]", supervisionMan);
        boolean result = supervisionManService.save(supervisionMan);
        if(!result){
            log.warn("[SupervisionManInsertConsumer: onMessage][插入或更新消息失败, 消息内容：{}]", supervisionMan);
        }
    }
}
