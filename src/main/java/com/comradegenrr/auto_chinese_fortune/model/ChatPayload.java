package com.comradegenrr.auto_chinese_fortune.model;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatPayload {

    private Map<String, Object> payload;

    public ChatPayload() {
        payload = Map.of();
    }

    public static ChatPayload BuildChatPayload() {
        return new ChatPayload();
    }

    public ChatPayload SetAiModel(String model) {
        payload.put("model", model);
        return this;
    }

    public ChatPayload IsStream(boolean isStream) {
        payload.put("stream", isStream);
        return this;
    }

    public ChatPayload SetTemperature(Double temperature) {
        payload.put("temperature", temperature);
        return this;
    }

    public ChatPayload SetChatMessage(List<Map<String, String>> chatMessage) {
        payload.put("messages", chatMessage);
        return this;
    }

    public String BuildPayloadString() {
        String jsonPayload = new Gson().toJson(payload);
        return jsonPayload;
    }
}
