package com.comradegenrr.auto_chinese_fortune.dto;

import java.util.ArrayList;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FortuneRequest {

    private String username;

    @NotBlank
    private String asking;

    private String guaXiang;

    private ArrayList<Integer> sangShu;

    private Date date;
}
