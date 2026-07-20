package org.jl.learnaicode.sa.flow;

import org.springframework.ai.chat.client.ChatClient;


public class ChainWorkflow {
    private ChatClient client;
    public ChainWorkflow(ChatClient client){
        this.client = client;
    }
    private String[] systemPrompts = {
            "将以下白话文翻译为文言文：",
            "将以下文言文润色：",
            "给文言文加注，返回原文及注释："
    };

    public String chain(String usetInput){
        String response = usetInput;
        for (String prompt : systemPrompts){
            String input = String.format("{%s}\n {%s}",prompt,response);
            response = client.prompt(input).call().content();
        }
        return response;
    }
}
