package com.blue.save.mqconsumer;

import com.fire.dto.entity.WxUserInfo;
import com.blue.save.service.WxUserInfoService;
import com.fire.dto.constants.RocketTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 消费支付宝微信授权用户信息到数据库
 * @date 2022/3/14 10:17
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketTopicConstant.WX_USER_INFO,
        consumerGroup = "WX_USER_INFO-DB")
public class WxUserInfoDBConsumer implements RocketMQListener<WxUserInfo> {

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Override
    public void onMessage(WxUserInfo wxUserInfo) {
        log.info("[WxUserInfoDBConsumer: onMessage][消息内容：{}]", wxUserInfo);
        try {
            wxUserInfo.setIncomingDate(DateFormatter.format(wxUserInfo.getIncomingDate()));
            wxUserInfoService.insert(wxUserInfo);
        } catch (Exception e) {
            log.warn("[WxUserInfoDBConsumer: onMessage][插入消息失败, 消息内容：{}]", wxUserInfo);
        }
    }
}
