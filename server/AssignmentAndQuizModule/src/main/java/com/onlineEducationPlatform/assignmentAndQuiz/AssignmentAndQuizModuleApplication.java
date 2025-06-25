package com.onlineEducationPlatform.assignmentAndQuiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AssignmentAndQuizModuleApplication {




	public static void main(String[] args) {
		SpringApplication.run(AssignmentAndQuizModuleApplication.class, args);
	}

}
