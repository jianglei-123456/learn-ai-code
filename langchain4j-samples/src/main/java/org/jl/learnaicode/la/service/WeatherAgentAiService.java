package org.jl.learnaicode.la.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface WeatherAgentAiService {
    String chat(@MemoryId int memoryId, @UserMessage String message);
}
