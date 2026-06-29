package com.example.kardex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KardexApplication {
    public static void main(String[] args) {
        SpringApplication.run(KardexApplication.class, args);
    }
}