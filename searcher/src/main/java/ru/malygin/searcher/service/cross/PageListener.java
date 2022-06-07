package ru.malygin.searcher.service.cross;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.DefaultTempListenerContainerFactory;
import ru.malygin.helper.service.cross.DataListener;
import ru.malygin.helper.service.cross.DataReceiver;
import ru.malygin.searcher.model.entity.Page;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageListener implements DataListener<Page> {

    private final DefaultTempListenerContainerFactory containerFactory;
    private final DataReceiver pageRequestSender;
    private final ObjectMapper mapper;
    private final SearchEngineProperties properties;

    @Override
    public Flux<Page> listenData(Long taskId,
                                 Long itemCount) {
        String requestRoute = properties
                .getCommon()
                .getRequest()
                .getPageRoute();
        DataRequest dataRequest = pageRequestSender
                .getDataListenerQueue(taskId, requestRoute)
                .orElse(null);
        if (dataRequest == null) return Flux.empty();

        AtomicLong pageFetch = new AtomicLong(0);
        return Flux.create(emmiter -> containerFactory.create(dataRequest.getDataQueue(), message -> {
            try {
                Page page = mapper.readValue(message.getBody(), Page.class);
                emmiter.next(page);
                pageFetch.incrementAndGet();
                if (pageFetch.get() == itemCount) {
                    emmiter.complete();
                    containerFactory.remove(dataRequest.getDataQueue());
                    pageRequestSender.removeDataListenerQueue(taskId, requestRoute);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }));
    }
}
