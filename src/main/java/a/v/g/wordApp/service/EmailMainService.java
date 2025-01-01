package a.v.g.wordApp.service;

import a.v.g.wordApp.utils.EmailUtil;
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
public class EmailMainService implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;
    private final EmailUtil emailUtil;

    @Value("${registration.subject}")
    private String regSubject;

    public void sendEmail(String to, String subject, String text){
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

    @Override
    public void sendRegistrationEmail(String userName, String email, String registrationToken) {
        var text = emailUtil.generateRegistrationEmail(userName, registrationToken);
        sendEmail(email, regSubject, text );
    }
}
