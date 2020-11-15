package com.cloud.user.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 项目名称：lz-cloud-crm
 * 类 名 称：RedisConfiguration
 * 类 描 述：TODO
 * 创建时间：2020/11/2 1:28 下午
 * 创 建 人：chenyouhong
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();


        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));

        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }

}
