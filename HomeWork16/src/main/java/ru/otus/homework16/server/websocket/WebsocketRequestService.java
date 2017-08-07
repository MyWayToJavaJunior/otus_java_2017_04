package ru.otus.homework16.server.websocket;

import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.MessageSystemContext;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.IRequestService;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.server.websocket.messages.CacheParamsRequestMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketRequestService implements IMessageReceiver, IRequestService {

    private final Address address;
    private final MessageSystemContext messageSystemContext;
    private final Map<Address, AdminPageDataWebSocket> innerReceivers;

    public WebsocketRequestService(Address address, MessageSystemContext messageSystemContext) {
        this.address = address;
        this.messageSystemContext = messageSystemContext;
        innerReceivers = new ConcurrentHashMap<>();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void addInnerReciever(IMessageReceiver innerReceiver) {
        if (innerReceiver instanceof AdminPageDataWebSocket) {
            innerReceivers.put(innerReceiver.getAddress(), (AdminPageDataWebSocket) innerReceiver);
        }
        else {
            throw new IllegalArgumentException();
        }

    }

    @Override
    public void removeInnerReceiver(Address innerReceiverAddress) {
        innerReceivers.remove(innerReceiverAddress);
    }

    @Override
    public void sendRequest(Address requesterAddress) {
        Message message = new CacheParamsRequestMessage(messageSystemContext.getMessageSystem(), messageSystemContext.getDbServiceAddress(), address, requesterAddress);
        messageSystemContext.getMessageSystem().sendMessage(message);
    }

    @Override
    public void processResponse(Address innerRecieverAddress, String response) {
        AdminPageDataWebSocket innerReceiver = innerReceivers.get(innerRecieverAddress);
        if (innerReceiver != null) {
            innerReceiver.processResponse(response);
        }
    }
}
