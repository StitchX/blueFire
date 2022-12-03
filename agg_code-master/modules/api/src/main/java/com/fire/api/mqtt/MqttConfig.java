package com.fire.api.mqtt;



import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

/**
 * zkc 云音响mqtt Bean
 */
@Data
@Configuration
public class MqttConfig {

    @Value("${mqtt.isAlibaba:false}")
    private boolean isAlibaba;

    @Value("${mqtt.userName:mqttpay}")
    private String userName;
    @Value("${mqtt.password:mqttpay}")
    private String password;

    /**
     * 标注mqtt/阿里云 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
     */
    @Value("${mqtt.address:39.98.75.131}")
    private String address;

    @Value("${mqtt.port:1883}")
    private String port;

    /**
     * MQ4IOT 实例 ID，购买后控制台获取
     */
    @Value("${mqtt.instanceId:null}")
    private String instanceId;

    /**
     * 账号 accesskey，从账号系统控制台获取
     */
    @Value("${mqtt.accessKey:null}")
    private String accessKey;

    /**
     * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
     */
    @Value("${mqtt.secretKey:null}")
    private String secretKey;

    /**
     * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
     * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
     */
    @Value("${mqtt.groupID:null}")
    private String groupID;
    @Value("${mqtt.deviceId:null}")
    private String deviceId;

    /**
     * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
     */
    @Value("${mqtt.qosLevel:0}")
    private int qosLevel;
    @Value("${mqtt.cleanSession:true}")
    private boolean cleanSession;

    /**
     * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。
     * 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
     */
    @Value("${mqtt.parentTopic:null}")
    private String parentTopic;


    /**
     * 阿里云MQTT 签名鉴权模式
     */
    MqttConnectOptions mqttConnectOptions() throws Exception{
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        if (isAlibaba){
            if (instanceId.equals("null") || accessKey.equals("null") || groupID.equals("null") || secretKey.equals("null")){
                throw new Exception("instanceId or accessKey or groupID or secretKey is null");
            }
            mqttConnectOptions.setUserName("Signature|" + accessKey + "|" + instanceId);
            mqttConnectOptions.setPassword(macSignature(groupID+"@@@"+deviceId, secretKey).toCharArray());
        }else{
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
        }

        mqttConnectOptions.setCleanSession(cleanSession); // 阿里云demo中 默认为true
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MQTT_VERSION_3_1_1); // 阿里云demo中 的
        mqttConnectOptions.setConnectionTimeout(5000);
        return  mqttConnectOptions;
    }

    public static String macSignature(String text, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        Charset charset = StandardCharsets.UTF_8;
        String algorithm = "HmacSHA1";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
        byte[] bytes = mac.doFinal(text.getBytes(charset));
        return new String(Base64.encodeBase64(bytes), charset);
    }

    @Bean
    public MqttClient mqttClient() throws Exception{
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        MqttClient mqttClient = new MqttClient("tcp://" + address + ":"+port, groupID+"@@@"+deviceId, memoryPersistence);
        mqttClient.setTimeToWait(5000);
        mqttClient.connect(mqttConnectOptions());
        return mqttClient;
    }


}
