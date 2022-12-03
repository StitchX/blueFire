package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.BankInfo;
import com.fire.admin.entity.BusinessLicense;
import com.fire.admin.entity.Merchant;
import com.fire.admin.service.MerchantService;
import com.fire.admin.service.impl.BusinessLicenseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 11:12]
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.BUSINESS_LICENSE,
        consumerGroup = RocketTopicConstant.BUSINESS_LICENSE + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class BusinessLicenseInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private BusinessLicenseServiceImpl businessLicenseService;

    @Override
    public void onMessage(MessageExt msg) {

        String s = new String(msg.getBody());
        log.info("营业执照消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        BusinessLicense businessLicense = null;
        try {
            businessLicense = om.readValue(s, BusinessLicense.class);
        } catch (JsonProcessingException e) {
            log.error("营业执照消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(businessLicense)){
            return;
        }
        LambdaQueryWrapper<BusinessLicense> wrapper = new LambdaQueryWrapper<BusinessLicense>()
                .eq(BusinessLicense::getBusinessLicenseId, businessLicense.getBusinessLicenseId());
        int count = businessLicenseService.count(wrapper);
        if(count > 0){
            LambdaUpdateWrapper<BusinessLicense> updateWrapper = new LambdaUpdateWrapper<BusinessLicense>()
                    .eq(BusinessLicense::getBusinessLicenseId, businessLicense.getBusinessLicenseId());
            boolean result = businessLicenseService.update(businessLicense, updateWrapper);
            if(!result){
                log.warn("BusinessLicenseInsertConsumer：更新消息失败, 消息内容：{}]", businessLicense);
            }else {
                log.info("BusinessLicenseInsertConsumer: 成功更新银行信息消息：{}", businessLicense);
            }
        } else{
            boolean result = businessLicenseService.save(businessLicense);
            if(!result){
                log.warn("BusinessLicenseInsertConsumer：消费营业执照消息失败, 消息内容：{}]", businessLicense);
            }else {
                log.info("BusinessLicenseInsertConsumer: 成功消费营业执照消息：{}", businessLicense);
            }
        }
    }
}
