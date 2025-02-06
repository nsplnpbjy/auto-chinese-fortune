package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import lombok.Getter;

@Getter
public class UserNotExistException extends AuthenticationException {

    private AuthResponse authResponse;

    public UserNotExistException(String message) {
        super(message);
        authResponse = new AuthResponse("UserNotExistException: " + message, false);

    }

}
