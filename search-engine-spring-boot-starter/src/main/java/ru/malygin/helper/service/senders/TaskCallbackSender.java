package ru.malygin.helper.service.senders;

import org.springframework.context.ApplicationListener;
import ru.malygin.helper.events.TaskCallbackEvent;

public interface TaskCallbackSender extends ApplicationListener<TaskCallbackEvent> {
}
