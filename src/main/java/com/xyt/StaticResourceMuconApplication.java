package com.xyt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StaticResourceMuconApplication {

    public static void main(String[] args) {

          SpringApplication.run (StaticResourceMuconApplication.class, args);
    }

}
