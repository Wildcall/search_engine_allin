package ru.malygin.indexer.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.indexer.model.dto.BaseDto;
import ru.malygin.indexer.model.dto.view.View;
import ru.malygin.indexer.model.entity.BaseEntity;
import ru.malygin.indexer.model.entity.impl.Statistic;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto implements BaseDto {

    @JsonView(View.Response.class)
    private Long id;

    @JsonView(View.Response.class)
    private Long siteId;

    @JsonView(View.Response.class)
    private Long appUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(View.Response.class)
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(View.Response.class)
    private LocalDateTime endTime;

    @JsonView(View.Response.class)
    private Integer parsedPages;

    @JsonView(View.Response.class)
    private Integer savedLemmas;

    @JsonView(View.Response.class)
    private Integer createdIndexes;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Statistic(id,
                             siteId,
                             appUserId,
                             startTime,
                             endTime,
                             parsedPages,
                             savedLemmas,
                             createdIndexes);
        //  @formatter:on
    }
}
