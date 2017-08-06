package ru.otus.homework15.server.websocket.messages;

import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.base.IRequestService;
import ru.otus.homework15.message.system.base.Message;
import ru.otus.homework15.message.system.base.IMessageReceiver;

public class CacheParamsResponseMessage extends Message {
    private String response;
    private final Address innerReceiver;


    public CacheParamsResponseMessage(String response, Address receiver, Address innerReceiver, Address sender) {
        super(receiver, sender);
        this.response = response;
        this.innerReceiver = innerReceiver;
    }

    @Override
    public void onDeliver(IMessageReceiver receiver) {
        if (receiver instanceof IRequestService){
            onDeliver((IRequestService)receiver);
        }
    }

    public void onDeliver(IRequestService receiver) {
        receiver.processResponse(innerReceiver, response);

    }
}
