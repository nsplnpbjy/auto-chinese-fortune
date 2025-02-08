package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;

import lombok.Getter;

@Getter
public class FortuneFailedException extends AuthenticationException {

    private FortuneResponse fortuneResponse;

    public FortuneFailedException(String message) {
        super(message);
        fortuneResponse = new FortuneResponse();
        fortuneResponse.setSuccess(false);
    }

}
