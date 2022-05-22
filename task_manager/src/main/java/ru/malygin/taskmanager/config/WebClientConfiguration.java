package ru.malygin.taskmanager.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient getWebClient() {
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpClient getHttpClient() {
        return HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                                       conn
                                               .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                               .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
    }

//    public Flux<String> pingServices() {
//        return Flux
//                .fromIterable(this.servicesConfig
//                                      .getServices()
//                                      .entrySet())
//                .flatMap(entry -> {
//                    String serviceId = entry.getKey();
//                    ServicesConfig.ServiceConfig config = entry.getValue();
//                    return getWebClient()
//                            .get()
//                            .uri(uriBuilder -> uriBuilder
//                                    .scheme(config.getScheme())
//                                    .host(config.getHost())
//                                    .port(config.getPort())
//                                    .path(config.getStatus())
//                                    .build())
//                            .retrieve()
//                            .bodyToMono(String.class)
//                            .onErrorReturn(serviceId + " unavailable");
//                });
//    }
}
