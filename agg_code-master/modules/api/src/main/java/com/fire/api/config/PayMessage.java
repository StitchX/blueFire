package com.fire.api.config;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.data.annotation.Reference;

import java.util.Map;


@Data
@Component
@ConfigurationProperties(prefix = "pay-message")
public class PayMessage {
    private Map<String, String> payTypes;
    @Reference
    private Map<String, Device> devices;

    @Data
    public static class Device{
        private String IMEI;
        private String IccID;
    }

}
