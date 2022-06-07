package ru.malygin.helper.service.cross;

import reactor.core.publisher.Flux;

public interface DataListener<T> {
    Flux<T> listenData(Long taskId,
                       Long itemCount);
}
