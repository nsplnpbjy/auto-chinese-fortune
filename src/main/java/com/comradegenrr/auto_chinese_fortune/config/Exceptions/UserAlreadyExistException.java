package com.comradegenrr.auto_chinese_fortune.config.Exceptions;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import lombok.Getter;

@Getter
public class UserAlreadyExistException extends AuthenticationException {

    private AuthResponse authResponse;

    public UserAlreadyExistException(String message) {
        super(message);
        authResponse = new AuthResponse("UserAlreadyExistException: " + message, false);
    }

}
