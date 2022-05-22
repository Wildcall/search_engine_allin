package ru.malygin.taskmanager.model.dto;

import ru.malygin.taskmanager.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
