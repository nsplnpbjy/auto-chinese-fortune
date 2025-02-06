package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import lombok.Getter;

@Getter
public class UserNotAvilableException extends AuthenticationException {

    private AuthResponse authResponse;

    public UserNotAvilableException(String message) {
        super(message);
        authResponse = new AuthResponse("UserNotAvilableException: " + message, false);
    }

}
