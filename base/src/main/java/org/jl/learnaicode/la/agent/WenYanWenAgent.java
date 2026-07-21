package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface WenYanWenAgent {
    @Agent(outputKey = "trans")
    String transWenYanWen(@V("message") String message);
}
