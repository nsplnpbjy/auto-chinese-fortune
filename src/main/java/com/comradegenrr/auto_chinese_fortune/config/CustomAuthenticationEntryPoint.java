package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, java.io.IOException {
        AuthResponse authResponse = ExceptionAdvicor.handle(authException);
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 使用 Jackson 将 User 对象转换为 JSON 格式
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(authResponse);

        // 写入 JSON 数据到响应体
        response.getWriter().write(json);
    }
}