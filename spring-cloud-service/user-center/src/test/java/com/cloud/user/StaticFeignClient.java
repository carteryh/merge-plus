package com.cloud.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-center", url = "http://localhost:9002")
public interface StaticFeignClient {
 
	@GetMapping("/user/dic/merge/{typeCode}")
    Map<String, String> merge(@PathVariable(name = "typeCode", required = true) String typeCode);

}
