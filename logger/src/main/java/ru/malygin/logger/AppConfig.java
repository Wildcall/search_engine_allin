package ru.malygin.logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.DefaultQueueDeclareService;

@Configuration
public class AppConfig {

    @Bean
    public boolean declareQueue(DefaultQueueDeclareService defaultQueueDeclareService) {
        defaultQueueDeclareService.declareLogQueue();
        defaultQueueDeclareService.declareMetricsQueue();
        return true;
    }
}
