package ru.malygin.searcher.service;

import org.springframework.context.ApplicationListener;
import ru.malygin.searcher.model.ResourceCallback;

public interface CallbackSender extends ApplicationListener<ResourceCallback> {
}
