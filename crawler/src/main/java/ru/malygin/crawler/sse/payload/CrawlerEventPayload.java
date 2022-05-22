package ru.malygin.crawler.sse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.crawler.model.entity.impl.Statistic;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlerEventPayload implements Cloneable {

    private Long taskId;
    private Long siteId;
    private Long appUserId;
    private Integer statusCode;
    private LocalDateTime startTime;
    private Statistic statistic;

    @Override
    public CrawlerEventPayload clone() {
        CrawlerEventPayload crawlerEventPayload;
        try {
            crawlerEventPayload = (CrawlerEventPayload) super.clone();
        } catch (CloneNotSupportedException e) {
            //  @formatter:off
            crawlerEventPayload = new CrawlerEventPayload(this.taskId,
                                                          this.siteId,
                                                          this.appUserId,
                                                          this.statusCode,
                                                          this.startTime,
                                                          null);
            //  @formatter:on
        }
        crawlerEventPayload.statistic = this.statistic.clone();
        return crawlerEventPayload;
    }
}
