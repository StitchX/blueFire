package com.fire.admin.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.admin.constant.RocketTopicConstant;
import com.fire.admin.entity.OtherCertificates;
import com.fire.admin.entity.WxUserInfo;
import com.fire.admin.service.impl.WxUserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:14]
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.WECHAT,
        consumerGroup = RocketTopicConstant.WECHAT + "-" +"${rocketmq.consumer.group}",
        selectorExpression = "${rocketmq.areaTag}"
)
public class WxUserInfoInsertConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private WxUserInfoServiceImpl wxUserInfoService;

    @Override
    public void onMessage(MessageExt msg) {

        String s = new String(msg.getBody());
        log.info("微信用户消息:msgId {}, tag {}, 消息体 {}", msg.getMsgId(), msg.getTags(), s);
        ObjectMapper om = new ObjectMapper();

        WxUserInfo wxUserInfo = null;
        try {
            wxUserInfo = om.readValue(s, WxUserInfo.class);
        } catch (JsonProcessingException e) {
            log.error("微信用户消费json解析异常：",e);
        }

        if(ObjectUtil.isNull(wxUserInfo)){
            return;
        }

        if(wxUserInfo.getOpenId() == null) {
            log.info("WxUserInfoInsertConsumer: wxUserInfo的openId为null,消息过滤：{}", wxUserInfo);
            return;
        }

        LambdaQueryWrapper<WxUserInfo> wrapper = new LambdaQueryWrapper<WxUserInfo>()
                .eq(WxUserInfo::getWxUserId, wxUserInfo.getWxUserId());
        int count = wxUserInfoService.count(wrapper);
        if(count > 0){
            LambdaUpdateWrapper<WxUserInfo> updateWrapper = new LambdaUpdateWrapper<WxUserInfo>()
                    .eq(WxUserInfo::getWxUserId, wxUserInfo.getWxUserId());
            boolean result = wxUserInfoService.update(wxUserInfo, updateWrapper);
            if(!result){
                log.warn("WxUserInfoInsertConsumer：更新消息失败, 消息内容：{}]", wxUserInfo);
            }else {
                log.info("WxUserInfoInsertConsumer: 微信用户已存在,更新消息内容：{}", wxUserInfo);
            }
        } else{
            boolean result = wxUserInfoService.save(wxUserInfo);
            if(!result){
                log.warn("WxUserInfoInsertConsumer：更新消息失败, 消息内容：{}]", wxUserInfo);
            }else {
                log.info("WxUserInfoInsertConsumer: 消费微信用户消息：{}", wxUserInfo);
            }
        }
    }
}
