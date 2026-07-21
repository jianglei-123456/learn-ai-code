package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RouterAgent {
    @UserMessage("""
            根据输入进行分类，从以下分类中选择一个：订单，技术服务。 
            只需要返回分类名词。 
            输入：{{massage}}
            """)
    @Agent(outputKey = "category")
    String boss(@V("massage") String massage);
}
