package com.comradegenrr.auto_chinese_fortune.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.comradegenrr.auto_chinese_fortune.config.Exceptions.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneRequest;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.service.FortuneService;
import com.comradegenrr.auto_chinese_fortune.service.GeneralAiChatService;
import com.comradegenrr.auto_chinese_fortune.service.StreamChatService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
@Slf4j
public class AiChatController {

    @Autowired
    private FortuneService fortuneService;
    @Autowired
    private GeneralAiChatService generalAiChatService;

    @Autowired
    private StreamChatService streamChatService;

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
    public ChatMessage chat(HttpServletRequest request, @RequestBody ChatMessage chatMessage) {
        ChatMessage responseChatMessage = generalAiChatService.DoMultiChat(chatMessage);
        return responseChatMessage;
    }

    @PostMapping("/stream")
    public ResponseEntity<SseEmitter> stream(HttpServletRequest request, @RequestBody ChatMessage chatMessage) {
        SecurityContext context = SecurityContextHolder.getContext();
        SseEmitter emitter = new SseEmitter(600_000L); // 10分钟超时
        CompletableFuture.runAsync(() -> {
            SecurityContextHolder.setContext(context); // 设置到异步线程[7](@ref)
            try {
                streamChatService.DoChat(emitter, request, chatMessage);
            } catch (URISyntaxException e) {
                throw new FortuneFailedException(e.getMessage());
            } catch (AuthenticationException e) {
                throw new FortuneFailedException(e.getMessage());
            }
        });
        return new ResponseEntity<SseEmitter>(emitter, HttpStatus.OK);
    }

}
