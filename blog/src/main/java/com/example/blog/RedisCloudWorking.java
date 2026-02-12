package com.example.blog;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class RedisCloudWorking {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisCloudWorking(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
//    @PostConstruct
//    public void pingRedis() {
//        try {
//            redisTemplate.opsForValue().set("ping", "pong");
//            System.out.println("Redis says: " +
//                    redisTemplate.opsForValue().get("ping"));
//        } catch (Exception e) {
//            log.error("Redis connection failed", e);
//        }
//    }
//    @PostConstruct
    public void pingRedis() {
        try {
//            assert redisTemplate.getConnectionFactory() != null;
//            String pong = redisTemplate.getConnectionFactory()
//                    .getConnection()
//                    .ping();
//            log.info("Redis PING response = {}", pong);
            redisTemplate.opsForValue().set("ping", "ram");
            log.info("Redis says: " +
                    redisTemplate.opsForValue().get("ping"));
        } catch (Exception e) {
            log.error("Redis connection failed", e);
        }
    }
}
