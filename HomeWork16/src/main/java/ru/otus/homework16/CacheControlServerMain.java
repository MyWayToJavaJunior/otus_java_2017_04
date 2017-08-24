package ru.otus.homework16;

import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.webserver.CacheControlServer;
import ru.otus.homework16.webserver.websocket.WebsocketRequestService;

public class CacheControlServerMain {
    public static void main(String[] args) {

        WebsocketRequestService requestService = new WebsocketRequestService(new Address(ServersConsts.CACHE_CONTROL_SERVER_ADDRESS_01));
        requestService.init();

        CacheControlServer server = new CacheControlServer(requestService);
        try {
            server.startServer();
            server.join();
        } catch (Exception e) {

        }

    }
}
