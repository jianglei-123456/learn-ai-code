package org.jl.learnaicode.sa.flow;

import org.jl.learnaicode.sa.dto.ContentScore;
import org.springframework.ai.chat.client.ChatClient;


public class EvaluatorOptimizerWorkflow {
    private ChatClient client;
    public EvaluatorOptimizerWorkflow(ChatClient client){
        this.client = client;
    }
    private final int maxIterations = 4;

    public String loop(String usetInput){
        String bestContent = "";
        Double bestScore = Double.MIN_VALUE;
        for (int i = 0; i< maxIterations;i++){
            // 1 生成文案
            String content = client.prompt()
                    .system("你是一个作文优化专家。需要优化的原始文本是：" + usetInput +
                            (bestContent.isBlank() ? "" : "\n前一次的优化文本是：" + bestContent))
                    .user("优化作文文案，整体不多于200字").call().content();

            // 2 评估文案
            ContentScore score = client.prompt().system("对作文段落从内容，修辞，情感方面进行打分，评分区间为0-10分，数字越大，作文质量越好。")
                    .user(content).call().entity(ContentScore.class);

            if (score.score() > bestScore){
                bestScore =score.score();
                bestContent = content;
            }
            if (score.score() >= 8.0) break;
        }

        return bestContent + "-- 评分："+ bestScore;
    }
}
