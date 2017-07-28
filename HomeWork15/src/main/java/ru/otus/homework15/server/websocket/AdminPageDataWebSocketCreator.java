package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.MessageSystem;

import java.util.HashSet;
import java.util.Set;

public class AdminPageDataWebSocketCreator implements WebSocketCreator {
    private Set<AdminPageDataWebSocket> connectedClients;
    private final MessageSystem messageSystem;
    private final Address dbServiceAddress;

    public AdminPageDataWebSocketCreator(MessageSystem messageSystem, Address dbServiceAddress) {
        this.messageSystem = messageSystem;
        this.dbServiceAddress = dbServiceAddress;

        this.connectedClients = new HashSet<>();
        System.out.println("WebSocketCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        AdminPageDataWebSocket socket = new AdminPageDataWebSocket(connectedClients, messageSystem, dbServiceAddress);
        System.out.println("Socket created");
        return socket;
    }
}
