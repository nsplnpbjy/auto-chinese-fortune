package com.comradegenrr.auto_chinese_fortune.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.comradegenrr.auto_chinese_fortune.config.AiConfig.AiInfo;
import com.comradegenrr.auto_chinese_fortune.config.Exceptions.ChatFailedException;
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.model.ChatPayload;
import com.comradegenrr.auto_chinese_fortune.model.StreamChatResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StreamChatService {

    @Autowired
    private AiInfo aiInfo;

    public void DoChat(SseEmitter emitter, HttpServletRequest request, ChatMessage chatMessage)
            throws URISyntaxException, ChatFailedException {
        Map<String, String> lastMessage = chatMessage.getMessageList().getLast();
        if (!lastMessage.get("role").equals("user")) {
            throw new ChatFailedException("The last message must be from user");
        }

        String jsonPayload = ChatPayload.BuildChatPayload().SetAiModel(aiInfo.getModel())
                .IsStream(true)
                .SetTemperature(aiInfo.getTemperture())
                .SetChatMessage(chatMessage.getMessageList())
                .BuildPayloadString();

        HttpRequest requestForSend = HttpRequest.newBuilder().timeout(Duration.ofMinutes(10))
                .uri(new URI(aiInfo.getAiUrl()))
                .header("Authorization", "Bearer " + aiInfo.getAiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpResponse<InputStream> response = null;
        String assistantReplyString = "";
        try {
            response = client.send(requestForSend, java.net.http.HttpResponse.BodyHandlers.ofInputStream());
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(response.body()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.equals("keep-alive") || line.equals(": keep-alive")
                        || line.equals("[DONE]") || line.equals("[\"DONE\"]") || line.equals("data: [DONE]"))
                    continue;

                StreamChatResponse output = parseWithGson(line);
                if (!output.getChat().isBlank() || !output.getReason().isBlank()) {
                    emitter.send(new Gson().toJson(output, StreamChatResponse.class));
                    assistantReplyString = assistantReplyString + output.getChat();
                }
            }
            chatMessage.AddAssistantMessage(assistantReplyString);
            emitter.send(chatMessage);
            reader.close();
        } catch (InterruptedException e) {
            throw new ChatFailedException("AI服务连接失败");
        } catch (Exception e) {
            throw new ChatFailedException(e.getMessage());
        }
        client.close();
    }

    private StreamChatResponse parseWithGson(String jsonLine) {
        try {
            // 1. 清洗SSE协议前缀
            if (jsonLine.startsWith("data: ")) {
                jsonLine = jsonLine.substring(6).trim();
            }

            // 2. 跳过心跳包和非JSON数据
            if (jsonLine.isEmpty() || jsonLine.equals("keep-alive"))
                return new StreamChatResponse();

            // 3. 安全解析
            JsonElement element = JsonParser.parseString(jsonLine);
            JsonObject root = element.getAsJsonObject();

            // 4. 结构化校验
            if (!root.has("choices") ||
                    !root.get("choices").isJsonArray() ||
                    root.getAsJsonArray("choices").isEmpty()) {
                return new StreamChatResponse();
            }

            JsonObject choice = root.getAsJsonArray("choices").get(0).getAsJsonObject();
            JsonObject delta = new JsonObject();
            if (choice.has("delta") && choice.get("delta").isJsonObject()) {
                delta = choice.getAsJsonObject("delta");
            }

            // 5. 空安全取值
            String content = delta.has("content") && !delta.get("content").isJsonNull()
                    ? delta.get("content").getAsString()
                    : "";
            String reasoning = delta.has("reasoning_content") && !delta.get("reasoning_content").isJsonNull()
                    ? delta.get("reasoning_content").getAsString()
                    : "";

            StreamChatResponse streamChatResponse = new StreamChatResponse();
            if (!content.isEmpty()) {
                streamChatResponse.setChat(content);
            } else {
                streamChatResponse.setReason(reasoning);
            }
            return streamChatResponse;

        } catch (JsonSyntaxException e) {
            throw new ChatFailedException("json序列化错误");
        } catch (IllegalStateException e) {
            throw new ChatFailedException("AI服务器返回错误");
        } catch (Exception e) {
            log.error("未捕获异常: " + e.getClass().getSimpleName());
        }
        return new StreamChatResponse();
    }

}
