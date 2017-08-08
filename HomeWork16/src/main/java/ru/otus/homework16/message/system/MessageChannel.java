package ru.otus.homework16.message.system;

import ru.otus.homework16.message.system.base.Message;

import java.io.IOException;


public interface MessageChannel {
    void send(Message msg);

    Message pool();

    Message take() throws InterruptedException;

    void close() throws IOException;
}
