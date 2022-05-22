package ru.malygin.notification.model.dto;

import ru.malygin.notification.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
