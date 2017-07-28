package ru.otus.homework15.message.system;

import ru.otus.homework15.common.db.IDatabaseService;

public class CacheParamsRequestMessage extends Message{
    private MessageSystem messageSystem;

    public CacheParamsRequestMessage(MessageSystem messageSystem, Address receiver, Address sender) {
        super(receiver, sender);
        this.messageSystem = messageSystem;
    }

    @Override
    public void onDeliver(MessageSystemMember receiver) {
        if (receiver instanceof IDatabaseService) {
            onDeliver((IDatabaseService)receiver);
        }
    }

    public void onDeliver(IDatabaseService service) {
        String json = service.getCache().toJSONString();
        Message message = new CacheParamsResponseMessage(json, getSender(), getReceiver());
        messageSystem.sendMessage(message);
    }
}
