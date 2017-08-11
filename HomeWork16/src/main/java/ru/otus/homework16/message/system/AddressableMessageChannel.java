package ru.otus.homework16.message.system;

import ru.otus.homework16.message.system.base.IAddressableMessageChannel;
import ru.otus.homework16.message.system.base.IMessageChannel;
import ru.otus.homework16.message.system.base.Message;

import java.io.IOException;

public class AddressableMessageChannel implements IAddressableMessageChannel {
    private final IMessageChannel channel;
    private Address address;


    public AddressableMessageChannel(IMessageChannel channel) {
        this.channel = channel;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void send(Message msg) {
        channel.send(msg);
    }

    @Override
    public Message pool() {
        return channel.pool();
    }

    @Override
    public Message take() throws InterruptedException {
        return channel.take();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
