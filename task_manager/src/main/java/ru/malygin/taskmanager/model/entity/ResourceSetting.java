package ru.malygin.taskmanager.model.entity;

import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.entity.impl.CrawlerSetting;
import ru.malygin.taskmanager.model.entity.impl.IndexerSetting;
import ru.malygin.taskmanager.model.entity.impl.SearcherSetting;

import java.io.Serializable;
import java.util.Map;

public interface ResourceSetting extends Serializable {

    Map<ResourceType, Class<? extends ResourceSetting>> classMap
            = Map.of(ResourceType.CRAWLER, CrawlerSetting.class,
                     ResourceType.INDEXER, IndexerSetting.class,
                     ResourceType.SEARCHER, SearcherSetting.class);

    ResourceType getType();

    ResourceSettingDto toResourceSettingDto();

    Map<String, Object> toMap();
}
