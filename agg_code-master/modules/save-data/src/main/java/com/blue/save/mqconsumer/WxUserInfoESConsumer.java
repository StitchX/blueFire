package com.blue.save.mqconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.constants.RocketTopicConstant;
import com.fire.dto.entity.WxUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.fire.dto.constants.EsIndex.WX_USER_INFO_INDEX;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费支付宝微信授权用户信息到ES
 * @date 2022/3/14 10:17
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.WX_USER_INFO,
        consumerGroup = "WX_USER_INFO-ES")
public class WxUserInfoESConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void onMessage(MessageExt msg) {
        String msgBody = new String(msg.getBody());
        String tags = msg.getTags();
        log.info("[WxUserInfoESConsumer: onMessage][消息内容：{}][tag:{}]", msgBody, tags);
        ObjectMapper om = new ObjectMapper();
        try {
            WxUserInfo wxUserInfo = om.readValue(msgBody, WxUserInfo.class);
            wxUserInfo.setIncomingDate(DateFormatter.format(wxUserInfo.getIncomingDate()));
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(wxUserInfo.getWxUserId());
            indexQuery.setObject(wxUserInfo);
            IndexCoordinates indexCoordinates = IndexCoordinates.of(WX_USER_INFO_INDEX);
            try {
                elasticsearchRestTemplate.index(indexQuery, indexCoordinates);
            } catch (Exception e) {
                log.error("[WxUserInfoESConsumer: onMessage][插入消息失败, 消息内容：{}]", msgBody);
            }
        } catch (Exception e) {
            log.error("[WxUserInfoESConsumer: onMessage][插入消息失败, 消息内容：{}]", msgBody);
        }
    }
}
