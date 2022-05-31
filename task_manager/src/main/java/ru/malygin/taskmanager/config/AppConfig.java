package ru.malygin.taskmanager.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.taskmanager.model.entity.impl.Task;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        return map;
    }
}
