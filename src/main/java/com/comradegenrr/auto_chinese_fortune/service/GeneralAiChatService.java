package com.comradegenrr.auto_chinese_fortune.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comradegenrr.auto_chinese_fortune.config.FortuneFailedException;
import com.comradegenrr.auto_chinese_fortune.config.AiConfig.AiInfo;
import com.comradegenrr.auto_chinese_fortune.model.ChatCompletionResponse;
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.model.ChatPayload;
import com.comradegenrr.auto_chinese_fortune.util.ChatUtil;

@Service
public class GeneralAiChatService {

    @Autowired
    private AiInfo aiInfo;

    public ChatMessage DoMultiChat(ChatMessage chatMessage) {
        if (chatMessage == null) {
            throw new FortuneFailedException("message is null");
        }
        if (!chatMessage.isSystemed()) {
            chatMessage.SetSystemMessage(aiInfo.getSystemMessage());
        }
        ChatCompletionResponse chatCompletionResponse = ChatUtil.DoChatRequest(aiInfo,
                ChatPayload.BuildChatPayload().SetAiModel(aiInfo.getModel())
                        .SetTemperature(aiInfo.getTemperture())
                        .IsStream(false)
                        .SetChatMessage(chatMessage.getMessageList()).BuildPayloadString());
        chatMessage.AddAssistantMessage(chatCompletionResponse.getChoices().get(0).getMessage().getContent());
        return chatMessage;
    }
}
