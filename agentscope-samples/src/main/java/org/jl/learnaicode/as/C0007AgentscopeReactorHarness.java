package org.jl.learnaicode.as;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.extensions.model.openai.OpenAIChatModel;
import org.jl.learnaicode.as.tool.WeatherTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("arh")
public class C0007AgentscopeReactorHarness {

    @GetMapping("easy")
    public String easy(){
        ReActAgent agent = ReActAgent.builder()
                .name("my-agent").sysPrompt("你是一个有帮助的助手。")
                .model(OpenAIChatModel.builder()
                        .baseUrl("https://api.deepseek.com")
                        .modelName("deepseek-v4-flash")
                        .apiKey(System.getenv("spring.ai.deepseek.api-key"))
                        .stream(true).build())
                .build();

        Msg result = agent.call(
                List.of(new UserMessage("你好，介绍一下你自己")),
                RuntimeContext.empty()
        ).block();

        return result.getTextContent();
    }

    @GetMapping("tool")
    public String tool(){
        // 集中注册
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new WeatherTools());

        ReActAgent agent = ReActAgent.builder()
                .name("my-agent").sysPrompt("你是一个天气助手。")
                .model(OpenAIChatModel.builder()
                        .baseUrl("https://api.deepseek.com")
                        .modelName("deepseek-v4-flash")
                        .apiKey(System.getenv("spring.ai.deepseek.api-key"))
                        .stream(true).build())
                .toolkit(toolkit)
                .build();

        Msg result = agent.call(
                List.of(new UserMessage("北京天气怎么样")),
                RuntimeContext.empty()
        ).block();

        return result.getTextContent();
    }
}
