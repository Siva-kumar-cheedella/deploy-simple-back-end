package com.goodmorning.service;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendEmail(String to, String subject, String htmlContent) {
        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from("no-reply@cleanlearn.in") // Use this for testing
            .to(to)
            .subject(subject)
            .html(htmlContent)
            .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email sent successfully! ID: " + data.getId());
        } catch (ResendException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
