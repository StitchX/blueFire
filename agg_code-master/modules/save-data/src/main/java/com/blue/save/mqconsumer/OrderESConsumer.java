package com.blue.save.mqconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.constants.RocketTopicConstant;
import com.fire.dto.entity.OrderList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.fire.dto.constants.EsIndex.ORDER_INDEX;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费订单消息存储到es
 * @date 2022/3/9 14:37
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.ORDER,
        consumerGroup = "ORDER-ES")
public class OrderESConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void onMessage(MessageExt msg) {
        String msgBody = new String(msg.getBody());
        String tags = msg.getTags();
        log.info("[OrderESConsumer: onMessage][消息内容：{}][tag:{}]", msgBody, tags);
        ObjectMapper om = new ObjectMapper();
        try {
            OrderList orderList = om.readValue(msgBody, OrderList.class);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(orderList.getOrderId().toString());
            orderList.setCreateTime(DateFormatter.format(orderList.getCreateTime()));
            orderList.setCallbackTime(DateFormatter.format(orderList.getCallbackTime()));
            indexQuery.setObject(orderList);
            IndexCoordinates indexCoordinates = IndexCoordinates.of(ORDER_INDEX);
            try {
                elasticsearchRestTemplate.index(indexQuery, indexCoordinates);
            } catch (Exception e) {
                log.error("订单：".concat(String.valueOf(orderList.getOrderId())).concat("    异常：入ES失败"), e);
            }
        } catch (Exception e) {
            log.warn("[OrderESConsumer: onMessage][插入消息失败, 消息内容：{}]", msgBody);
        }
    }
}
