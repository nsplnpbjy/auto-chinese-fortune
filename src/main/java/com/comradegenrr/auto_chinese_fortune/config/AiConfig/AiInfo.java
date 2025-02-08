package com.comradegenrr.auto_chinese_fortune.config.AiConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AiInfo {

    @Value("${ai.key}")
    private String aiKey;

    @Value("${ai.url}")
    private String aiUrl;

    @Value("${ai.temperture}")
    private double temperture;

    @Value("${ai.model}")
    private String model;

    @Value("${ai.systemMessage}")
    private String systemMessage;
}
