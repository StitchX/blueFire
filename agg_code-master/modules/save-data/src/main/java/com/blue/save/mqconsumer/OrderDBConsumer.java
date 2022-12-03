package com.blue.save.mqconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.entity.OrderList;
import com.blue.save.service.OrderListService;
import com.fire.dto.constants.RocketTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fire.dto.constants.RocketTagConstant.MAKE_TAG;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费订单消息存储到mysql
 * @date 2022/3/9 14:29
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.ORDER,
        consumerGroup = "ORDER-DB")
public class OrderDBConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private OrderListService orderListService;

    @Override
    public void onMessage(MessageExt msg) {
        String msgBody = new String(msg.getBody());
        String tags = msg.getTags();
        log.info("[OrderDBConsumer: onMessage][消息内容：{}][tag:{}]", msgBody, tags);
        ObjectMapper om = new ObjectMapper();
        try {
            OrderList orderList = om.readValue(msgBody, OrderList.class);
            orderList.setCreateTime(DateFormatter.format(orderList.getCreateTime()));
            orderList.setCallbackTime(DateFormatter.format(orderList.getCallbackTime()));
            try {
                if (tags.equals(MAKE_TAG)) {
                    orderListService.insert(orderList);
                } else {
                    orderListService.update(orderList);
                }
            } catch (Exception e) {
                log.error("订单：".concat(String.valueOf(orderList.getOrderId())).concat("    异常：入DB失败"), e);
            }
        } catch (Exception e) {
            log.error("订单消费json解析错误：", e);
        }
    }
}
