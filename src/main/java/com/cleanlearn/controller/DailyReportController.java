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
    public String generateDailyReport() {
        StudyPlanResponse studyPlan = groqService.askGroq();
        reportGenerationService.generateAndSendEmailReport(null, studyPlan);
        return "Daily report generated and sent successfully!";
    }   

}