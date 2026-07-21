package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TextScoreAgent {
    @UserMessage("""
            "你是一个作文评审专家专家。需要评审的作文是：{{content}}
             对作文进行评分，取值范围是[0,10]区间的double类型的数字。
            """)
    @Agent(outputKey = "score")
    Double score(@V("content") String content);
}
