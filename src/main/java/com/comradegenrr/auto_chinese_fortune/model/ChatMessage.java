package com.comradegenrr.auto_chinese_fortune.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private List<Map<String, String>> MessageList;
    private boolean isSystemed;

    public ChatMessage() {
        MessageList = new ArrayList<Map<String, String>>();
        isSystemed = false;
    }

    public static ChatMessage BuildMessage() {
        return new ChatMessage();
    }

    public ChatMessage SetSystemMessage(String systemMessage) {
        if (isSystemed) {
            return this;
        }
        Map<String, String> systemMap = Map.of(
                "role", "system",
                "content", systemMessage);
        MessageList.add(0, systemMap);
        isSystemed = true;
        return this;
    }

    public ChatMessage AddUserMessage(String userMessage) {
        Map<String, String> userMap = Map.of(
                "role", "user",
                "content", userMessage);
        MessageList.add(userMap);
        return this;
    }

    public ChatMessage AddAssistantMessage(String assistantMessage) {
        Map<String, String> assistanMessage = Map.of(
                "role", "assistant",
                "content", assistantMessage);
        MessageList.add(assistanMessage);
        return this;
    }

}
