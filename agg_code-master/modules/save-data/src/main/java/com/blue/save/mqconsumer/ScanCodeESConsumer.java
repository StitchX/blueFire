package com.blue.save.mqconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.constants.RocketTopicConstant;
import com.fire.dto.entity.ScanCodeCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.fire.dto.constants.EsIndex.SCAN_CODE_INDEX;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费扫码记录消息存储到es
 * @date 2022/3/9 14:41
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.SCAN,
        consumerGroup = "SCAN-ES")
public class ScanCodeESConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void onMessage(MessageExt msg) {
        String msgBody = new String(msg.getBody());
        String tags = msg.getTags();
        log.info("[ScanCodeESConsumer: onMessage][消息内容：{}][tag:{}]", msgBody, tags);
        ObjectMapper om = new ObjectMapper();
        try {
            ScanCodeCount scanCodeCount = om.readValue(msgBody, ScanCodeCount.class);
            scanCodeCount.setCreateTime(DateFormatter.format(scanCodeCount.getCreateTime()));
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(scanCodeCount.getId().toString());
            indexQuery.setObject(scanCodeCount);
            IndexCoordinates indexCoordinates = IndexCoordinates.of(SCAN_CODE_INDEX);
            try {
                elasticsearchRestTemplate.index(indexQuery, indexCoordinates);
            } catch (Exception e) {
                log.error("[ScanCodeESConsumer: onMessage][插入消息失败, 消息内容：{}]", msgBody);
            }
        } catch (Exception e) {
            log.error("[ScanCodeESConsumer: onMessage][插入消息失败, 消息内容：{}]", msgBody);
        }
    }
}
