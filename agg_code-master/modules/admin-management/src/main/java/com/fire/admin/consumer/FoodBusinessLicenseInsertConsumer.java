package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.BusinessLicense;
import com.fire.admin.entity.FoodBusinessLicense;
import com.fire.admin.service.impl.FoodBusinessLicenseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/14 9:57]
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = RocketTopicConstant.FOOD_BUSINESS_LICENSE,
        consumerGroup = RocketTopicConstant.FOOD_BUSINESS_LICENSE + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}")
public class FoodBusinessLicenseInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private FoodBusinessLicenseServiceImpl foodBusinessLicenseService;

    @Override
    public void onMessage(MessageExt msg) {

        String s = new String(msg.getBody());
        log.info("食品卫生安全执照消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        FoodBusinessLicense foodBusinessLicense = null;
        try {
            foodBusinessLicense = om.readValue(s, FoodBusinessLicense.class);
        } catch (JsonProcessingException e) {
            log.error("食品卫生安全执照消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(foodBusinessLicense)){
            return;
        }
        LambdaQueryWrapper<FoodBusinessLicense> wrapper = new LambdaQueryWrapper<FoodBusinessLicense>()
                .eq(FoodBusinessLicense::getFoodBusinessLicenseId, foodBusinessLicense.getFoodBusinessLicenseId());
        int count = foodBusinessLicenseService.count(wrapper);
        if(count > 0){
            LambdaUpdateWrapper<FoodBusinessLicense> updateWrapper = new LambdaUpdateWrapper<FoodBusinessLicense>()
                    .eq(FoodBusinessLicense::getFoodBusinessLicenseId, foodBusinessLicense.getFoodBusinessLicenseId());
            boolean result = foodBusinessLicenseService.update(foodBusinessLicense, updateWrapper);
            if(!result){
                log.warn("FoodBusinessLicenseInsertConsumer：更新消息失败, 消息内容：{}]", foodBusinessLicense);
            }else {
                log.info("FoodBusinessLicenseInsertConsumer: 成功更新食品安全信息消息：{}", foodBusinessLicense);
            }
        } else{
            boolean result = foodBusinessLicenseService.save(foodBusinessLicense);
            if(!result){
                log.warn("FoodBusinessLicenseInsertConsumer：插入消息失败, 消息内容：{}]", foodBusinessLicense);
            }else {
                log.info("FoodBusinessLicenseInsertConsumer: 消费食品安全消息：{}", foodBusinessLicense);
            }
        }
    }
}
