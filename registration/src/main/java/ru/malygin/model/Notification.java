package ru.malygin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Notification {
    private String type;
    private String sendTo;
    private String subject;
    private String template;
    private Map<String, String> payload;
}
