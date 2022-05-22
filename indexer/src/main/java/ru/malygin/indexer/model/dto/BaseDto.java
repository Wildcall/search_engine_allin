package ru.malygin.indexer.model.dto;

import ru.malygin.indexer.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
