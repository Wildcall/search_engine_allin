package ru.malygin.crawler.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.malygin.crawler.model.dto.BaseDto;
import ru.malygin.crawler.model.dto.impl.StatisticDto;
import ru.malygin.crawler.model.entity.BaseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("_stat")
public class Statistic implements BaseEntity, Cloneable {
    @Id
    private Long id;
    private Long siteId;
    private Long appUserId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer fetchPages;
    private Integer savedPages;
    private Integer linksCount;
    private Integer errors;

    public Statistic clone() {
        //  @formatter:off
        Statistic crawlerStatistic;
        try {
            crawlerStatistic = (Statistic) super.clone();
        } catch (CloneNotSupportedException e) {
            crawlerStatistic = new Statistic(this.id,
                                               this.siteId,
                                               this.appUserId,
                                               this.startTime,
                                               this.endTime,
                                               this.fetchPages,
                                               this.savedPages,
                                               this.linksCount,
                                               this.errors);
        }
        return crawlerStatistic;
        //  @formatter:on
    }

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new StatisticDto(id,
                                siteId,
                                appUserId,
                                startTime,
                                endTime,
                                fetchPages,
                                savedPages,
                                linksCount,
                                errors);
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        //  @formatter:off
        return siteId != null
                && appUserId != null
                && startTime != null
                && endTime != null
                && fetchPages != null
                && savedPages != null
                && linksCount != null
                && errors != null;
        //  @formatter:on
    }
}
