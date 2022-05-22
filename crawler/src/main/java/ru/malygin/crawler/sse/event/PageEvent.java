package ru.malygin.crawler.sse.event;

import ru.malygin.crawler.sse.payload.PageEventPayload;

public record PageEvent(int id, PageEventPayload payload) {
}
