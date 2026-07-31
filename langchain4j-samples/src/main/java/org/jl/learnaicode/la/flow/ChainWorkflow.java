package org.jl.learnaicode.la.flow;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.jl.learnaicode.la.agent.JiazhuWenYanWenAgent;
import org.jl.learnaicode.la.agent.TransWenYanWenAgent;
import org.jl.learnaicode.la.agent.WenYanWenAgent;
import org.jl.learnaicode.la.agent.YouHuaWenYanWenAgent;

public class ChainWorkflow {
    private final OpenAiChatModel model;
    private TransWenYanWenAgent transWenYanWenAgent;
    private YouHuaWenYanWenAgent youHuaWenYanWenAgent;
    private JiazhuWenYanWenAgent jiazhuWenYanWenAgent;

    public ChainWorkflow(OpenAiChatModel model) {
        this.model = model;
        transWenYanWenAgent = AgenticServices.agentBuilder(TransWenYanWenAgent.class)
                .chatModel(this.model).build();
        youHuaWenYanWenAgent = AgenticServices.agentBuilder(YouHuaWenYanWenAgent.class)
                .chatModel(this.model).build();
        jiazhuWenYanWenAgent = AgenticServices.agentBuilder(JiazhuWenYanWenAgent.class)
                .chatModel(this.model).build();
    }
    public String chain(String userInput) {
        WenYanWenAgent trans = AgenticServices.sequenceBuilder(WenYanWenAgent.class).subAgents(
                transWenYanWenAgent, youHuaWenYanWenAgent, jiazhuWenYanWenAgent
        ).build();

        return trans.transWenYanWen(userInput);
    }
}
