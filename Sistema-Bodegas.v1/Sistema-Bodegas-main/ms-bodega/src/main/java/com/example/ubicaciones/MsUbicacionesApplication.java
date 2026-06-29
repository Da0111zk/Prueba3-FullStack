package com.example.ubicaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsUbicacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUbicacionesApplication.class, args);
	}

}
