package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface YouHuaWenYanWenAgent {
    @UserMessage("将以下文言文润色,{{trans}}")
    @Agent(outputKey = "trans")
    String youhuaWenYanWen(@V("trans") String trans);
}
