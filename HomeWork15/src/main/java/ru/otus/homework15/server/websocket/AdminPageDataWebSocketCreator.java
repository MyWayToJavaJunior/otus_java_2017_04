package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.HashSet;
import java.util.Set;

public class AdminPageDataWebSocketCreator implements WebSocketCreator {
    private Set<AdminPageDataWebSocket> connectedClients;

    public AdminPageDataWebSocketCreator() {
        this.connectedClients = new HashSet<>();
        System.out.println("WebSocketCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        AdminPageDataWebSocket socket = new AdminPageDataWebSocket(connectedClients);
        System.out.println("Socket created");
        return socket;
    }
}
