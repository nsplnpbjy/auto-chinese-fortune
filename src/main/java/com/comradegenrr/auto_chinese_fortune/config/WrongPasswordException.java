package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import lombok.Getter;

@Getter
public class WrongPasswordException extends AuthenticationException {

    private AuthResponse authResponse;

    public WrongPasswordException(String message) {
        super(message);
        authResponse = new AuthResponse("WrongPasswordException: " + message, false);

    }

}
