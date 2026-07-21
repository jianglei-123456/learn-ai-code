package org.jl.learnaicode.sa.flow;

import org.springframework.ai.chat.client.ChatClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;


public class ParallelizationWorkflow {
    private ChatClient client;
    public ParallelizationWorkflow(ChatClient client){
        this.client = client;
    }
    private final int maxConcurrency = 3;

    public List<String> parallel(String basePrompt, List<String> items){
        var executor = Executors.newFixedThreadPool(maxConcurrency);
        var futures = items.stream()
                .map(item -> CompletableFuture.supplyAsync(() ->
                        client.prompt(basePrompt + item).call().content(), executor
                ))
                .toList();
        return futures.stream().map(CompletableFuture::join).toList();
    }
}
