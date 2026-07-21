package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TransWenYanWenAgent {
    @UserMessage("将原文翻译为文言文,{{message}}")
    @Agent(outputKey = "trans")
    String transWenYanWen(@V("message") String message);
}
