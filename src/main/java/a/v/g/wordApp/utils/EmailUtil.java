package a.v.g.wordApp.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EmailUtil {

    private String regLinkDefault = "http://localhost:8080/registration/submit?rt=";
    private String textTemplateDefault = """
            Уважаемый {0},
            
            Спасибо за регистрацию на нашем сервисе. Для завершения регистрации, пожалуйста, перейдите по следующей ссылке:
            
            {1}
            
            Если вы не запрашивали это письмо, пожалуйста, проигнорируйте его.
            
            С уважением,
            Команда сервиса
            """;

    @Value("${registration.link:#{null}}")
    private String registrationLink;

    @Value("${registration.textTemplate:#{null}}")
    private String textTemplate;

    @PostConstruct
    public void init() {
        if (registrationLink == null) {
            registrationLink = regLinkDefault;
        }

        if (textTemplate == null) {
            textTemplate = textTemplateDefault;
        }
    }


    public String generateRegistrationEmail(String username, String registrationToken) {
        return MessageFormat.format(textTemplate, username, registrationLink + registrationToken);
    }


}
