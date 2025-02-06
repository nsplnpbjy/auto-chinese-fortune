package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

public class ExceptionAdvicor {

    static public AuthResponse handle(AuthenticationException e) {
        return new AuthResponse("login failed", false);
    }

}
