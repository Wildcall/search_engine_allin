package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import ru.malygin.taskmanager.config.ResourceConfig;
import ru.malygin.taskmanager.config.WebClientConfiguration;
import ru.malygin.taskmanager.model.Notification;
import ru.malygin.taskmanager.model.ResourceCallback;
import ru.malygin.taskmanager.model.TaskState;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.security.JwtUtil;
import ru.malygin.taskmanager.service.NotificationService;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JwtUtil jwtUtil;
    private final ResourceConfig resourceConfig;
    private final WebClientConfiguration webClientConfiguration;

    @Override
    public void send(Task task,
                     ResourceCallback resourceCallback) {
        //  @formatter:off
        TaskState state = TaskState.getFromState(resourceCallback.getStatus());

        String type = "email";
        String sendTo = task.getAppUser().getEmail();
        String subject = "Task " + task.getId() + " " + state.name().toLowerCase(Locale.ROOT);
        String template = state.getTemplate();
        String name = sendTo.substring(0, sendTo.indexOf("@"));
        String taskId = task.getId().toString();
        String taskType = task.getType().name();
        String startTime = task.getStartTime() == null ? "" : task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String endTime = task.getEndTime() == null ? "" : task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String sitePath = task.getSite().getPath();
        String siteName = task.getSite().getName();
        Map<String, String> payload = Map.of("name", name,
                                             "taskId", taskId,
                                             "taskType", taskType,
                                             "startTime", startTime,
                                             "endTime", endTime,
                                             "sitePath", sitePath,
                                             "siteName", siteName);
        //  @formatter:on
        send(new Notification(type, sendTo, subject, template, payload));
    }

    private void send(Notification notification) {
        log.info("NOTIFICATION SEND / SendTo: {} / Subject: {} / Template: {}", notification.getSendTo(),
                 notification.getSubject(), notification.getTemplate());
        try {
            ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(ResourceType.NOTIFICATION);
            String uri = resourceParam.getBaseUrl() + resourceParam
                    .getAvailablePaths()
                    .get("send");
            webClientConfiguration
                    .getWebClient()
                    .post()
                    .uri(uri)
                    .body(BodyInserters.fromValue(notification))
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + jwtUtil.generateResourceToken(ResourceType.NOTIFICATION))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("NOTIFICATION ERROR: {}", e.getMessage());
        }
    }
}
