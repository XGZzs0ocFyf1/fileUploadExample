package a.v.g.wordApp.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);


    void sendRegistrationEmail(String userName, String email, String registrationToken);
}
