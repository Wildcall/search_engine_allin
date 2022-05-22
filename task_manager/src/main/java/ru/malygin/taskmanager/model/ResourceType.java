package ru.malygin.taskmanager.model;

public enum ResourceType {
    CRAWLER(0),
    INDEXER(1),
    SEARCHER(2),
    REGISTRATION(-1),
    NOTIFICATION(-1);

    private final int order;

    ResourceType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
