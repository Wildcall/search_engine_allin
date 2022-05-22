package ru.malygin.indexer.service;

import org.springframework.context.ApplicationListener;
import ru.malygin.indexer.model.ResourceCallback;

public interface CallbackSender extends ApplicationListener<ResourceCallback> {
}

