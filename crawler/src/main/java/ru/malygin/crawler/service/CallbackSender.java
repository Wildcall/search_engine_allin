package ru.malygin.crawler.service;

import org.springframework.context.ApplicationListener;
import ru.malygin.crawler.model.ResourceCallback;

public interface CallbackSender extends ApplicationListener<ResourceCallback> {
}
