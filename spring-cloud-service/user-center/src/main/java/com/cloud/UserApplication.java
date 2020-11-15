package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan
@EnableFeignClients
public class UserApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class, args);
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
