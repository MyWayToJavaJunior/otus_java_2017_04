package ru.otus.homework16.webserver.websocket.messages;

import ru.otus.homework16.common.db.IDatabaseService;
import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageChannel;

public class ChangeCacheParamsRequestMessage extends Message {
    private final Address innerSender;
    private final long maximalLifeTime;
    private final long maximalIdleTime;
    private final int maximalSize;

    public ChangeCacheParamsRequestMessage(Address receiver,
                                           Address sender,
                                           Address innerSender,
                                           long maximalLifeTime, long maximalIdleTime, int maximalSize) {
        super(receiver, sender);
        this.innerSender = innerSender;
        this.maximalLifeTime = maximalLifeTime;
        this.maximalIdleTime = maximalIdleTime;
        this.maximalSize = maximalSize;
    }

    @Override
    public void onDeliver(IMessageChannel channel, IMessageReceiver receiver) {
        if (receiver instanceof IDatabaseService) {
            onDeliver(channel, (IDatabaseService)receiver);
        }
    }

    public void onDeliver(IMessageChannel channel, IDatabaseService service) {
        service.getCache().setMaximalLifeTime(maximalLifeTime );
        service.getCache().setMaximalIdleTime(maximalIdleTime);
        service.getCache().setMaximalSize(maximalSize);

        String json = service.getCache().toJSONString();
        Message message = new CacheParamsResponseMessage(json, getSender(), innerSender, getReceiver());
        channel.send(message);
    }
}
