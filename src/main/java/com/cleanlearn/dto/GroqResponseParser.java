package com.cleanlearn.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

public class GroqResponseParser {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StudyPlanResponse(
        TodayFocus todayFocus,
        List<RecommendedProblem> recommendedProblems,
        SequenceReasoning sequenceReasoning,
        AvoidToday avoidToday,
        List<String> mentorObservations
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TodayFocus(
        String primaryGoal,
        String secondaryGoal,
        int estimatedTotalTimeMinutes,
        String strategySummary
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RecommendedProblem(
        String id,
        String problemName,
        String difficulty,
        List<String> topic,
        String whyThisNow,
        String patternBeingTrained,
        String techRelevance,
        String interviewSignal,
        String expectedLearningOutcome,
        int priorityOrder
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SequenceReasoning(
        List<String> whyOnlyThese,
        List<String> patternsReinforcedToday,
        List<String> weaknessesTargeted,
        String fatigueManagementReasoning,
        String difficultyProgressionReasoning
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AvoidToday(
        List<String> topicsToAvoid,
        String reason
    ) {}
}