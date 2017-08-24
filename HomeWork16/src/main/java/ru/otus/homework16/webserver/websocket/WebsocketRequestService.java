package ru.otus.homework16.webserver.websocket;

import ru.otus.homework16.ServersConsts;
import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.IRequestService;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.RegisterMessage;
import ru.otus.homework16.message.system.SocketClientChannel;
import ru.otus.homework16.webserver.websocket.messages.CacheParamsRequestMessage;
import ru.otus.homework16.webserver.websocket.messages.ChangeCacheParamsRequestMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class WebsocketRequestService implements IMessageReceiver, IRequestService {
    private static final Logger logger = Logger.getLogger(WebsocketRequestService.class.getName());

    private final Address address;

    private final Map<Address, AdminPageDataWebSocket> innerReceivers;
    private SocketClientChannel clientChannel;

    public WebsocketRequestService(Address address) {
        this.address = address;
        innerReceivers = new ConcurrentHashMap<>();
    }

    public void init() {
        try {
            clientChannel = new SocketClientChannel(new Socket(ServersConsts.MESSAGE_SYSTEM_SERVER_HOST, ServersConsts.MESSAGE_SYSTEM_SERVER_PORT));
            clientChannel.init();

            Message registerMessage = new RegisterMessage(null, getAddress(), true);
            clientChannel.send(registerMessage);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                while (!executor.isShutdown()) {
                    try {
                        Message message = clientChannel.take();
                        message.onDeliver(clientChannel, this);
                    } catch (InterruptedException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });

        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
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
    public void sendCacheParamsRequest(Address requesterAddress) {
        AdminPageDataWebSocket ds = innerReceivers.get(requesterAddress);
        if (ds != null) {
            Address dbServiceAddress = ds.getDbServiceAddress();
            Message message = new CacheParamsRequestMessage(dbServiceAddress, address, requesterAddress);
            clientChannel.send(message);
        }
    }

    @Override
    public void processCacheParamsRequestResponse(Address innerRecieverAddress, String response) {
        AdminPageDataWebSocket innerReceiver = innerReceivers.get(innerRecieverAddress);
        if (innerReceiver != null) {
            innerReceiver.processResponse(response);
        }
    }

    @Override
    public void sendCacheParamsChangeRequest(Address requesterAddress, long maximalLifeTime, long maximalIdleTime, int maximalSize) {
        AdminPageDataWebSocket ds = innerReceivers.get(requesterAddress);
        if (ds != null) {
            Address dbServiceAddress = ds.getDbServiceAddress();
            Message message = new ChangeCacheParamsRequestMessage(dbServiceAddress, address, requesterAddress, maximalLifeTime, maximalIdleTime, maximalSize);
            clientChannel.send(message);
        }
    }

}
