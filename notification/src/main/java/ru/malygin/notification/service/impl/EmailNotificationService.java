package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.malygin.helper.model.Notification;
import ru.malygin.helper.senders.LogSender;
import ru.malygin.notification.config.TemplateConfig;
import ru.malygin.notification.service.NotificationService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static ru.malygin.notification.config.TemplateConfig.TemplateParam;
import static ru.malygin.notification.service.NotificationServiceType.EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationService implements NotificationService {

    private final TemplateConfig templateConfig;
    private final TemplateEngine htmlTemplateEngine;
    private final JavaMailSender javaMailSender;
    private final LogSender logSender;

    @Override
    public void send(Notification notification) {
        try {
            String sendTo = notification.getSendTo();
            String subject = notification.getSubject();

            TemplateParam templateParam = templateConfig
                    .getTypes()
                    .get(EMAIL)
                    .get(notification.getTemplate());

            if (templateParam == null) {
                logSender.error("Template %s not supported", notification.getTemplate());
                return;
            }

            String templateName = templateParam.getName();
            Map<String, String> fields = templateParam.getFields();
            Context ctx = new Context();
            fields
                    .keySet()
                    .forEach(field -> ctx.setVariable(field, notification
                            .getPayload()
                            .get(field)));

            String htmlBody = this.htmlTemplateEngine.process(templateName, ctx);

            this.send(sendTo, subject, htmlBody);
            logSender.info("EMAIL SEND / Type: %s / Template: %s / Send to: %s", notification.getType(),
                           notification.getTemplate(), notification.getSendTo());
        } catch (Exception e) {
            logSender.error("EMAIL SEND ERROR / Error: %s / Type: %s / Template: %s / Send to: %s", e.getMessage(),
                            notification.getType(), notification.getTemplate(), notification.getSendTo());
        }
    }

    @Override
    public String getType() {
        return EMAIL;
    }

    private void send(String to,
                      String subject,
                      String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }
}
