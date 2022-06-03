package ru.malygin.notification.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import static ru.malygin.notification.service.NotificationServiceType.EMAIL;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final TemplateConfig templateConfig;

    @Bean
    public TemplateEngine htmlTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        String location = templateConfig
                .getLocation()
                .get(EMAIL);
        templateEngine.setTemplateResolver(htmlTemplateResolver(location));
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver(String location) {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setPrefix(location);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
