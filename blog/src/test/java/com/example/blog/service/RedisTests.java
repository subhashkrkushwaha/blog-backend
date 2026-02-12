/*
package com.example.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@EnableCaching
@Disabled
@Slf4j
 class RedisTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

        @Test
        void testSomething(){
            try {

                redisTemplate.opsForList().leftPush("hello","world");
                Object o = redisTemplate.opsForList().rightPop("hello");
                System.out.println(o);
            }catch (Exception e){
                log.error(e.getMessage());
            }

        }
}
*/
