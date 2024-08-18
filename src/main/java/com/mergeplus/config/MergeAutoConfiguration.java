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
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeAutoConfiguration
 * 类 描 述：合并自动注入
 * 创建时间：2020/10/23 1:56 下午
 * 创 建 人：chenyouhong
 */
@Import({MergeAspect.class})
public class MergeAutoConfiguration {

    /**
     * mergeCore
     * @return MergeCore
     */
    @Bean
//    @Primary
    @ConditionalOnMissingBean
    public MergeCore mergeCore() {
        return new MergeCore();
    }

    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public ValidateHandler validateHandler() {
        return new ValidateHandler();
    }

    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public CollectionHandler collectionHandler() {
        return new CollectionHandler();
    }

    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectHandler objectHandler() {
        return new ObjectHandler();
    }

    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public MergeInfoHandler mergeInfoHandler() {
        return new MergeInfoHandler();
    }

    /**
     *
     * @return 返回
     */
//    @Bean
//    @ConditionalOnMissingBean(name = "redisTemplate")
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        return template;
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MergeAspect mergeAspect() {
//        return new MergeAspect();
//    }

    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public GetMergeRequest getMergeRequest() {
        return new GetMergeRequest();
    }


    /**
     *
     * @return 返回
     */
    @Bean
    @ConditionalOnMissingBean
    public PostMergeRequest postMergeRequest() {
        return new PostMergeRequest();
    }


}
