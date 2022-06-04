package ru.malygin.taskmanager.model;

public enum ServiceType {
    CRAWLER(0),
    INDEXER(1),
    SEARCHER(2),
    REGISTRATION(-1),
    NOTIFICATION(-1);

    private final int order;

    ServiceType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}