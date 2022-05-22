package ru.malygin.searcher.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.malygin.searcher.model.dto.BaseDto;
import ru.malygin.searcher.model.dto.impl.StatisticDto;
import ru.malygin.searcher.model.entity.BaseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("_stat")
public class Statistic implements BaseEntity {
    @Id
    private Long id;
    private Long siteId;
    private Long appUserId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer savedPages;
    private Integer savedLemmas;
    private Integer savedIndexes;

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
                                             this.savedPages,
                                             this.savedLemmas,
                                             this.savedIndexes);
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
                                savedPages,
                                savedLemmas,
                                savedIndexes);
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        //  @formatter:off
        return siteId != null
                && appUserId != null
                && startTime != null
                && endTime != null
                && savedPages != null
                && savedLemmas != null
                && savedIndexes != null;
        //  @formatter:on
    }
}
