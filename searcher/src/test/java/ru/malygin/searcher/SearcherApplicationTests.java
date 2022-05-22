package ru.malygin.searcher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class SearcherApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testUrlParse() {
        try {
           // URL url = new URL("http://localhost:8001/api/v1/page?sitePath=https://playback.ru&initiator=1&error=false&token=token");
            URL url = new URL("http://localhost:8001/api/v1/page");
            log.info("getAuthority - {}", url.getAuthority());
            log.info("path - {}",url.getPath() );

            String query = url.getQuery();
            if (query != null){
                Map<String, String> tmp = new HashMap<>();
                String[] queryParam = url.getQuery().split("&");
                Arrays.stream(queryParam).forEach(param -> {
                    String[] m = param.split("=");
                    if (m.length == 2) {
                        tmp.put(m[0],m[1]);
                    }
                });

                tmp.forEach((k, v) -> log.info("{} = {}", k,v));
            }
        } catch (IOException e) {
            log.info("parse error");
        }
    }

}
