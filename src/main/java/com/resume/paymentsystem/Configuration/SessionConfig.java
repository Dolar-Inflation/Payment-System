package com.resume.paymentsystem.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {

//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        return new GenericJacksonJsonRedisSerializer(objectMapper);
//    }

}