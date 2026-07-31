package org.jl.learnaicode.sa;

import org.jl.learnaicode.sa.tool.DateTimeTools;
import org.jl.learnaicode.sa.tool.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sacc")
public class C0002ChatClientSamples {
    @Autowired
    private ChatClient.Builder chatClientBuilder;

    // 简单的chatclient
    @GetMapping("test")
    public void test(){
        ChatClient client = chatClientBuilder.build();
        String response = client.prompt().user("用以及和介绍什么是 ReActAgent").call().content();
        System.out.printf(response);
    }
    // 工具调用
    @GetMapping("test2")
    public void test2(){
        ChatClient client = chatClientBuilder.build();
        String response = client.prompt().user("明天是星期几").call().content();
        System.out.printf(response);

        ChatClient client2 = chatClientBuilder.build();
        String response2 = client2.prompt().user("明天是星期几")
                .tools(new DateTimeTools()).call().content();
        System.out.printf(response2);

    }
    // 调用多个工具，自动编排
    @GetMapping("test3")
    public void test3(){
        ChatClient client2 = chatClientBuilder.build();
        String response2 = client2.prompt().user("帮我定一个10分钟后的闹钟")
                .tools(new DateTimeTools()).call().content();
        System.out.printf(response2);

    }

    // 练习
    @GetMapping("train")
    public void train(){
        ChatClient client2 = chatClientBuilder.build();
        String response2 = client2.prompt().advisors(new SimpleLoggerAdvisor()).user("今天北京天气怎么样")
                .tools(new WeatherTools()).call().content();
        System.out.println(response2);
        ChatClient client3 = chatClientBuilder.build();
        String response3 = client3.prompt().user("今天北京和上海哪个适合出去玩")
                .tools(new WeatherTools()).call().content();
        System.out.println(response3);

    }
}

