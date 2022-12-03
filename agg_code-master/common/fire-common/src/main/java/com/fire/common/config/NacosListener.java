package com.fire.common.config;

import com.fire.common.data.CommonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RefreshScope
public class NacosListener implements ApplicationListener<RefreshEvent> {
    @Value("${sleep.consume:false}")
    private Boolean sleepConsume;
    /**
     * 此处配置刷新时间
     *
     * @param refreshEvent 刷新事件
     */
    @Override
    public void onApplicationEvent(@SuppressWarnings("NullableProblems") RefreshEvent refreshEvent) {
        CommonData.sleepConsume = sleepConsume;
        log.info("nacos配置刷新" + sleepConsume);
    }
}
