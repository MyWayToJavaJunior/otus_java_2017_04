package ru.otus.homework16.message.system;

import ru.otus.homework16.message.system.base.IAddressableMessageChannel;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageChannel;

public class RegisterMessage extends Message{
    private final boolean register;

    public RegisterMessage(Address receiver, Address sender, boolean register) {
        super(receiver, sender);
        this.register = register;
    }

    @Override
    public void onDeliver(IMessageChannel channel, IMessageReceiver receiver) {
        if (receiver instanceof MessageSystemServer && channel instanceof IAddressableMessageChannel) {
            onDeliver((IAddressableMessageChannel)channel, (MessageSystemServer) receiver);
        }
    }

    public void onDeliver(IAddressableMessageChannel channel, MessageSystemServer server) {
        server.registerReceiver(channel, this);
    }

    public boolean isRegister() {
        return register;
    }
}
