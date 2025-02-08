package com.comradegenrr.auto_chinese_fortune.dto;

import lombok.Data;

@Data
public class FortuneResponse extends STDResponse {

    private boolean success;

    private String fortune;

    private String reason;
}
