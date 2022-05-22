package ru.malygin.indexer.util.indexer;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.malygin.indexer.indexer.Indexer;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@SpringBootTest
@ExtendWith(SpringExtension.class)
class IndexerTest {

    private final ApplicationContext applicationContext;

    @Test
    public void givenIndexerScopeComponent() {
        Indexer indexerA = applicationContext.getBean(Indexer.Builder.class).build();
        Indexer indexerB = applicationContext.getBean(Indexer.Builder.class).build();

        assertNotNull(indexerA);
        assertNotNull(indexerB);
        assertNotEquals(indexerA, indexerB);
    }
}