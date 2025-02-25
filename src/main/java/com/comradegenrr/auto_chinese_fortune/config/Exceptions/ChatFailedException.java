package com.comradegenrr.auto_chinese_fortune.config.Exceptions;

import org.springframework.security.core.AuthenticationException;

import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;

import lombok.Getter;

@Getter
public class ChatFailedException extends AuthenticationException {

    private FortuneResponse fortuneResponse;

    public ChatFailedException(String msg) {
        super(msg);
        fortuneResponse = new FortuneResponse();
        fortuneResponse.setSuccess(false);
    }

}
