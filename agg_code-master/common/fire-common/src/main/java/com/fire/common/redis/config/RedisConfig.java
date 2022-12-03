package com.fire.common.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import java.util.HashSet;
import java.util.Set;


@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();

        try {
            String[] clusNodes = clusterNodes.split(",");

            for (String clusNode : clusNodes) {
                String[] hostAndPort = clusNode.split(":");
                nodes.add(new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
            }

        } catch (Exception e) {
            log.error("Redis服务异常:", e);
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxTotal(maxActive);
        return new JedisCluster(nodes, timeout, timeout, 4, password, jedisPoolConfig);
    }

}
