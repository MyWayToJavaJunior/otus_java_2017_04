package ru.otus.homework15.message.system;

public class MessageSystemContext {
    private final MessageSystem messageSystem;
    private final Address dbServiceAddress;

    public MessageSystemContext(MessageSystem messageSystem, Address dbServiceAddress) {
        this.messageSystem = messageSystem;
        this.dbServiceAddress = dbServiceAddress;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getDbServiceAddress() {
        return dbServiceAddress;
    }
}
