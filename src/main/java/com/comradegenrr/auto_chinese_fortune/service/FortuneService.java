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

@Service
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

                HttpResponse<String> response = java.net.http.HttpClient.newHttpClient().send(request,
                                HttpResponse.BodyHandlers.ofString());
                if (response.body().isEmpty()) {
                        throw new FortuneFailedException("Response body is empty");
                }
                ChatCompletionResponse chatCompletionResponse = null;
                try {
                        chatCompletionResponse = new Gson().fromJson(response.body(),
                                        ChatCompletionResponse.class);
                } catch (JsonSyntaxException e) {

                        throw new FortuneFailedException(e.getMessage());
                }

                if (!chatCompletionResponse.getChoices().isEmpty()) {
                        FortuneResponse fortuneResponse = new FortuneResponse();
                        fortuneResponse.setFortune(
                                        chatCompletionResponse.getChoices().get(0).getMessage().getContent());

                        // 思考原因，如果没有这个功能的ai可以注释掉
                        fortuneResponse.setReason(
                                        chatCompletionResponse.getChoices().get(0).getMessage().getReasoning_content());

                        fortuneResponse.setSuccess(true);
                        return fortuneResponse;
                } else {
                        FortuneResponse fortuneResponse = new FortuneResponse();
                        fortuneResponse.setFortune("I'm sorry, I don't know what to say.");
                        fortuneResponse.setReason("I'm sorry, I don't know what to say.");
                        fortuneResponse.setSuccess(false);
                        return fortuneResponse;
                }
        }

}
