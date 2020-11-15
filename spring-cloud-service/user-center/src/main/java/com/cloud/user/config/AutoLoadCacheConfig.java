package com.cloud.user.config;

import com.jarvis.cache.serializer.FastjsonSerializer;
import com.jarvis.cache.serializer.ISerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AutoLoadCacheConfig {

	@Bean
	public ISerializer<Object> autoloadCacheSerializer() {
		ISerializer<Object> res = new FastjsonSerializer();
//		log.debug("auto load cache serializer FastjsonSerializer ");
		return res;
	}
}
