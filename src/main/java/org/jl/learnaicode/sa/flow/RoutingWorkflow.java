package org.jl.learnaicode.sa.flow;

import org.springframework.ai.chat.client.ChatClient;

import java.util.Map;


public class RoutingWorkflow {
    private ChatClient client;
    public RoutingWorkflow(ChatClient client){
        this.client = client;
    }
    private final Map<String,String> routes = Map.of(
            "订单","你是一个订单处理员",
            "技术服务","你是一个技术服务工程师",
            "通用","你是一个产品客服人员"
    );

    public String routing(String input){
        // 1. 让LLM分类
        String category = client.prompt()
                .user("""
                        根据输入进行分类，从以下分类中选择一个：订单，技术服务，通用。
                        只需要返回分类名词。
                        输入：%s
                        """.formatted(input))
                .call().content().trim().toLowerCase();

        // 2. 分发嗲用不同的提示词
        String route = routes.getOrDefault(category, "通用");
        return client.prompt().system(route).user(input).call().content();
    }
}
