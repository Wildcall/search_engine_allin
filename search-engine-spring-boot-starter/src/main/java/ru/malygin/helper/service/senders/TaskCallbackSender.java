package ru.malygin.helper.service.senders;

import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskState;

public interface CallbackSender {
    void send(Long id,
              TaskAction action,
              TaskState state);
}
