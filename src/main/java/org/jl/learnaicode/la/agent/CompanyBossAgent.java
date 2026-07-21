package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CompanyBossAgent {
    @UserMessage("你是公司老板，分析发生在公司的这件事对你的影响,100字以内,{{massage}}")
    @Agent(outputKey = "boss")
    String boss(@V("massage") String massage);
}
