package ru.otus.homework15.server.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.MessageSystem;

/**
 * This class represents a servlet starting a webSocket application
 */
public class AdminPageDataWebSocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final MessageSystem messageSystem;
    private final Address dbServiceAddress;

    public AdminPageDataWebSocketServlet(MessageSystem messageSystem, Address dbServiceAddress) {
        this.messageSystem = messageSystem;
        this.dbServiceAddress = dbServiceAddress;
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new AdminPageDataWebSocketCreator(messageSystem, dbServiceAddress));
    }
}
