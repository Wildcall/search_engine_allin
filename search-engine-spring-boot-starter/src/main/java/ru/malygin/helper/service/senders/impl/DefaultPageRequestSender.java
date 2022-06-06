package ru.malygin.helper.service.senders.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.helper.service.senders.PageRequestSender;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultPageRequestSender implements PageRequestSender {

    private final RabbitTemplate rabbitTemplate;
    private final LogSender logSender;
    private final SearchEngineProperties.Common.Request requestProp;
    private final Random random = new Random();
    private final Map<Long, PageRequest> callbackAddress = new ConcurrentHashMap<>();

    @Override
    public Long sendPageRequest(PageRequest pageRequest) {
        String pageQueue = String.format("page-response-queue.%s.%s.%s.%s", pageRequest.getTaskId(),
                                         pageRequest.getSiteId(), pageRequest.getAppUserId(), random.nextLong());

        pageRequest.setPageQueue(pageQueue);
        logSender.info("SEND PAGE REQUEST / Task id: %s / Site id: %s / AppUser id: %s / Page response queue: %s",
                       pageRequest.getTaskId(), pageRequest.getSiteId(), pageRequest.getAppUserId(),
                       pageRequest.getPageQueue());
        Long pagesCount = (Long) rabbitTemplate.convertSendAndReceive(requestProp.getExchange(),
                                                                      requestProp.getPageRoute(), pageRequest);
        if (pagesCount == null)
            return -1L;
        logSender.info("PAGE RESPONSE COUNT / Count: %s", pagesCount);
        callbackAddress.put(pageRequest.getTaskId(), pageRequest);
        return pagesCount;
    }

    @Override
    public Optional<PageRequest> getPageResponseQueue(Long taskId) {
        return Optional.ofNullable(callbackAddress.get(taskId));
    }
}
