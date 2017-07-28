package ru.otus.homework15.message.system;

import ru.otus.homework15.server.websocket.AdminPageDataWebSocket;

public class CacheParamsResponseMessage extends Message {
    private String response;

    public CacheParamsResponseMessage(String response, Address receiver, Address sender) {
        super(receiver, sender);
        this.response = response;
    }

    @Override
    public void onDeliver(MessageSystemMember receiver) {
        if (receiver instanceof AdminPageDataWebSocket){
            onDeliver((AdminPageDataWebSocket)receiver);
        }
    }

    public void onDeliver(AdminPageDataWebSocket receiver) {
        receiver.processResponse(response);
    }
}
