package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.homework15.message.system.MessageSystemContext;

import java.util.HashSet;
import java.util.Set;

public class AdminPageDataWebSocketCreator implements WebSocketCreator {
    private Set<AdminPageDataWebSocket> connectedClients;
    private final MessageSystemContext messageSystemContext;

    public AdminPageDataWebSocketCreator(MessageSystemContext messageSystemContext) {
        this.messageSystemContext = messageSystemContext;

        this.connectedClients = new HashSet<>();
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        AdminPageDataWebSocket socket = new AdminPageDataWebSocket(connectedClients, messageSystemContext);
        return socket;
    }
}
