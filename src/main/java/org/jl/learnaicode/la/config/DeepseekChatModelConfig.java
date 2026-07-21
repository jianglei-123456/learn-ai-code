package org.jl.learnaicode.la.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DeepseekChatModelConfig {
    @Value("${spring.ai.deepseek.chat.options.model}")
    private String modelName;

    @Bean
    public OpenAiChatModel create(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(System.getenv("spring.ai.deepseek.api-key")).modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .build();
        return model;
    }
}
