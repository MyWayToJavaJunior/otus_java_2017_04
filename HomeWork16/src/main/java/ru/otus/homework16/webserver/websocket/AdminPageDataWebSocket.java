package ru.otus.homework16.webserver.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private static final String VARIABLE_MAXIMAL_LIFE_TIME = "maximalLifeTime";
    private static final String VARIABLE_MAXIMAL_IDLE_TIME = "maximalIdleTime";
    private static final String VARIABLE_MAXIMAL_SIZE = "maximalSize";
    private static final String VARIABLE_COMMAND = "command";
    private static final String COMMAND_CHANGE_CACHE_PARAMETERS_VALUES = "changeCacheParametersValues";
    private static final String COMMAND_SUBSCRIBE = "subscribe";
    private static final String VARIABLE_NEW_DB_SERVICE_ADDRESS_ID = "newDBServiceAddressID";


    private Set<AdminPageDataWebSocket> connectedClients;
    private Session session;
    private IRequestService requestService;
    private Address address;
    private Address dbServiceAddress;

    private final Timer cacheParamsRequestTimer;

    private String lastResponse;

    public AdminPageDataWebSocket(Set<AdminPageDataWebSocket> connectedClients, IRequestService requestService) {
        this.connectedClients = connectedClients;
        this.requestService = requestService;
        cacheParamsRequestTimer = new Timer(true);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(data).getAsJsonObject();
        String command = object.get(VARIABLE_COMMAND).getAsString();

        if (command.equals(COMMAND_CHANGE_CACHE_PARAMETERS_VALUES)) {
            long maximalLifeTime = object.get(VARIABLE_MAXIMAL_LIFE_TIME).getAsLong();
            long maximalIdleTime = object.get(VARIABLE_MAXIMAL_IDLE_TIME).getAsLong();
            int maximalSize = object.get(VARIABLE_MAXIMAL_SIZE).getAsInt();

            requestService.sendCacheParamsChangeRequest(getAddress(), maximalLifeTime, maximalIdleTime, maximalSize);
        }
        else if (command.equals(COMMAND_SUBSCRIBE)) {
            String newDBServiceAddressID = object.get(VARIABLE_NEW_DB_SERVICE_ADDRESS_ID).getAsString();
            dbServiceAddress = new Address(newDBServiceAddressID);
        }
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
                if (dbServiceAddress != null) {
                    requestService.sendCacheParamsRequest(getAddress());
                }
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

    public Address getDbServiceAddress() {
        return dbServiceAddress;
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
