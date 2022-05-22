package ru.malygin.searcher.model.dto;

import ru.malygin.searcher.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
