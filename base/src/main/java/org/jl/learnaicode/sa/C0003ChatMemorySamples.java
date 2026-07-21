package org.jl.learnaicode.sa;

import org.jl.learnaicode.sa.service.C0003ChatMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sacm")
public class C0003ChatMemorySamples {


    @Autowired
    private C0003ChatMemoryService chatMemoryService;
    @GetMapping("chat")
    public String chat(String sessionId,String userMassage){
        return chatMemoryService.chat(sessionId,userMassage);
    }
}
