package org.jl.learnaicode.la.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.jl.learnaicode.la.dto.ContentScore;

import java.util.List;

public class EvaluatorOptimizerWorkflow {
    private final OpenAiChatModel model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EvaluatorOptimizerWorkflow(OpenAiChatModel model) {
        this.model = model;
    }

    private final int maxIterations = 4;

    public String loop(String userInput) {
        String bestContent = "";
        double bestScore = Double.MIN_VALUE;
        for (int i = 0; i < maxIterations; i++) {
            // 1 生成文案
            String content = model.chat(List.of(
                    SystemMessage.from("你是一个作文优化专家。需要优化的原始文本是：" + userInput +
                            (bestContent.isBlank() ? "" : "\n前一次的优化文本是：" + bestContent)),
                    UserMessage.from("优化作文文案，整体不多于200字")
            )).aiMessage().text();

            // 2 评估文案
            String scoreJson = model.chat(List.of(
                    SystemMessage.from("对作文段落从内容，修辞，情感方面进行打分，评分区间为0-10分，数字越大，作文质量越好。返回JSON格式：{\"score\": 数字}"),
                    UserMessage.from(content)
            )).aiMessage().text();
            ContentScore score = parseScore(scoreJson);

            if (score.score() > bestScore) {
                bestScore = score.score();
                bestContent = content;
            }
            if (score.score() >= 8.0) break;
        }

        return bestContent + "-- 评分：" + bestScore;
    }

    private ContentScore parseScore(String json) {
        try {
            // 清理可能的 markdown 代码块包装
            String cleaned = json.replaceAll("(?s)```json\\s*", "").replaceAll("(?s)```\\s*", "").trim();
            return objectMapper.readValue(cleaned, ContentScore.class);
        } catch (Exception e) {
            return new ContentScore(Double.MIN_VALUE);
        }
    }
}
