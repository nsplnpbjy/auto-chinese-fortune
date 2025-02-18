package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.TokenNotValidateException;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.UserAlreadyExistException;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.UserNotAvilableException;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.UserNotExistException;
import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;
import com.comradegenrr.auto_chinese_fortune.dto.STDResponse;

public class ExceptionAdvicor {

    public static STDResponse handle(AuthenticationException e) {
        switch (e.getClass().getName()) {
            case "org.springframework.security.authentication.BadCredentialsException":
                return new AuthResponse("wrong username or password: " + e.getMessage(), false);
            case "com.comradegenrr.auto_chinese_fortune.config.UserAlreadyExistException":
                UserAlreadyExistException u = (UserAlreadyExistException) e;
                return u.getAuthResponse();
            case "com.comradegenrr.auto_chinese_fortune.config.TokenNotValidateException":
                TokenNotValidateException t = (TokenNotValidateException) e;
                return t.getAuthResponse();
            case "com.comradegenrr.auto_chinese_fortune.config.FortuneFailedException":
                FortuneFailedException a = (FortuneFailedException) e;
                return a.getFortuneResponse();
            case "org.springframework.security.authentication.InternalAuthenticationServiceException":
                Throwable cause = e.getCause();
                if (cause instanceof UserNotExistException) {
                    UserNotExistException u1 = (UserNotExistException) cause;
                    return u1.getAuthResponse();
                }
                if (cause instanceof UserNotAvilableException) {
                    UserNotAvilableException u2 = (UserNotAvilableException) cause;
                    return u2.getAuthResponse();
                }
                // 如果 InternalAuthenticationServiceException 的 cause 不是上述两种异常，则默认处理
                return new AuthResponse("InternalAuthenticationServiceException: " + e.getMessage(), false);
            default:
                return new AuthResponse("Authentication failed", false);
        }
    }
}