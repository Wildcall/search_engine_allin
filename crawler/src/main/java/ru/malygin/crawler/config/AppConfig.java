package ru.malygin.crawler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.entity.Page;
import ru.malygin.crawler.service.PageService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        map.put("Page", Page.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }

    @Bean
    public Queue msgQueue() {
        return new Queue("msg-queue", false, false, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("rpc-exchange");
    }

    @Bean
    public Binding msgBinding() {
        return BindingBuilder
                .bind(msgQueue())
                .to(exchange())
                .with("msg-queue");
    }

    @Bean
    public Server server(PageService ps,
                         RabbitTemplate r,
                         RabbitAdmin ra) {
        return new Server(ps, r, ra);
    }

    @RequiredArgsConstructor
    public static class Server {

        private final PageService pageService;
        private final RabbitTemplate rabbitTemplate;
        private final RabbitAdmin rabbitAdmin;

        @RabbitListener(queues = "msg-queue")
        public Long receivePage(Map<String, String> map) {
            try {
                Long siteId = Long.parseLong(map.get("siteId"));
                Long appUserId = Long.parseLong(map.get("appUserId"));
                String pageQueue = map.get("pageQueue");
                return pageService
                        .getCountBySiteIdAndAppUserId(siteId, appUserId)
                        .doOnSuccess(sink -> sendPages(pageQueue, siteId, appUserId))
                        .block();
            } catch (NumberFormatException e) {
                log.info(e.getMessage());
                return -1L;
            }
        }

        public void sendPages(String pageQueue,
                              Long siteId,
                              Long appUserId) {
            rabbitAdmin.declareQueue(new Queue(pageQueue, false, false, true));
            pageService
                    .findAllBySiteIdAndAppUserId(siteId, appUserId)
                    .subscribe(page -> {
                        //log.info("Send page: {}", page.getPath());
                        rabbitTemplate.convertAndSend(pageQueue, page);
                    });

        }
    }
}
