package ru.malygin.helper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.malygin.helper.config.MsgAutoConfiguration;
import ru.malygin.helper.config.SearchEngineProperties;

@ComponentScan
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SearchEngineProperties.class)
@Import({MsgAutoConfiguration.class})
public class SearchEngineAutoConfiguration {

}
