package com.comradegenrr.auto_chinese_fortune.controller;

import org.springframework.web.bind.annotation.RestController;

import com.comradegenrr.auto_chinese_fortune.config.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneRequest;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;
import com.comradegenrr.auto_chinese_fortune.model.ChatCompletionResponse;
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.service.FortuneService;
import com.comradegenrr.auto_chinese_fortune.service.GeneralAiChatService;
import com.comradegenrr.auto_chinese_fortune.util.ChatUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class AiChatController {

    @Autowired
    private FortuneService fortuneService;
    @Autowired
    private GeneralAiChatService generalAiChatService;

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

    @PostMapping("/chat")
    public ChatMessage postMethodName(@RequestBody ChatMessage chatMessage) {

        ChatMessage responseChatMessage = generalAiChatService.DoMultiChat(chatMessage);
        return responseChatMessage;
    }

}
