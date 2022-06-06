package ru.malygin.crawler.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.malygin.helper.model.PageRequest;

public interface PageRequestListener {

    @RabbitListener(queues = "#{properties.getCommon().getRequest().getPageRoute()}")
    Long receivePageRequest(PageRequest pageRequest);
}
