package ru.malygin.indexer.sse.event;

import ru.malygin.indexer.sse.payload.IndexerEventPayload;

public record IndexerEvent(int id, IndexerEventPayload payload) {
}
