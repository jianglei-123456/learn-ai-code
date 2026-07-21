package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RepairerAgent {
    @UserMessage("""
            你是维修工程师，根据用户需求做出回答。 
            输入：{{massage}}
            """)
    @Agent(outputKey = "response")
    String boss(@V("massage") String massage);
}
