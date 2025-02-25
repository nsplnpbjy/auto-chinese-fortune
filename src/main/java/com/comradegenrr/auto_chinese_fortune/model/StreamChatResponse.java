package com.comradegenrr.auto_chinese_fortune.model;

import lombok.Data;

@Data
public class StreamChatResponse {
    private String reason;
    private String chat;

    public StreamChatResponse() {
        reason = "";
        chat = "";
    }

}
