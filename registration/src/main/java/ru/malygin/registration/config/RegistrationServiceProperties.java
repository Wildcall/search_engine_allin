package ru.malygin.registration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("regProperties")
@ConfigurationProperties("spring.search-engine.registration")
public class RegistrationServiceProperties {

    private final Expiration expiration = new Expiration();
    private final Email email = new Email();
    private String secret = "amtzZGJuZmFzaWx1ZGRmO2xrbnFsO2tmanF3ay9kZm13O2xma3cnO2RmLHF3LmRmbXdkZgo";

    @Data
    public static class Expiration {
        private Long access = 0L;
        private Long refresh = 0L;
        private Long confirm = 0L;
    }

    @Data
    public static class Email {
        private Long resendTimeoutInMin = 10L;
        private String callbackAddress = "http://localhost:3000/email/confirm/token/";
    }
}
