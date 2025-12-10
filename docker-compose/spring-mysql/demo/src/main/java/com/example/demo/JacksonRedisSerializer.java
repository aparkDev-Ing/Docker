package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public JacksonRedisSerializer(Class<T> type, ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) return null;
        try {
            return objectMapper.readValue(bytes, type);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize", e);
        }
    }
}
