package ru.otus.homework16.server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.homework16.message.system.*;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.IRequestService;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class AdminPageDataWebSocket implements IMessageReceiver {
    private static final long CACHE_PARAMS_REQUEST_TIMER_INTERVAL = 1000;
    private Set<AdminPageDataWebSocket> connectedClients;
    private Session session;
    private IRequestService requestService;
    private Address address;
    private final Timer cacheParamsRequestTimer;

    private String lastResponse;

    public AdminPageDataWebSocket(Set<AdminPageDataWebSocket> connectedClients, IRequestService requestService) {
        this.connectedClients = connectedClients;
        this.requestService = requestService;
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
        requestService.addInnerReciever(this);

        cacheParamsRequestTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestService.sendRequest(getAddress());
            }
        }, CACHE_PARAMS_REQUEST_TIMER_INTERVAL, CACHE_PARAMS_REQUEST_TIMER_INTERVAL);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        cacheParamsRequestTimer.cancel();
        cacheParamsRequestTimer.purge();

        connectedClients.remove(this);

        requestService.removeInnerReceiver(this.getAddress());
    }

    @Override
    public Address getAddress() {
        return address;
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
