package ru.malygin.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("templates")
public class TemplateConfig {

    private Map<String, String> location = new HashMap<>();
    private Map<String, Map<String, TemplateParam>> types = new HashMap<>();

    @Data
    public static class TemplateParam {
        private String name;
        private Map<String, String> fields;
    }
}
