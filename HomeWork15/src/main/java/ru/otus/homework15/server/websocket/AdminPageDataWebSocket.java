package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.homework15.message.system.*;

import java.io.IOException;
import java.util.Set;

@WebSocket
public class AdminPageDataWebSocket implements MessageSystemMember{
    private Set<AdminPageDataWebSocket> connectedClients;
    private Session session;
    private MessageSystem messageSystem;
    private Address address;
    private Address dbServiceAddress;

    public AdminPageDataWebSocket(Set<AdminPageDataWebSocket> connectedClients, MessageSystem messageSystem, Address dbServiceAddress) {
        this.connectedClients = connectedClients;
        this.messageSystem = messageSystem;
        this.dbServiceAddress = dbServiceAddress;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.println("onMessage");
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        connectedClients.add(this);
        setSession(session);

        address = new Address(this.toString());
        messageSystem.addReciever(this);

        sendCacheParamsRequest();

        System.out.println("onSocketOpen");
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

        messageSystem.removeReceiver(this);

        System.out.println("onSocketClose");
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void sendCacheParamsRequest() {
        Message message = new CacheParamsRequestMessage(messageSystem, dbServiceAddress, address);
        messageSystem.sendMessage(message);

    }

    public void processResponse(String response) {
        try {
            session.getRemote().sendString(response);
        } catch (IOException e) {
        }
    }
}
