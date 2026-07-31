package org.jl.learnaicode.sa.service;

import org.jl.learnaicode.sa.tool.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class C0003ChatMemoryService {

    private ChatClient chatClient;
    private ChatMemory chatMemory;

    C0003ChatMemoryService(ChatClient.Builder builder, ChatMemory chatMemory){
        this.chatMemory = chatMemory;
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                new SimpleLoggerAdvisor()
        ).build();
    }

    public String chat(String sessionId,String userMassage){
        return chatClient.prompt().user(userMassage).tools(new WeatherTools())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID,sessionId))
                .call().content();
    }
}
