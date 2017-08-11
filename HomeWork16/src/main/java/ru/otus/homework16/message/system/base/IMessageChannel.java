package ru.otus.homework16.message.system.base;

import java.io.IOException;


public interface IMessageChannel {
    void send(Message msg);

    Message pool();

    Message take() throws InterruptedException;

    void close() throws IOException;
}
