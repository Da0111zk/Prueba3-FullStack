package com.example.ms_proveedores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsProveedoresApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsProveedoresApplication.class, args);
    }
}
