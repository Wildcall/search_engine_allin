package ru.malygin.notification.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.notification.model.dto.BaseDto;
import ru.malygin.notification.model.dto.view.View;
import ru.malygin.notification.model.entity.BaseEntity;
import ru.malygin.notification.model.entity.impl.Notification;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDto implements BaseDto {

    @NotNull(groups = {View.Request.class})
    @NotEmpty(groups = {View.Request.class})
    @NotBlank(groups = {View.Request.class})
    private String type;

    @NotNull(groups = {View.Request.class})
    @NotEmpty(groups = {View.Request.class})
    @NotBlank(groups = {View.Request.class})
    @Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", groups = {View.Request.class})
    private String sendTo;

    @NotNull(groups = {View.Request.class})
    @NotEmpty(groups = {View.Request.class})
    @NotBlank(groups = {View.Request.class})
    private String subject;

    @NotNull(groups = {View.Request.class})
    @NotEmpty(groups = {View.Request.class})
    @NotBlank(groups = {View.Request.class})
    private String template;

    @NotNull(groups = {View.Request.class})
    private Map<String, String> payload;

    @Override
    public BaseEntity toBaseEntity() {
        return new Notification(type,
                                sendTo,
                                subject,
                                template,
                                payload);
    }
}
