package ru.malygin.helper.conditions;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class ConditionalOnReceiverPresent extends AnyNestedCondition {

    public ConditionalOnReceiverPresent() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "queue", havingValue = "true")
    static class TaskReceiverPresent {
    }
}
