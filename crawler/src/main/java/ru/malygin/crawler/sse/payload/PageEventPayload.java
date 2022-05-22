package ru.malygin.crawler.sse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.malygin.crawler.model.entity.impl.Page;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PageEventPayload {

    private Long id;
    private Long siteId;
    private Long appUserId;
    private String path;
    private Integer code;
    private LocalDateTime createTime;

    public static PageEventPayload fromPage(Page page) {
        return new PageEventPayload(page.getId(),
                                    page.getSiteId(),
                                    page.getAppUserId(),
                                    page.getPath(),
                                    page.getCode(),
                                    page.getCreateTime());
    }
}
