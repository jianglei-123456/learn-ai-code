package org.jl.learnaicode.sa.flow;

import org.jl.learnaicode.sa.dto.OrchestratorAnalysis;
import org.jl.learnaicode.sa.dto.PlanningTask;
import org.jl.learnaicode.sa.dto.TravelItinerary;
import org.jl.learnaicode.sa.dto.TravelRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrchestratorWorkersWorkflow {
    private final ChatClient chatClient;

    public OrchestratorWorkersWorkflow(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    public TravelItinerary planTrip(TravelRequest request) {
        // 1. 编排器：分析任务并拆解
        String decomposePrompt = String.format("""
            为前往%s的%d天旅行制定计划，风格：%s。
            请将任务拆解为住宿、景点、餐饮等子任务，并以JSON格式返回。
            """, request.destination(), request.days(), request.style());

        OrchestratorAnalysis analysis = chatClient.prompt()
                .user(decomposePrompt)
                .call()
                .entity(OrchestratorAnalysis.class);

        // 2. 工作者：并行处理每个子任务
        List<CompletableFuture<String>> workerFutures = analysis.tasks().stream()
                .map(task -> CompletableFuture.supplyAsync(() ->
                        executeWorkerTask(request, task) // 每个worker处理一个子任务
                ))
                .toList();

        List<String> workerResults = workerFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 3. 合成器：整合所有结果
        return synthesize(analysis, workerResults);
    }

    // 模拟Worker执行
    private String executeWorkerTask(TravelRequest request, PlanningTask task) {
        String workerPrompt = String.format("作为%s专家，为%s的%d天旅行提供建议。",
                task.type(), request.destination(), request.days());
        return chatClient.prompt(workerPrompt).call().content();
    }

    private TravelItinerary synthesize(OrchestratorAnalysis analysis, List<String> results) {
        // 将所有worker的建议组合成最终行程
        return new TravelItinerary(analysis.strategy(), results);
    }
}
