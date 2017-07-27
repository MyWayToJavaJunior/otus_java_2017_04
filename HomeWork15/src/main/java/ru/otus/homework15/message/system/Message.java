package ru.otus.homework15.message.system;

public interface Message {
    Address getReceiver();
    Address getSender();
}
