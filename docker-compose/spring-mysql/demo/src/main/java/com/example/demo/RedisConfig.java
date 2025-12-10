package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer: use your own Jackson ObjectMapper directly
        template.setValueSerializer(new JacksonRedisSerializer<>(Object.class, new ObjectMapper()));
        template.setHashValueSerializer(new JacksonRedisSerializer<>(Object.class, new ObjectMapper()));

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ApplicationRunner redisChecker(RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            // Force a ping; throws exception if Redis is unreachable
            String pong = redisTemplate.getConnectionFactory().getConnection().ping();
            System.out.println("Redis ping response: " + pong);
        };
    }
}
