package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.malygin.notification.config.TemplateConfig;
import ru.malygin.notification.exception.BadRequestException;
import ru.malygin.notification.exception.InternalException;
import ru.malygin.notification.model.entity.impl.Notification;
import ru.malygin.notification.service.NotificationSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static ru.malygin.notification.config.TemplateConfig.TemplateParam;
import static ru.malygin.notification.service.NotificationSenderType.EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationSender implements NotificationSender {

    private final TemplateConfig templateConfig;
    private final TemplateEngine htmlTemplateEngine;
    private final JavaMailSender javaMailSender;

    @Override
    public String send(Notification notification) {
        try {
            log.info("EMAIL SEND  / Type: {} / Template: {} / Send to: {}", notification.getType(),
                     notification.getTemplate(), notification.getSendTo());
            String sendTo = notification.getSendTo();
            String subject = notification.getSubject();

            TemplateParam templateParam = templateConfig
                    .getTypes()
                    .get(EMAIL)
                    .get(notification.getTemplate());

            if (templateParam == null) throw new BadRequestException(
                    "Template - " + notification.getTemplate() + " not supported");

            String templateName = templateParam.getName();
            Map<String, String> fields = templateParam.getFields();
            Context ctx = new Context();

            log.info("Fields: {}", notification
                    .getPayload());

            fields
                    .keySet()
                    .forEach(field -> ctx.setVariable(field, notification
                            .getPayload()
                            .get(field)));

            String htmlBody = this.htmlTemplateEngine.process(templateName, ctx);

            new Thread(() -> send(sendTo, subject, htmlBody)).start();

            return sendTo;
        } catch (Exception e) {
            log.error("NOTIFICATION ERROR / Error: {} / Type: {} / Template: {} / Send to: {}", e.getMessage(),
                      notification.getType(), notification.getTemplate(), notification.getSendTo());
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public String getType() {
        return EMAIL;
    }

    private void send(String to,
                      String subject,
                      String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
