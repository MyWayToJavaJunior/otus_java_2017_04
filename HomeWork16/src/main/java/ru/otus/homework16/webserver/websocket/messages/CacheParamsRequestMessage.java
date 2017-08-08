package ru.otus.homework16.webserver.websocket.messages;

import ru.otus.homework16.common.db.IDatabaseService;
import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.MessageChannel;

public class CacheParamsRequestMessage extends Message {
    private final Address innerSender;

    public CacheParamsRequestMessage(Address receiver, Address sender, Address innerSender) {
        super(receiver, sender);
        this.innerSender = innerSender;
    }

    @Override
    public void onDeliver(MessageChannel channel, IMessageReceiver receiver) {
        if (receiver instanceof IDatabaseService) {
            onDeliver(channel, (IDatabaseService)receiver);
        }
    }

    public void onDeliver(MessageChannel channel, IDatabaseService service) {
        String json = service.getCache().toJSONString();
        Message message = new CacheParamsResponseMessage(json, getSender(), innerSender, getReceiver());
        channel.send(message);
    }
}
