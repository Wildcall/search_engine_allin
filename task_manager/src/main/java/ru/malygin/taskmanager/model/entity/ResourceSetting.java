package ru.malygin.taskmanager.model.entity;

import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.entity.impl.CrawlerSetting;
import ru.malygin.taskmanager.model.entity.impl.IndexerSetting;
import ru.malygin.taskmanager.model.entity.impl.SearcherSetting;

import java.io.Serializable;
import java.util.Map;

public interface ResourceSetting extends Serializable {

    Map<ServiceType, Class<? extends ResourceSetting>> classMap
            = Map.of(ServiceType.CRAWLER, CrawlerSetting.class,
                     ServiceType.INDEXER, IndexerSetting.class,
                     ServiceType.SEARCHER, SearcherSetting.class);

    ServiceType getType();

    ResourceSettingDto toResourceSettingDto();

    Map<String, Object> toMap();
}
