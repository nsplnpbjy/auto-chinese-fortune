package com.comradegenrr.auto_chinese_fortune.config.AOP;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.comradegenrr.auto_chinese_fortune.dto.STDResponse;
import com.comradegenrr.auto_chinese_fortune.filter.JwtTokenFilter;
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.util.JwtTokenProvider;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAOP {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Around("execution(* com.comradegenrr.auto_chinese_fortune.controller.AiChatController.postMethodName(..))")
    public Object logAiChat(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String username = jwtTokenProvider.getUsernameFromToken(jwtTokenFilter.getTokenFromRequest(request));
        log.info(username + " do chat");

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 方法执行后
        ChatMessage chatMessage = (ChatMessage) result;
        System.out.println(username + "get chat response：" + new Gson().toJson(chatMessage));

        return result;
    }

}
