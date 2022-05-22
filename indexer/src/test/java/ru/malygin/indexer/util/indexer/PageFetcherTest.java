package ru.malygin.indexer.util.indexer;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PageFetcherTest {

    private final ApplicationContext applicationContext;

//    @Test
//    public void givenPageFetcherScopeComponent() {
//        Fetcher.FetcherBuilder fetcherBuilder = applicationContext.getBean(Fetcher.FetcherBuilder.class);
//        Fetcher fetcherA = fetcherBuilder
//                .sitePath("pageFetcherA").build();
//        Fetcher fetcherB = fetcherBuilder
//                .sitePath("pageFetcherB").build();
//
//        assertNotNull(fetcherA);
//        assertNotNull(fetcherB);
//        assertNotEquals(fetcherA, fetcherB);
//    }

    @Test
    public void givenWebClientScopeNotEquals() {
        WebClient webClientA = WebClient.builder().baseUrl("A").build();
        WebClient webClientB = WebClient.builder().baseUrl("B").build();

        assertNotNull(webClientA);
        assertNotNull(webClientB);
        assertNotEquals(webClientA, webClientB);
    }

}