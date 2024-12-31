package a.v.g.wordApp.controllers;

import a.v.g.wordApp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        try {
            emailService.sendEmail("a.j.lapin1@gmail.com", "Test Subject", "This is a test email");
            return "Email sent successfully";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }
}
