package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Set;

@WebSocket
public class AdminPageDataWebSocket {
    private Set<AdminPageDataWebSocket> connectedClients;
    private Session session;

    public AdminPageDataWebSocket(Set<AdminPageDataWebSocket> connectedClients) {
        this.connectedClients = connectedClients;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        for (AdminPageDataWebSocket client : connectedClients) {
            try {
                //client.getSession().getRemote().sendString(data);
                System.out.println("Sending message: " + data);
            } catch (Exception e) {
                System.out.print(e.toString());
            }
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        connectedClients.add(this);
        setSession(session);
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
        System.out.println("onSocketClose");
    }
}
