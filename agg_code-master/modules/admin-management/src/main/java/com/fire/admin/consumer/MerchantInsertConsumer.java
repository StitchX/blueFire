package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.FoodBusinessLicense;
import com.fire.admin.entity.Merchant;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.service.MerchantService;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.admin.service.impl.MerchantServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 11:12]
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.MERCHANT,
        consumerGroup = RocketTopicConstant.MERCHANT + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class MerchantInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private MerchantServiceImpl merchantService;

    @Autowired
    private SupervisionBureauService supervisionBureauService;

    @Override
    public void onMessage(MessageExt msg) {
        String s = new String(msg.getBody());
        log.info("商户消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        Merchant merchant = null;
        try {
            merchant = om.readValue(s, Merchant.class);
        } catch (JsonProcessingException e) {
            log.error("商户消息消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(merchant)){
            return;
        }

        SupervisionBureau supervisionBureau = supervisionBureauService.getSupervisionBureauById(merchant.getSupervisionId());
        if(supervisionBureau != null){
            merchant.setSupervisionName(supervisionBureau.getSupervisionName());
        }

        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMerchantId, merchant.getMerchantId());
        int count = merchantService.count(wrapper);
        if(count > 0){
            log.info("MerchantInsertConsumer：商户信息已存在,更新消息内容: {}", merchant);
            LambdaUpdateWrapper<Merchant> updateWrapper = new LambdaUpdateWrapper<Merchant>()
                    .eq(Merchant::getMerchantId, merchant.getMerchantId());
            boolean result = merchantService.update(merchant, updateWrapper);
            if(!result){
                log.warn("MerchantInsertConsumer：更新消息失败, 消息内容：{}]", merchant);
            }else {
                log.info("MerchantInsertConsumer: 成功更新商户消息：{}", merchant);
            }
        } else{
            boolean result = merchantService.save(merchant);
            if(!result){
                log.warn("MerchantInsertConsumer：插入消息失败, 消息内容：{}]", merchant);
            }else {
                log.info("MerchantInsertConsumer: 消费商户消息：{}", merchant);
            }
        }
    }
}
