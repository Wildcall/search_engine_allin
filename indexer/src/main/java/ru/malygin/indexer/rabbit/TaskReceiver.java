package ru.malygin.indexer.rabbit.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.TaskAction;
import ru.malygin.indexer.service.IndexerService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskReceiver {

    private final IndexerService indexerService;
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
            logSender.send("RECEIVE / TaskId: " + task.getId() + " / Action: " + taskAction);
            indexerService.process(task, taskAction);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }
}
