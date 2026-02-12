package com.example.blog.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper(){
     return new ModelMapper();
    }

    @Bean
    public Cloudinary getCloudinary(){
        Map map = new HashMap();
        map.put("cloud_name", "dcq0mn7hp");
        map.put("api_key", "425453158213147");
        map.put("api_secret", "9PNQahYsHiJyOp5GhdT35OZO0Jw");
        map.put("secure",true);
        return new Cloudinary(map);
    }

/*    @Bean
    public RedisTemplate getRedisTemplate(RedisConnectionFactory factory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return new  RedisTemplate();
    }*/


}
