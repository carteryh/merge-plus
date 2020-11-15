package com.mergeplus.config;

import com.mergeplus.aspect.MergeAspect;
import com.mergeplus.core.MergeCore;
import com.mergeplus.handler.CollectionHandler;
import com.mergeplus.handler.MergeInfoHandler;
import com.mergeplus.handler.ObjectHandler;
import com.mergeplus.handler.ValidateHandler;
import com.mergeplus.request.GetMergeRequest;
import com.mergeplus.request.PostMergeRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeAutoConfiguration
 * 类 描 述：TODO
 * 创建时间：2020/10/23 1:56 下午
 * 创 建 人：chenyouhong
 */
@Configuration
@Import({MergeAspect.class})
public class MergeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MergeCore mergeCore() {
        return new MergeCore();
    }

    @Bean
    @ConditionalOnMissingBean
    public ValidateHandler validateHandler() {
        return new ValidateHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CollectionHandler collectionHandler() {
        return new CollectionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectHandler objectHandler() {
        return new ObjectHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public MergeInfoHandler mergeInfoHandler() {
        return new MergeInfoHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MergeAspect mergeAspect() {
//        return new MergeAspect();
//    }


    @Bean
    @ConditionalOnMissingBean
    public GetMergeRequest getMergeRequest() {
        return new GetMergeRequest();
    }


    @Bean
    @ConditionalOnMissingBean
    public PostMergeRequest postMergeRequest() {
        return new PostMergeRequest();
    }


}
