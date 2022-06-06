package ru.malygin.helper.service.senders;

import ru.malygin.helper.model.PageRequest;

import java.util.Optional;

public interface PageRequestSender {
    Long sendPageRequest(PageRequest pageRequest);

    Optional<PageRequest> getPageResponseQueue(Long taskId);
}
