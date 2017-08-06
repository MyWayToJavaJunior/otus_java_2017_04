package ru.otus.homework15.message.system.base;

public interface IMessageSystem {

    void addReciever(IMessageReceiver receiver);
    void removeReceiver(IMessageReceiver receiver);
    void sendMessage(Message message);

    void start();
    void stop();
}
