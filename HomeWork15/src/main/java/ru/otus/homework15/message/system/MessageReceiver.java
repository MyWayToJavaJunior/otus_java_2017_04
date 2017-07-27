package ru.otus.homework15.message.system;

public interface MessageReceiver extends MessageSystemMember{
    void receive(Message message);
    void processMessages();
}
