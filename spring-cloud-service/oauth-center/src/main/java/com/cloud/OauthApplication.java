package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@MapperScan
public class OauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthApplication.class, args);
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
