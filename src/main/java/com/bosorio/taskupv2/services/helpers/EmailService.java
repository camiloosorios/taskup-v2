package com.bosorio.taskupv2.services.helpers;

import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String to, String name, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setFrom("UpTask <admin@uptask.com>");
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject("UpTask - Confirm your account");
            String htmlTemplate = readFile("confirmAccountTemplate.html");
            String htmlContent = htmlTemplate.replace("${user}", name);
            htmlContent = htmlContent.replace("${token}", token);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void sendResetPasswordEmail(String to, String name, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setFrom("UpTask <admin@uptask.com>");
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject("UpTask - Reset your password");
            String htmlTemplate = readFile("resetPasswordTemplate.html");
            String htmlContent = htmlTemplate.replace("${user}", name);
            htmlContent = htmlContent.replace("${token}", token);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private String readFile(String filepath) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + filepath);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}