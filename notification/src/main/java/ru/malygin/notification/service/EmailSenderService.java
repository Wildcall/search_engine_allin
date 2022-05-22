package ru.malygin.notification.service;

public interface EmailSenderService {
    void send(String to,
              String subject,
              String htmlBody);
}
