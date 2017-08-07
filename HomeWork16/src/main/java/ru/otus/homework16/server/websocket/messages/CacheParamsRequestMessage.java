package ru.otus.homework16.server.websocket.messages;

import ru.otus.homework16.common.db.IDatabaseService;
import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.MessageSystem;
import ru.otus.homework16.message.system.base.IMessageReceiver;

public class CacheParamsRequestMessage extends Message {
    private MessageSystem messageSystem;
    private final Address innerSender;

    public CacheParamsRequestMessage(MessageSystem messageSystem, Address receiver, Address sender, Address innerSender) {
        super(receiver, sender);
        this.messageSystem = messageSystem;
        this.innerSender = innerSender;
    }

    @Override
    public void onDeliver(IMessageReceiver receiver) {
        if (receiver instanceof IDatabaseService) {
            onDeliver((IDatabaseService)receiver);
        }
    }

    public void onDeliver(IDatabaseService service) {
        String json = service.getCache().toJSONString();
        Message message = new CacheParamsResponseMessage(json, getSender(), innerSender, getReceiver());
        messageSystem.sendMessage(message);
    }
}
