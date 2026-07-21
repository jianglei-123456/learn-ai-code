package org.jl.learnaicode.sa.dto;

import java.util.List;

public record OrchestratorAnalysis(String strategy, List<PlanningTask> tasks) {
}
