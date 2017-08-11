package ru.otus.homework16.webserver.websocket.messages;

import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.IRequestService;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.IMessageChannel;

public class CacheParamsResponseMessage extends Message {
    private String response;
    private final Address innerReceiver;


    public CacheParamsResponseMessage(String response, Address receiver, Address innerReceiver, Address sender) {
        super(receiver, sender);
        this.response = response;
        this.innerReceiver = innerReceiver;
    }

    @Override
    public void onDeliver(IMessageChannel channel, IMessageReceiver receiver) {
        if (receiver instanceof IRequestService){
            onDeliver(channel, (IRequestService)receiver);
        }
    }

    public void onDeliver(IMessageChannel channel, IRequestService receiver) {
        receiver.processCacheParamsRequestResponse(innerReceiver, response);

    }
}
