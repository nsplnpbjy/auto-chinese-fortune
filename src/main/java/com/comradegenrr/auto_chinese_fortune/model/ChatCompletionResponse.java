package com.comradegenrr.auto_chinese_fortune.model;

import java.util.List;

import lombok.Data;

@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;

        @Data
        public static class Message {
            private String role;
            private String content;
            private String reasoning_content;

            // Getters and Setters
        }

        // Getters and Setters
    }

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
        private PromptTokensDetails prompt_tokens_details;
        private CompletionTokensDetails completion_tokens_details;
        private int prompt_cache_hit_tokens;
        private int prompt_cache_miss_tokens;

        public static class PromptTokensDetails {
            private int cached_tokens;

            // Getters and Setters
        }

        public static class CompletionTokensDetails {
            private int reasoning_tokens;

            // Getters and Setters
        }

        // Getters and Setters
    }

    // Getters and Setters
}
