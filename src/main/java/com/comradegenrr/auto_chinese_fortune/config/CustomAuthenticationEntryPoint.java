package com.comradegenrr.auto_chinese_fortune.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, java.io.IOException {
        AuthResponse authResponse = ExceptionAdvicor.handle(authException);
        response.getOutputStream() // getOutputStream() throws IOException
                .println(authResponse.toString());
    }
}