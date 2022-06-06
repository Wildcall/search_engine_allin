package ru.malygin.helper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    private Long siteId;
    private Long taskId;
    private Long appUserId;
    private String pageQueue;

    public PageRequest(Long siteId,
                       Long taskId,
                       Long appUserId) {
        this.siteId = siteId;
        this.taskId = taskId;
        this.appUserId = appUserId;
    }
}
