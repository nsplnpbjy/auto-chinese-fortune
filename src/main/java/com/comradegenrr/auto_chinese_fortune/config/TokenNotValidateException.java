package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import lombok.Getter;

@Getter
public class TokenNotValidateException extends AuthenticationException {

    private AuthResponse authResponse;

    public TokenNotValidateException(String message) {
        super(message);
        authResponse = new AuthResponse("TokenNotValidateException: " + message, false);
    }

}
