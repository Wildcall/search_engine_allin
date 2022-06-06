package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.receivers.impl.DefaultTaskReceiver;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MsgReceiversConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "queue", havingValue = "true")
    public DefaultTaskReceiver taskReceiver(ApplicationEventPublisher p,
                                            ObjectMapper objectMapper,
                                            Map<String, Class<?>> idClassMap) {
        log.info("[*] Create TaskReceiver in starter");
        return new DefaultTaskReceiver(p, objectMapper, idClassMap);
    }
}
