package com.comradegenrr.auto_chinese_fortune.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.comradegenrr.auto_chinese_fortune.config.AiConfig.AiInfo;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.model.ChatCompletionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ChatUtil {

    public static ChatCompletionResponse DoChatRequest(AiInfo aiInfo, String payload) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(aiInfo.getAiUrl()))
                    .header("Authorization", "Bearer " + aiInfo.getAiKey())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
        } catch (URISyntaxException e) {
            throw new FortuneFailedException(e.getMessage());
        }
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new FortuneFailedException(e.getMessage());
        }

        if (response == null || response.body().isEmpty()) {
            throw new FortuneFailedException("Response body is empty");
        }

        ChatCompletionResponse chatCompletionResponse = null;
        try {
            chatCompletionResponse = new Gson().fromJson(response.body(),
                    ChatCompletionResponse.class);
        } catch (JsonSyntaxException e) {
            throw new FortuneFailedException(e.getMessage());
        }
        if (chatCompletionResponse != null && chatCompletionResponse.getChoices() != null) {
            return chatCompletionResponse;
        } else {
            throw new FortuneFailedException("Response body dosen't contain chatCompletionResponse");
        }
    }
}
