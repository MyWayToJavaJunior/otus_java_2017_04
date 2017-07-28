package ru.otus.homework15.message.system;

public abstract class Message {
    private Address receiver;
    private Address sender;

    public Message(Address receiver, Address sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public Address getReceiver() {
        return receiver;
    }

    public Address getSender() {
        return sender;
    }

    public abstract void onDeliver(MessageSystemMember receiver);
}
