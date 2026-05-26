package com.cleanlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cleanlearn.service.ReportGenerationService;
import com.cleanlearn.service.external.GroqService;
import com.cleanlearn.dto.GroqResponseParser.StudyPlanResponse;

@RestController
@RequestMapping("/api/v1/dailyReport")
public class DailyReportController {
    
    @Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private GroqService groqService;

    @GetMapping("/generate")
    public String generateDailyReport(@RequestParam Long userId) throws Exception {
        StudyPlanResponse studyPlan = groqService.askGroq(userId);
        reportGenerationService.generateAndSendEmailReport(studyPlan, userId);
        return "Daily report generated and sent successfully!";
    }   

}