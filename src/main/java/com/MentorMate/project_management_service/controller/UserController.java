package com.MentorMate.project_management_service.controller;

import com.MentorMate.project_management_service.dto.AuthResponseDto;
import com.MentorMate.project_management_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pm/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFromAuthService(id));
    }
}

