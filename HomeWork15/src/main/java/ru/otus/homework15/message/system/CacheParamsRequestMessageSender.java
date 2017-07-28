package ru.otus.homework15.message.system;

public class CacheParamsRequestMessageSender implements MessageSender{
    private Address address;
    private Address dbServiceAddress;
    private final MessageSystem messageSystem;

    public CacheParamsRequestMessageSender(Address address, Address dbServiceAddress, MessageSystem messageSystem) {
        this.address = address;
        this.dbServiceAddress = dbServiceAddress;
        this.messageSystem = messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void processReply(Message reply) {

    }

    public void sendCacheParamsRequestMessage() {
        Message message = new CacheParamsRequestMessage(dbServiceAddress, address);
        messageSystem.sendMessage(message);
    }

}
