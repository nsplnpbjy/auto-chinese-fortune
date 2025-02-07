package com.comradegenrr.auto_chinese_fortune.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/1")
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.ok("1");
    }

}
