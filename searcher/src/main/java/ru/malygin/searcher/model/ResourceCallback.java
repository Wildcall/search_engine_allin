package ru.malygin.searcher.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ResourceCallback extends ApplicationEvent {

    private final Long id;
    private final Integer status;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long statId;

    public ResourceCallback(Long id,
                            Integer status,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            Long statId) {
        super(id);
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.statId = statId;
    }
}
