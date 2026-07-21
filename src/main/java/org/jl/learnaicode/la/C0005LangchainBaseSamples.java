package org.jl.learnaicode.la;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.jl.learnaicode.la.service.WeatherAgentAiService;
import org.jl.learnaicode.la.tool.WeatherTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lcbs")
public class C0005LangchainBaseSamples {

    @Value("${spring.ai.deepseek.chat.options.model}")
    private String modelName;

    @GetMapping("chat")
    public String chat(@RequestParam String message,@RequestParam Integer id){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(System.getenv("spring.ai.deepseek.api-key")).modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .build();
        WeatherAgentAiService agentAiService = AiServices.builder(WeatherAgentAiService.class)
                .chatModel(model).chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .tools(new WeatherTools()).build();
        return agentAiService.chat(id,message);
    }
}
