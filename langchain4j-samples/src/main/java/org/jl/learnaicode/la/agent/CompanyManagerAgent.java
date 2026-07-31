package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CompanyManagerAgent {
    @UserMessage("你是公司管理层，分析发生在公司的这件事对你的影响,100字以内,{{massage}}")
    @Agent(outputKey = "manager")
    String manager(@V("massage") String massage);
}
