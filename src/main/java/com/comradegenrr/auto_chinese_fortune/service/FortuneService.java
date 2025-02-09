package com.comradegenrr.auto_chinese_fortune.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comradegenrr.auto_chinese_fortune.config.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.config.AiConfig.AiInfo;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneRequest;
import com.comradegenrr.auto_chinese_fortune.dto.FortuneResponse;
import com.comradegenrr.auto_chinese_fortune.model.ChatCompletionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FortuneService {

        @Autowired
        private AiInfo aiInfo;

        public FortuneResponse DoFortune(FortuneRequest fortuneRequest)
                        throws URISyntaxException, IOException, InterruptedException, FortuneFailedException {

                // 仅实现了对话算命，还差卦象等功能
                String systemMessage = aiInfo.getSystemMessage();
                String userMessage = fortuneRequest.getAsking();
                ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>();

                Map<String, String> systemMap = Map.of(
                                "role", "system",
                                "content", systemMessage);
                Map<String, String> userMap = Map.of(
                                "role", "user",
                                "content", userMessage);

                messages.add(0, systemMap);
                messages.add(1, userMap);

                Map<String, Object> payload = Map.of(
                                "model", aiInfo.getModel(),
                                "stream", false,
                                "temperature", aiInfo.getTemperture(),
                                "messages", messages);

                String jsonPayload = new Gson().toJson(payload);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(aiInfo.getAiUrl()))
                                .header("Authorization", "Bearer " + aiInfo.getAiKey())
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                                .build();

                java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                java.net.http.HttpResponse<String> response = null;
                try {
                        response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException e) {
                        log.info("Fortune failed,user:{}", fortuneRequest.getUsername());
                        throw new FortuneFailedException(e.getMessage());
                }

                if (response == null || response.body().isEmpty()) {
                        log.info("Response body is empty,user:{}", fortuneRequest.getUsername());
                        throw new FortuneFailedException("Response body is empty");
                }

                ChatCompletionResponse chatCompletionResponse = null;
                try {
                        chatCompletionResponse = new Gson().fromJson(response.body(),
                                        ChatCompletionResponse.class);
                } catch (JsonSyntaxException e) {
                        log.info("Response body is wrong,user:{}", fortuneRequest.getUsername());
                        throw new FortuneFailedException(e.getMessage());
                }

                if (chatCompletionResponse != null && !chatCompletionResponse.getChoices().isEmpty()) {
                        FortuneResponse fortuneResponse = new FortuneResponse();
                        fortuneResponse.setFortune(
                                        chatCompletionResponse.getChoices().get(0).getMessage().getContent());

                        // 思考原因，如果没有这个功能的ai可以注释掉
                        fortuneResponse.setReason(
                                        chatCompletionResponse.getChoices().get(0).getMessage().getReasoning_content());

                        fortuneResponse.setSuccess(true);
                        log.info("Fortune success,user:{}", fortuneRequest.getUsername());
                        return fortuneResponse;
                } else {
                        log.info("Response body is empty,user:" + fortuneRequest.getUsername());
                        throw new FortuneFailedException("Response body is empty");
                }
        }

}
