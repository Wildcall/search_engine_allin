package ru.malygin.crawler.sse.event;

import ru.malygin.crawler.sse.payload.CrawlerEventPayload;

public record CrawlerEvent(int id, CrawlerEventPayload payload) {
}
