package ru.otus.homework16.message.system.base;

import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.MessageChannel;

public abstract class Message {

    public static final String CLASS_NAME_VARIABLE = "className";

    private final Address receiver;
    private final Address sender;
    private final String className;

    public Message(Address receiver, Address sender) {
        this.receiver = receiver;
        this.sender = sender;
        this.className = this.getClass().getName();
    }

    public Address getReceiver() {
        return receiver;
    }

    public Address getSender() {
        return sender;
    }

    public String getClassName() {
        return className;
    }

    public abstract void onDeliver(MessageChannel channel, IMessageReceiver receiver);
}
