package com.comradegenrr.auto_chinese_fortune.controller;

import org.springframework.web.bind.annotation.RestController;

import com.comradegenrr.auto_chinese_fortune.config.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneRequest;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;
import com.comradegenrr.auto_chinese_fortune.service.FortuneService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    @PostMapping("/fortune")
    public FortuneResponse postMethodName(@RequestBody @Valid FortuneRequest fortuneRequest,
            HttpServletRequest request) {
        fortuneRequest.setUsername((String) request.getAttribute("username"));
        try {
            return fortuneService.DoFortune(fortuneRequest);
        } catch (Exception e) {
            throw new FortuneFailedException(e.getMessage());
        }
    }

}
