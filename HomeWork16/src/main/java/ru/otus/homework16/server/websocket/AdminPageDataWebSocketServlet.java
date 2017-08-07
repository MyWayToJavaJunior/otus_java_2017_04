package ru.otus.homework16.server.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.homework16.message.system.base.IRequestService;

public class AdminPageDataWebSocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final IRequestService requestService;

    public AdminPageDataWebSocketServlet(WebsocketRequestService requestService) {
        this.requestService = requestService;
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new AdminPageDataWebSocketCreator(requestService));
    }
}
