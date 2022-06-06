package ru.malygin.indexer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.DefaultTempListenerContainerFactory;
import ru.malygin.helper.service.senders.impl.DefaultPageRequestSender;
import ru.malygin.indexer.model.Page;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.service.PageResponseListener;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageResponseListenerImpl implements PageResponseListener {

    private final DefaultTempListenerContainerFactory containerFactory;
    private final DefaultPageRequestSender pageRequestSender;
    private final ObjectMapper mapper;

    @Override
    public Flux<Page> listenPageResponse(Task task,
                                         Long pageCount) {

        PageRequest pageRequest = pageRequestSender
                .getPageResponseQueue(task.getId())
                .orElse(null);
        if (pageRequest == null) return Flux.empty();

        AtomicLong pageFetch = new AtomicLong(0);
        return Flux.create(emmiter -> containerFactory.create(pageRequest.getPageQueue(), message -> {
            try {
                Page page = mapper.readValue(message.getBody(), Page.class);
                emmiter.next(page);
                pageFetch.incrementAndGet();
                if (pageFetch.get() == pageCount) {
                    emmiter.complete();
                    containerFactory.remove(pageRequest.getPageQueue());
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }));
    }
}
