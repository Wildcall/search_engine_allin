package ru.malygin.indexer.sse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.indexer.model.entity.impl.Statistic;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexerEventPayload implements Cloneable {

    private Long taskId;
    private Long siteId;
    private Long appUserId;
    private Integer statusCode;
    private LocalDateTime startTime;
    private Statistic statistic;

    @Override
    public IndexerEventPayload clone() {
        IndexerEventPayload indexerEventPayload;
        try {
            indexerEventPayload = (IndexerEventPayload) super.clone();
        } catch (CloneNotSupportedException e) {
            //  @formatter:off
            indexerEventPayload = new IndexerEventPayload(this.taskId,
                                                          this.siteId,
                                                          this.appUserId,
                                                          this.statusCode,
                                                          this.startTime,
                                                          null);
            //  @formatter:on
        }
        indexerEventPayload.statistic = this.statistic.clone();
        return indexerEventPayload;
    }
}
