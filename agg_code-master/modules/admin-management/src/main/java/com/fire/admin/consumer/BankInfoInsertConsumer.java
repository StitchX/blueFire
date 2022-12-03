package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.BankInfo;
import com.fire.admin.entity.BusinessLicense;
import com.fire.admin.service.impl.BankInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:23]
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.BANK,
        consumerGroup = RocketTopicConstant.BANK + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class BankInfoInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private BankInfoServiceImpl bankInfoService;

    @Override
    public void onMessage(MessageExt msg) {
        String s = new String(msg.getBody());
        log.info("银行消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        BankInfo bankInfo = null;
        try {
            bankInfo = om.readValue(s, BankInfo.class);
        } catch (JsonProcessingException e) {
            log.error("银行信息消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(bankInfo)){
            return;
        }
        bankInfo.setId(null);
        if(bankInfo.getMerchantId() == null){
            log.info("BankInfoInsertConsumer: merchantId为null,过滤该消息： {}", bankInfo);
            return;
        }

        LambdaQueryWrapper<BankInfo> wrapper = new LambdaQueryWrapper<BankInfo>()
                .eq(BankInfo::getMerchantId, bankInfo.getMerchantId())
                .eq(BankInfo::getCustId, bankInfo.getCustId());
        int count = bankInfoService.count(wrapper);
        if(count > 0){
            LambdaUpdateWrapper<BankInfo> updateWrapper = new LambdaUpdateWrapper<BankInfo>()
                    .eq(BankInfo::getMerchantId, bankInfo.getMerchantId())
                    .eq(BankInfo::getCustId, bankInfo.getCustId());
            boolean result = bankInfoService.update(bankInfo, updateWrapper);
            if(!result){
                log.warn("BankInfoInsertConsumer：更新消息失败, 消息内容：{}]", bankInfo);
            }else {
                log.info("BankInfoInsertConsumer: 成功更新银行信息消息：{}", bankInfo);
            }
        } else{

            boolean result = bankInfoService.save(bankInfo);
            if(!result){
                log.warn("BankInfoInsertConsumer：插入消息失败, 消息内容：{}]", bankInfo);
            }else {
                log.info("BankInfoInsertConsumer: 成功消费银行信息消息：{}", bankInfo);
            }
        }
    }
}
