package org.jl.learnaicode.la.dto;

import java.time.LocalDate;
import java.util.List;

public class TravelItinerary {
    private String strategy;
    private List<String> suggestions;
    private String summary;
    private LocalDate startDate;

    public TravelItinerary() {}

    public TravelItinerary(String strategy, List<String> suggestions) {
        this.strategy = strategy;
        this.suggestions = suggestions;
        this.summary = generateSummary(strategy, suggestions);
    }

    private String generateSummary(String strategy, List<String> suggestions) {
        StringBuilder sb = new StringBuilder();
        sb.append("【旅行方案概览】\n");
        sb.append("策略：").append(strategy).append("\n");
        sb.append("详细建议：\n");
        for (int i = 0; i < suggestions.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(suggestions.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        this.summary = generateSummary(this.strategy, suggestions);
    }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    @Override
    public String toString() {
        return summary;
    }
}
