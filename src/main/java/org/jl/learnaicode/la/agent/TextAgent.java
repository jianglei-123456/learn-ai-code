package org.jl.learnaicode.la.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TextAgent {
    @UserMessage("""
            "你是一个作文优化专家，根据原始文本给出一份优化后的文本。需要优化的文本是：{{content}}
            """)
    @Agent(outputKey = "content")
    String write(@V("content") String content);
}
