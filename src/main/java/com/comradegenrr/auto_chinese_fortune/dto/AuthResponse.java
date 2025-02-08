package com.comradegenrr.auto_chinese_fortune.dto;

import lombok.Data;

@Data
public class AuthResponse extends STDResponse {

    private boolean success = true;

    private String message = "Authentication successful";

    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.token = null;
    }

    // getter
}