package ru.otus.homework15.message.system.base;

public interface IMessageSystem {

    void addReciever(IMessageSystemMember receiver);
    void removeReceiver(IMessageSystemMember receiver);
    void sendMessage(Message message);

    void start();
    void stop();
}
