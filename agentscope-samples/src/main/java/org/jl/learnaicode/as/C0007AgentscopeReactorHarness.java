package org.jl.learnaicode.as;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.extensions.model.openai.OpenAIChatModel;
import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.memory.compaction.CompactionConfig;
import org.jl.learnaicode.as.tool.WeatherTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
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

    private HarnessAgent harnessAgent;
    {
        // 集中注册
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new WeatherTools());
        harnessAgent = HarnessAgent.builder()
                .name("weather")
                .model(OpenAIChatModel.builder()
                        .baseUrl("https://api.deepseek.com")
                        .modelName("deepseek-v4-flash")
                        .apiKey(System.getenv("spring.ai.deepseek.api-key"))
                        .stream(true).build())
                .workspace(Paths.get(".agentscope/workspace"))    // ← 工作区目录
                .compaction(CompactionConfig.builder()             // ← 对话压缩
                        .triggerMessages(30)                           // 超过 30 条消息触发压缩
                        .keepMessages(10)                              // 压缩后保留最近 10 条
                        .build())
                .toolkit(toolkit)
                .build();
    }

    @GetMapping("harness")
    public String harness(@RequestParam("msg") String msg,@RequestParam("user") String user
            ,@RequestParam(name = "sessionId",required = false,defaultValue = "demo") String sessionId){
        RuntimeContext ctx = RuntimeContext.builder()
                .sessionId(sessionId)
                .userId(user)
                .build();

        return harnessAgent.call(new UserMessage(msg), ctx).block().getTextContent();
    }


}
