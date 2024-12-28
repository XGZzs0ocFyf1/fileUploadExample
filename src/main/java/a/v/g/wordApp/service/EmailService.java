package a.v.g.wordApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;

    public void sendEmail(String to, String subject, String text) throws MailException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(userName);
            helper.setText(text);

            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new MailException("Error sending email", e) {};
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
