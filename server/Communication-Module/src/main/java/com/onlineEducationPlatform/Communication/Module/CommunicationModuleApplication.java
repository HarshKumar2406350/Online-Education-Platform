package com.onlineEducationPlatform.Communication.Module;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CommunicationModuleApplication {

	@Bean
	public WebClient webClient(){
		return WebClient.builder().build();
	}

	@Bean
	public ModelMapper modelMapper(){
		return new  ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunicationModuleApplication.class, args);
	}

}
