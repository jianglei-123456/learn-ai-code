package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface JiazhuWenYanWenAgent {
    @UserMessage("保留文言文原本,给文言文内容加注,{{trans}}")
    @Agent(outputKey = "trans")
    String jiazhuWenYanWen(@V("trans") String trans);
}
