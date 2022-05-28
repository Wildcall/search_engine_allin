package ru.malygin.crawler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.crawler.model.Task;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final ObjectMapper mapper;

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(mapper);
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("Task", Task.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public void configureObjectMapper() {
        mapper.findAndRegisterModules();
    }
}
