package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.CategoryTotalsDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private TransactionService transactionService;

    public void sendWelcomeEmail(String to, Map<String, Object> templateModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(templateModel);
        String html = templateEngine.process("welcome-email", context);

        helper.setFrom("treasuryhub@outlook.com");
        helper.setTo(to);
        helper.setSubject("Welcome to Treasury Hub!");
        helper.setText(html, true);

        mailSender.send(message);
    }

    public void sendLastMonthStatus(String to, Map<String, Object> templateModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(templateModel);
        String html = templateEngine.process("monthly-report-email", context);

        helper.setFrom("treasuryhub@outlook.com");
        helper.setTo(to);
        helper.setSubject("Your monthly statement");
        helper.setText(html, true);

        mailSender.send(message);
    }
}
