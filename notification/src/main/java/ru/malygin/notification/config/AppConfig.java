package ru.malygin.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.model.Notification;
import ru.malygin.helper.service.DefaultQueueDeclareService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Notification", Notification.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }

    @Bean
    public boolean declareQueue(DefaultQueueDeclareService defaultQueueDeclareService) {
        defaultQueueDeclareService.declareNotificationQueue();
        return true;
    }
}
