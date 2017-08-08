package ru.otus.homework16.message.system;

import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;

public class RegisterMessage extends Message{
    private final boolean register;

    public RegisterMessage(Address receiver, Address sender, boolean register) {
        super(receiver, sender);
        this.register = register;
    }

    @Override
    public void onDeliver(MessageChannel channel, IMessageReceiver receiver) {
        if (receiver instanceof MessageSystemServer) {
            onDeliver(channel, (MessageSystemServer) receiver);
        }
    }

    public void onDeliver(MessageChannel channel, MessageSystemServer server) {
        server.registerReceiver(channel, this);
    }

    public boolean isRegister() {
        return register;
    }
}
