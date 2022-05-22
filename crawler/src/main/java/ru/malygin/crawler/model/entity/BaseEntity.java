package ru.malygin.crawler.model.entity;

import ru.malygin.crawler.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();

    boolean hasRequiredField();
}
