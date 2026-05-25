package com.cleanlearn.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import com.cleanlearn.service.EmailService;
import com.cleanlearn.dto.GroqResponseParser.*;

@Service
public class ReportGenerationService {

    @Autowired
    private EmailService emailService;

    public void generateAndSendEmailReport(String to, StudyPlanResponse plan) {
        
        to="ksiva7393@gmail.com";

        String subject = "Your Daily Study Plan - " + java.time.LocalDate.now();
        String htmlContent = generateEmailContent(plan);
        
        emailService.sendEmail(to, subject, htmlContent);
    }

    public String generateEmailContent(StudyPlanResponse plan) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; color: #333333; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
                    h1, h2, h3 { color: #2c3e50; }
                    .header { text-align: center; border-bottom: 2px solid #edf2f7; padding-bottom: 20px; margin-bottom: 25px; }
                    .header h1 { margin: 0; color: #2b6cb0; font-size: 24px; }
                    .section-card { background: #ebf8fa; padding: 20px; border-radius: 8px; margin-bottom: 25px; }
                    .section-card h2 { margin-top: 0; color: #2b6cb0; font-size: 18px; }
                    .problem-card { border: 1px solid #e2e8f0; padding: 20px; margin-bottom: 15px; border-radius: 8px; border-left: 4px solid #4299e1; background: #fafafa; }
                    .problem-card h3 { margin-top: 0; font-size: 18px; margin-bottom: 10px; }
                    .badge { display: inline-block; padding: 4px 10px; font-size: 12px; font-weight: 600; border-radius: 15px; background: #edf2f7; color: #4a5568; margin-right: 5px; margin-bottom: 5px; }
                    .badge-difficulty { background: #f6ad55; color: white; }
                    .alert-card { background: #fff5f5; padding: 20px; border-radius: 8px; border-left: 4px solid #f56565; margin-bottom: 25px; }
                    .alert-card h2 { margin-top: 0; color: #c53030; font-size: 18px; }
                    .list-unstyled { padding-left: 20px; margin-top: 10px; }
                    .list-unstyled li { margin-bottom: 8px; }
                    .footer { text-align: center; margin-top: 30px; font-size: 13px; color: #718096; border-top: 1px solid #edf2f7; padding-top: 20px; }
                    p { margin: 8px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚀 Your Daily Study Plan</h1>
                        <p style="color: #718096; margin: 5px 0 0 0;">Ready to crush your interview prep today?</p>
                    </div>

                    <div class="section-card">
                        <h2>🎯 Today's Focus (%d mins)</h2>
                        <p><strong>Primary Goal:</strong> %s</p>
                        <p><strong>Secondary Goal:</strong> %s</p>
                        <p><strong>Strategy:</strong> %s</p>
                    </div>

                    <div>
                        <h2>💻 Recommended Problems</h2>
                        %s
                    </div>

                    <div>
                        <h2>🧠 Learning Strategy</h2>
                        <p><strong>Why these patterns:</strong></p>
                        <ul class="list-unstyled">
                            %s
                        </ul>
                    </div>

                    <div class="alert-card">
                        <h2>🚫 What to Avoid Today</h2>
                        <p><strong>Skip these topics:</strong> %s</p>
                        <p><strong>Reason:</strong> %s</p>
                    </div>

                    <div>
                        <h2>💡 Mentor Observations</h2>
                        <ul class="list-unstyled">
                            %s
                        </ul>
                    </div>

                    <div class="footer">
                        <p>Happy Coding! Keep up the momentum.</p>
                        <p>© CleanLearn Platform</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                plan.todayFocus().estimatedTotalTimeMinutes(),
                plan.todayFocus().primaryGoal(),
                plan.todayFocus().secondaryGoal(),
                plan.todayFocus().strategySummary(),
                buildProblemsHtml(plan.recommendedProblems()),
                buildListItems(plan.sequenceReasoning().whyOnlyThese()),
                String.join(", ", plan.avoidToday().topicsToAvoid()),
                plan.avoidToday().reason(),
                buildListItems(plan.mentorObservations())
            );
    }

    // --- Helper Methods to generate dynamic lists ---

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
        return items.stream()
            .map(item -> "<li>" + item + "</li>")
            .collect(Collectors.joining("\n"));
    }
}