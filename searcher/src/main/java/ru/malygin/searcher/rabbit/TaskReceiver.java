package ru.malygin.searcher.rabbit;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.malygin.logsenderspringbootstarter.service.LogSender;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.model.TaskAction;
import ru.malygin.searcher.service.SearcherService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskReceiver {

    private final SearcherService searcherService;
    private final LogSender logSender;

    @RabbitListener(
            queuesToDeclare = @Queue(
                    name = "${rabbit.queues.task-queue}",
                    durable = "false",
                    autoDelete = "false"),
            messageConverter = "jsonConverter")
    public void receive(Task task,
                        @Header("action") String action) {
        try {
            TaskAction taskAction = TaskAction.valueOf(action);
            logSender.info("RECEIVE / TaskId: " + task.getId() + " / Action: " + taskAction);
            searcherService.process(task, taskAction);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }
}
