package com.onlineEducationPlatform.Communication.Module.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "http://localhost:8080")
public interface UserServiceClient {
    
    @GetMapping("/api/users/verify/{userId}")
    Boolean verifyUser(@PathVariable String userId);
}