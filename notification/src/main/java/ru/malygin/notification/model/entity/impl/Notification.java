package ru.malygin.notification.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.notification.model.dto.BaseDto;
import ru.malygin.notification.model.dto.impl.NotificationDto;
import ru.malygin.notification.model.entity.BaseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements BaseEntity {

    private String type;
    private String sendTo;
    private String subject;
    private String template;
    private Map<String, String> payload;

    @Override
    public BaseDto toBaseDto() {
        return new NotificationDto(type,
                                   sendTo,
                                   subject,
                                   template,
                                   payload);
    }
}
