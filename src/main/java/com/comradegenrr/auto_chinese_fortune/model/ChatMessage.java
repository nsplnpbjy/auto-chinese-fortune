package com.comradegenrr.auto_chinese_fortune.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    @JsonProperty("MessageList")
    private List<Map<String, String>> messageList;
    @JsonProperty("isSystemd")
    private boolean isSystemed;

    public ChatMessage() {
        this.messageList = new ArrayList<>();
        this.isSystemed = false;
    }

    public static ChatMessage BuildMessage() {
        return new ChatMessage();
    }

    public ChatMessage SetSystemMessage(String systemMessage) {
        if (this.isSystemed) {
            return this;
        }
        Map<String, String> systemMap = Map.of(
                "role", "system",
                "content", systemMessage);
        this.messageList.add(0, systemMap);
        this.isSystemed = true;
        return this;
    }

    public ChatMessage AddUserMessage(String userMessage) {
        Map<String, String> userMap = Map.of(
                "role", "user",
                "content", userMessage);
        this.messageList.add(userMap);
        return this;
    }

    public ChatMessage AddAssistantMessage(String assistantMessage) {
        Map<String, String> assistantMap = Map.of(
                "role", "assistant",
                "content", assistantMessage);
        this.messageList.add(assistantMap);
        return this;
    }
}
