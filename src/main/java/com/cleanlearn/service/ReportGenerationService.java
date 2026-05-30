package com.cleanlearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.cleanlearn.dto.GroqResponseParser.*;
import com.cleanlearn.service.EmailService;
import com.cleanlearn.service.UserService;
import com.cleanlearn.entity.User;
import com.cleanlearn.service.UserItemService;
import lombok.extern.slf4j.Slf4j;
import com.cleanlearn.util.EmailContent;
import com.cleanlearn.service.external.GroqService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReportGenerationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserItemService userItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroqService groqService;

    public void draftAndSendEmailReport(Long userId) throws Exception {
        
        Boolean anyDeadlineMissedProblems = userItemService.anyDeadlineMissedProblems(userId);

        if(anyDeadlineMissedProblems) {
            userItemService.updateDatesForUserItems(userId);
            sendDeadlineMissedAlert(userId);
        }else{
            StudyPlanResponse studyPlan = groqService.askGroq(userId);
            generateAndSendEmailReport(studyPlan, userId);
        }

    }

    private void sendDeadlineMissedAlert(Long userId) throws Exception {
        User user = userService.validateUserById(userId);
        String to = user.getEmail();
        String subject = "⚠️ Action Required: Missed Deadlines in Your Study Plan";
        String htmlContent = EmailContent.DEADLINE_MISSED_EMAIL_TEMPLATE;
        emailService.sendEmail(to, subject, htmlContent);
    }

    public void generateAndSendEmailReport(StudyPlanResponse plan, Long userId) throws Exception {

        updateFaDateForGroqProblems(plan.recommendedProblems(), userId);

        String subject = "Your Daily Study Plan - " + java.time.LocalDate.now();

        User user = userService.validateUserById(userId);
        String to = user.getEmail();

        List<String> revisionProblems = userItemService.getPendingRevisionsForToday(userId);
    
        String htmlContent = generateEmailContent(plan, revisionProblems);
        
        emailService.sendEmail(to, subject, htmlContent);
    }

    private void updateFaDateForGroqProblems(List<RecommendedProblem> recommendedProblems, Long userId) {
        for (RecommendedProblem prob : recommendedProblems) {
            userItemService.updateFaDateForProblem(userId, prob.id());
        }
    }

    public String generateEmailContent(StudyPlanResponse plan, List<String> revisionProblems) {
        return EmailContent.STUDY_PLAN_EMAIL_TEMPLATE.formatted(
                plan.todayFocus().estimatedTotalTimeMinutes(),
                plan.todayFocus().primaryGoal(),
                plan.todayFocus().secondaryGoal(),
                plan.todayFocus().strategySummary(),
                buildRevisionHtml(revisionProblems),
                buildProblemsHtml(plan.recommendedProblems()),
                buildListItems(plan.sequenceReasoning().whyOnlyThese()),
                String.join(", ", plan.avoidToday().topicsToAvoid()),
                plan.avoidToday().reason(),
                buildListItems(plan.mentorObservations())
            );
    }

    // --- Helper Methods ---

    private static String buildRevisionHtml(List<String> revisionProblems) {
        // Safe check to ensure we don't render an empty unordered list
        if (revisionProblems == null || revisionProblems.isEmpty()) {
            return "<p style=\"color: #b45309; font-weight: 600; margin-bottom: 0;\">🎉 All caught up! No pending revisions for today.</p>";
        }
        return "<ul class=\"list-unstyled\" style=\"margin-bottom: 0;\">\n" + buildListItems(revisionProblems) + "\n</ul>";
    }

    private static String buildProblemsHtml(List<RecommendedProblem> problems) {
        StringBuilder html = new StringBuilder();
        for (RecommendedProblem prob : problems) {
            String topicsHtml = prob.topic().stream()
                .map(t -> "<span class=\"badge\">" + t + "</span>")
                .collect(Collectors.joining());

            html.append("""
                <div class="problem-card">
                    <h3>%d. %s</h3>
                    <div style="margin-bottom: 12px;">
                        <span class="badge badge-difficulty">%s</span>
                        %s
                    </div>
                    <p><strong>Why this now:</strong> %s</p>
                    <p><strong>Interview Signal:</strong> %s</p>
                    <p><strong>Expected Outcome:</strong> %s</p>
                </div>
                """.formatted(
                    prob.priorityOrder(), 
                    prob.problemName(), 
                    prob.difficulty(), 
                    topicsHtml, 
                    prob.whyThisNow(), 
                    prob.interviewSignal(), 
                    prob.expectedLearningOutcome()
                ));
        }
        return html.toString();
    }

    private static String buildListItems(List<String> items) {
        if (items == null) return "";
        return items.stream()
            .map(item -> "<li>" + item + "</li>")
            .collect(Collectors.joining("\n"));
    }
}