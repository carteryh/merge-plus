package com.cloud.user;

import com.mergeplus.annonation.GetMerge;
import com.mergeplus.annonation.HttpMerge;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@HttpMerge(url = "http://localhost:9002")
public interface StaticHttpClient {
 
	@GetMerge(value = "/user/dic/merge/{typeCode}")
    Map<String, String> merge(@PathVariable(name = "typeCode", required = true) String typeCode);
    
}
