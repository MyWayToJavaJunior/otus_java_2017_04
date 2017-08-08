package ru.otus.homework16.webserver.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.homework16.message.system.base.IRequestService;

import java.util.HashSet;
import java.util.Set;

public class AdminPageDataWebSocketCreator implements WebSocketCreator {
    private Set<AdminPageDataWebSocket> connectedClients;
    private final IRequestService requestService;

    public AdminPageDataWebSocketCreator(IRequestService requestService) {
        this.requestService = requestService;

        this.connectedClients = new HashSet<>();
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        AdminPageDataWebSocket socket = new AdminPageDataWebSocket(connectedClients, requestService);
        return socket;
    }
}
