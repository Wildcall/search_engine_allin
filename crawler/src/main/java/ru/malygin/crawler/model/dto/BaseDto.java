package ru.malygin.crawler.model.dto;

import ru.malygin.crawler.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
