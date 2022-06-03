package ru.malygin.helper.service.senders;

public interface LogSender {
    void info(String s,
              Object... o);

    void error(String s,
               Object... o);
}
