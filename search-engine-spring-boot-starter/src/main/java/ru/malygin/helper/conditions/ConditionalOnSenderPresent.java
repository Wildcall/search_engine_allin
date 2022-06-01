package ru.malygin.helper.conditions;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class ConditionalOnSenderPresent extends AnyNestedCondition {

    public ConditionalOnSenderPresent() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "spring.search-engine.msg.log", name = "sender", havingValue = "true", matchIfMissing = true)
    static class LogSenderPresent {
    }

    @ConditionalOnProperty(prefix = "spring.search-engine.msg.notification", name = "sender", havingValue = "true")
    static class NotificationSenderPresent {
    }

    @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "sender", havingValue = "true")
    static class TaskSenderPresent {
    }
}
