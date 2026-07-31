package org.jl.learnaicode.la;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.observability.AgentMonitor;
import dev.langchain4j.agentic.observability.HtmlReportGenerator;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.jl.learnaicode.la.agent.*;
import org.jl.learnaicode.la.dto.TravelRequest;
import org.jl.learnaicode.la.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("lcai")
public class C0006LangchainAgentImplmentSamples {

    @Autowired
    private OpenAiChatModel chatModel;

    /**
     * 链式工作流：顺序执行
     * text: 今天是个好日子，天气晴朗，没有下雨，花开遍地
     */
    @GetMapping("chainWorkflow")
    public String chainWorkFlow(String text) {
        return new ChainWorkflow(chatModel).chain(text);
    }

    /**
     * 链式工作流：并行执行
     * text: 公司半年没有发工资
     */
    @GetMapping("parallelizationWorkflow")
    public String parallelizationWorkflow(String text) {
        CompanyBossAgent boss = AgenticServices.agentBuilder(CompanyBossAgent.class).chatModel(chatModel).build();
        CompanyManagerAgent manager = AgenticServices.agentBuilder(CompanyManagerAgent.class).chatModel(chatModel).build();
        CompanyEmploeeAgent emploee = AgenticServices.agentBuilder(CompanyEmploeeAgent.class).chatModel(chatModel).build();
        UntypedAgent agent = AgenticServices.parallelBuilder().subAgents(boss, manager, emploee)
                .executor(Executors.newFixedThreadPool(3))
                .outputKey("company")
                .output(agenticScope -> {
                    String boss1 = agenticScope.readState("boss", new String());
                    String emploee1 = agenticScope.readState("emploee", new String());
                    String manager1 = agenticScope.readState("manager", new String());
                    return boss1 + manager1 + emploee1;
                }).build();
        Map<String, Object> param = new HashMap<>();
        param.put("massage", text);
        String invoke = (String) agent.invoke(param);
        return invoke;
    }

    /**
     * 路由工作流：让大模型进行输入分发
     * text: 我要购买一个手机
     */
    @GetMapping("routingWorkflow")
    public String routingWorkflow(String text) {
        RouterAgent router = AgenticServices.agentBuilder(RouterAgent.class).chatModel(chatModel).build();
        SalesmanAgent salesman = AgenticServices.agentBuilder(SalesmanAgent.class).chatModel(chatModel).build();
        RepairerAgent repairer = AgenticServices.agentBuilder(RepairerAgent.class).chatModel(chatModel).build();
        // 条件编排：根据 category 的值选择
        UntypedAgent experts = AgenticServices.conditionalBuilder()
                .subAgents(
                        scope -> scope.readState("category", "未知").equals( "订单"), salesman
                ).subAgents(scope -> scope.readState("category", "未知").equals( "技术服务"), repairer)
                .build();

        AgentMonitor monitor = new AgentMonitor();
        // 把路由 + 分发串联起来
        UntypedAgent fullAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(router, experts)
                .listener(monitor)
                .outputKey("response")
                .build();
        String invoke = (String) fullAgent.invoke(Map.of("massage", text));
        HtmlReportGenerator.generateReport(monitor, Path.of("report.html"));
        return invoke;
    }

    /**
     * 优化器评估器工作流：循环进行优化直到评估质量达到目标
     * text: 今日是个好天气，出门吃了一个雪糕，看了看天空，心情非常好。
     */
    @GetMapping("evalWorkflow")
    public String evalWorkflow(String text) {
        TextAgent write = AgenticServices.agentBuilder(TextAgent.class).chatModel(chatModel).build();
        TextScoreAgent score = AgenticServices.agentBuilder(TextScoreAgent.class).chatModel(chatModel).build();
        // 循环：每轮先评分再编辑，分数 ≥ 0.8 时停止
        UntypedAgent reviewLoop = AgenticServices.loopBuilder()
                .subAgents(score, write)
                .maxIterations(5)
                .exitCondition(scope -> scope.readState("score", 0.0) >= 8.0    )
                .build();

        // Loop 本身也是 Agent，可以嵌入 Sequential
        UntypedAgent finalPipeline = AgenticServices.sequenceBuilder()
                .subAgents(write, reviewLoop)   // writer 写初稿 → reviewLoop 迭代优化
                .outputKey("content")
                .build();
        return (String) finalPipeline.invoke(Map.of("content",text));
    }
}
