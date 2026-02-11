package com.MentorMate.project_management_service.service;

import com.MentorMate.project_management_service.client.AuthClient;
import com.MentorMate.project_management_service.dto.AuthResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthClient authClient;

    public UserService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public AuthResponseDto getUserFromAuthService(Long id) {
        return authClient.getUserById(id);
    }
}
