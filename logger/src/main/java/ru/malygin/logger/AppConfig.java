package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.DefaultQueueDeclareService;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public boolean declareQueue(DefaultQueueDeclareService defaultQueueDeclareService) {
        defaultQueueDeclareService.declareLogQueue();
        defaultQueueDeclareService.declareMetricsQueue();
        return true;
    }
}
