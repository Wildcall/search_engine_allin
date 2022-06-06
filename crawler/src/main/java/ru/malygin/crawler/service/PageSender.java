package ru.malygin.crawler.service;

import ru.malygin.helper.model.PageRequest;

public interface PageSender {
    void send(Long pageCount, PageRequest pageRequest);
}
