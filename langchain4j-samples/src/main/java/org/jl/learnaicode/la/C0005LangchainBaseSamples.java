package org.jl.learnaicode.la;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.jl.learnaicode.la.service.WeatherAgentAiService;
import org.jl.learnaicode.la.service.WeatherAgentAiStreamService;
import org.jl.learnaicode.la.tool.WeatherTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.CompletableFuture;

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


    @GetMapping(value = "chatStreaming",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreaming(@RequestParam String message, @RequestParam Integer id){
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(System.getenv("spring.ai.deepseek.api-key")).modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .build();
        WeatherAgentAiStreamService agentAiService = AiServices.builder(WeatherAgentAiStreamService.class)
                .streamingChatModel(model).chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .tools(new WeatherTools()).build();
        // 使用 Sinks 创建可编程的 Flux
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        // 异步调用 AI Service
        CompletableFuture.runAsync(() -> {
            agentAiService.chat(id,message)
                    .onPartialResponse(sink::tryEmitNext) // 每个 token 推入 Flux
                    .onCompleteResponse(response -> sink.tryEmitComplete()) // 完成
                    .onError(error -> sink.tryEmitError(error)) // 错误
                    .start();
        });
        return sink.asFlux();
    }
}
