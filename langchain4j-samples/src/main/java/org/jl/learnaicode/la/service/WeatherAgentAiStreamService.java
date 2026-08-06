package org.jl.learnaicode.la.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface WeatherAgentAiStreamService {
    TokenStream chat(@MemoryId int memoryId, @UserMessage String message);
}
