package ru.otus.homework16.message.system.base;

public interface IMessageSystem {

    void addReciever(IMessageReceiver receiver);
    void removeReceiver(IMessageReceiver receiver);
    void sendMessage(Message message);

    void start();
    void stop();
}
