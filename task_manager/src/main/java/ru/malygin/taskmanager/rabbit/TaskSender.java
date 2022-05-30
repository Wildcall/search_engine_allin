package ru.malygin.taskmanager.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.TaskAction;
import ru.malygin.taskmanager.model.entity.impl.Task;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    @Value(value = "${rabbit.queues.crawler-task-queue}")
    private String crawlerTaskQueueName;
    @Value(value = "${rabbit.queues.indexer-task-queue}")
    private String indexerTaskQueueName;
    @Value(value = "${rabbit.queues.searcher-task-queue}")
    private String searcherTaskQueueName;

    @Bean
    public Queue getCrawlerTaskQueue() {
        return new Queue(crawlerTaskQueueName, false, false, false);
    }

    @Bean
    public Queue getIndexerQueue() {
        return new Queue(indexerTaskQueueName, false, false, false);
    }

    @Bean
    public Queue getSearcherQueue() {
        return new Queue(searcherTaskQueueName, false, false, false);
    }

    public void send(Task task,
                     TaskAction action) {
        if (task != null) {
            try {
                byte[] body = objectMapper
                        .writeValueAsString(task.toBody())
                        .getBytes();
                Message message =
                        MessageBuilder
                                .withBody(body)
                                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                                .setHeader("__TypeId__", "Task")
                                .setHeader("action", action.name())
                                .build();

                if (task.getType().equals(ResourceType.CRAWLER)) {
                    rabbitTemplate.send(crawlerTaskQueueName, message);
                    return;
                }
                if (task.getType().equals(ResourceType.INDEXER)) {
                    rabbitTemplate.send(indexerTaskQueueName, message);
                    return;
                }
                if (task.getType().equals(ResourceType.SEARCHER))
                    rabbitTemplate.send(searcherTaskQueueName, message);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }
}
