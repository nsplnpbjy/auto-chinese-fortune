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
import com.comradegenrr.auto_chinese_fortune.model.ChatMessage;
import com.comradegenrr.auto_chinese_fortune.model.ChatPayload;
import com.comradegenrr.auto_chinese_fortune.util.ChatUtil;
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
                ChatMessage chatMessage = ChatMessage.BuildMessage()
                                .SetSystemMessage(aiInfo.getSystemMessage())
                                .AddUserMessage(fortuneRequest.getAsking());

                String jsonPayload = ChatPayload.BuildChatPayload().SetAiModel(aiInfo.getModel())
                                .IsStream(false)
                                .SetTemperature(aiInfo.getTemperture())
                                .SetChatMessage(chatMessage.getMessageList())
                                .BuildPayloadString();

                ChatCompletionResponse chatCompletionResponse = ChatUtil.DoChatRequest(aiInfo, jsonPayload);

                FortuneResponse fortuneResponse = new FortuneResponse();
                fortuneResponse.setFortune(
                                chatCompletionResponse.getChoices().get(0).getMessage().getContent());

                // 思考原因，如果没有这个功能的ai可以注释掉
                fortuneResponse.setReason(
                                chatCompletionResponse.getChoices().get(0).getMessage().getReasoning_content());

                fortuneResponse.setSuccess(true);
                log.info("Fortune success,user:{}", fortuneRequest.getUsername());
                return fortuneResponse;

        }

}
