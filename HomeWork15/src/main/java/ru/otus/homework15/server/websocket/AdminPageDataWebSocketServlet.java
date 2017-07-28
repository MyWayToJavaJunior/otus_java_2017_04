package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.MessageSystem;
import ru.otus.homework15.message.system.MessageSystemContext;

/**
 * This class represents a servlet starting a webSocket application
 */
public class AdminPageDataWebSocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final MessageSystemContext messageSystemContext;

    public AdminPageDataWebSocketServlet(MessageSystemContext messageSystemContext) {
        this.messageSystemContext = messageSystemContext;
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new AdminPageDataWebSocketCreator(messageSystemContext));
    }
}
