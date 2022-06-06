package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.service.PageRequestListener;
import ru.malygin.crawler.service.PageSender;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.senders.LogSender;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageRequestListenerImpl implements PageRequestListener {

    private final PageService pageService;
    private final PageSender pageSender;
    private final LogSender logSender;

    @Override
    public Long receivePageRequest(PageRequest pageRequest) {
        logSender.info("RECEIVE PAGE REQUEST / Task id: %s / Site id: %s / AppUser id: %s / Page response queue: %s",
                       pageRequest.getTaskId(), pageRequest.getSiteId(), pageRequest.getAppUserId(),
                       pageRequest.getPageQueue());
        return pageService
                .getCountBySiteIdAndAppUserId(pageRequest.getSiteId(), pageRequest.getAppUserId())
                .doOnSuccess(pageCount -> pageSender.send(pageCount, pageRequest))
                .block();
    }
}
