package com.MentorMate.project_management_service.client;

import com.MentorMate.project_management_service.dto.AuthResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AuthService", url = "http://localhost:8080")
public interface AuthClient {

    // Example: Get user details from AuthService by ID
    @GetMapping("/users/{id}")
    AuthResponseDto getUserById(@PathVariable("id") Long id);
}