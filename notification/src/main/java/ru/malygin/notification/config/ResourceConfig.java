package ru.malygin.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@Validated
@ConfigurationProperties("resource")
public class ResourceConfig {

    @NotBlank
    @NotNull
    @NotEmpty
    private String secret;
}
