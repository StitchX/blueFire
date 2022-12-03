package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.Merchant;
import com.fire.admin.entity.OtherCertificates;
import com.fire.admin.service.impl.OtherCertificatesServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 17:55]
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.OTHER_CERTIFICATES,
        consumerGroup = RocketTopicConstant.OTHER_CERTIFICATES + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class OtherCertificatesInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private OtherCertificatesServiceImpl otherCertificatesService;

    @Override
    public void onMessage(MessageExt msg) {

        String s = new String(msg.getBody());
        log.info("其他执照消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        OtherCertificates otherCertificates = null;
        try {
            otherCertificates = om.readValue(s, OtherCertificates.class);
        } catch (JsonProcessingException e) {
            log.error("其他执照消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(otherCertificates)){
            return;
        }

        LambdaQueryWrapper<OtherCertificates> wrapper = new LambdaQueryWrapper<OtherCertificates>()
                .eq(OtherCertificates::getOtherCertificatesId, otherCertificates.getOtherCertificatesId());
        int count = otherCertificatesService.count(wrapper);
        if(count > 0){
            LambdaUpdateWrapper<OtherCertificates> updateWrapper = new LambdaUpdateWrapper<OtherCertificates>()
                    .eq(OtherCertificates::getOtherCertificatesId, otherCertificates.getOtherCertificatesId());
            boolean result = otherCertificatesService.update(otherCertificates, updateWrapper);
            if(!result){
                log.warn("OtherCertificatesInsertConsumer：更新消息失败, 消息内容：{}]", otherCertificates);
            }else {
                log.info("OtherCertificatesInsertConsumer: 其他证件信息已存在,更新消息内容：{}", otherCertificates);
            }
        } else{
            boolean result = otherCertificatesService.save(otherCertificates);
            if(!result){
                log.warn("OtherCertificatesInsertConsumer：插入消息失败, 消息内容：{}]", otherCertificates);
            }else {
                log.info("OtherCertificatesInsertConsumer: 消费其他证件消息：{}", otherCertificates);
            }
        }
    }
}
