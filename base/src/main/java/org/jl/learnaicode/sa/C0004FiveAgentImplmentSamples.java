package org.jl.learnaicode.sa;

import org.jl.learnaicode.sa.dto.TravelRequest;
import org.jl.learnaicode.sa.flow.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("saai")
public class C0004FiveAgentImplmentSamples {

    @Autowired
    private ChatClient.Builder builder;
    @Autowired
    private OrchestratorWorkersWorkflow orchestratorWorkersWorkflow;


    /**
     * 链式工作流：顺序执行
     * text: 今天是个好日子，天气晴朗，没有下雨，花开遍地
     */
    @GetMapping("chainWorkflow")
    public String chainWorkFlow(String text){

        return new ChainWorkflow(builder.defaultAdvisors(new SimpleLoggerAdvisor()).build()).chain(text);
    }

    /**
     * 链式工作流：并行执行
     * text: 公司半年没有发工资
     */
    @GetMapping("parallelizationWorkflow")
    public String parallelizationWorkflow(String text){
        String basePrompt = "分析这件事对公司不同人的影响："+text;
        List<String> items = List.of("普通员工","中层管理","公司老板");
        List<String> parallel = new ParallelizationWorkflow(builder.defaultAdvisors(new SimpleLoggerAdvisor()).build())
                .parallel(basePrompt, items);
        return parallel.toString();
    }

    /**
     * 路由工作流：让大模型进行输入分发
     * text: 我要购买一个手机
     */
    @GetMapping("routingWorkflow")
    public String routingWorkflow(String text){

       return new RoutingWorkflow(builder.defaultAdvisors(new SimpleLoggerAdvisor()).build()).routing(text);
    }
    /**
     * 指挥者-工作者工作流：分解任务，并行执行，合并结果
     * destination=北京&days=2&style=美食
     */
    @GetMapping("orchestratorWorkflow")
    public String orchestratorWorkflow(TravelRequest travelRequest){

        return orchestratorWorkersWorkflow.planTrip(travelRequest).toString();
    }
    /**
     * 优化器评估器工作流：循环进行优化直到评估质量达到目标
     * text: 今日是个好天气，出门吃了一个雪糕，看了看天空，心情非常好。
     */
    @GetMapping("evalWorkflow")
    public String evalWorkflow(String text){

        return new EvaluatorOptimizerWorkflow(builder.defaultAdvisors(new SimpleLoggerAdvisor()).build()).loop(text);
    }
}