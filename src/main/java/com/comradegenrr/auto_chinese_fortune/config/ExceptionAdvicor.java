package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

public class ExceptionAdvicor {

    static public AuthResponse handle(AuthenticationException e) {
        switch (e.getClass().getName()) {
            case "org.springframework.security.authentication.BadCredentialsException":
                return new AuthResponse("wrong username or password: " + e.getMessage(), false);
            case "com.comradegenrr.auto_chinese_fortune.config.UserAlreadyExistException":
                return new AuthResponse("UserAlreadyExistException: " + e.getMessage(), false);
            case "com.comradegenrr.auto_chinese_fortune.config.TokenNotValidateException":
                return new AuthResponse("TokenNotValidateException: " + e.getMessage(), false);
            case "org.springframework.security.authentication.InternalAuthenticationServiceException":
                Throwable cause = e.getCause();
                if (cause instanceof UserNotExistException) {
                    return new AuthResponse("UserNotExistException: " + cause.getMessage(), false);
                }
                if (cause instanceof UserNotAvilableException) {
                    return new AuthResponse("UserNotAvilableException: " + cause.getMessage(), false);
                }
                // 如果 InternalAuthenticationServiceException 的 cause 不是上述两种异常，则默认处理
                return new AuthResponse("InternalAuthenticationServiceException: " + e.getMessage(), false);
            default:
                return new AuthResponse("Authentication failed", false);
        }
    }
}