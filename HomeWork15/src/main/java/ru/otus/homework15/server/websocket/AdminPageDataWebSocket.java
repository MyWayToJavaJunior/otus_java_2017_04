package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.homework15.message.system.*;
import ru.otus.homework15.message.system.base.IMessageSystemMember;
import ru.otus.homework15.message.system.base.Message;
import ru.otus.homework15.server.websocket.messages.CacheParamsRequestMessage;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class AdminPageDataWebSocket implements IMessageSystemMember {
    private static final long CACHE_PARAMS_REQUEST_TIMER_INTERVAL = 1000;
    private Set<AdminPageDataWebSocket> connectedClients;
    private Session session;
    private MessageSystemContext messageSystemContext;
    private Address address;
    private final Timer cacheParamsRequestTimer;

    private String lastResponse;

    public AdminPageDataWebSocket(Set<AdminPageDataWebSocket> connectedClients, MessageSystemContext messageSystemContext) {
        this.connectedClients = connectedClients;
        this.messageSystemContext = messageSystemContext;
        cacheParamsRequestTimer = new Timer(true);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        connectedClients.add(this);
        setSession(session);

        address = new Address(this.toString());
        cacheParamsRequestTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendCacheParamsRequest();
            }
        }, CACHE_PARAMS_REQUEST_TIMER_INTERVAL, CACHE_PARAMS_REQUEST_TIMER_INTERVAL);

        messageSystemContext.getMessageSystem().addReciever(this);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        connectedClients.remove(this);

        messageSystemContext.getMessageSystem().removeReceiver(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void sendCacheParamsRequest() {
        Message message = new CacheParamsRequestMessage(messageSystemContext.getMessageSystem(), messageSystemContext.getDbServiceAddress(), address);
        messageSystemContext.getMessageSystem().sendMessage(message);
    }

    public void processResponse(String response) {
        try {
            if (lastResponse == null || !lastResponse.equals(response)) {
                session.getRemote().sendString(response);
                lastResponse = response;
            }
        } catch (IOException e) {
        }
    }
}
