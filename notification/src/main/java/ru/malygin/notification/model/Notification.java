package ru.malygin.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String type;
    private String sendTo;
    private String subject;
    private String template;
    private Map<String, String> payload;
}
