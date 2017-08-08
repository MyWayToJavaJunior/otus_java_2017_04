package ru.otus.homework16;


import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.MessageSystemServer;

public class MessageSystemServerMain {


    public static void main(String[] args) {
        MessageSystemServer server = new MessageSystemServer(new Address(ServersConsts.MESSAGE_SYSTEM_SERVER_ADDRESS_01));
        try {
            server.start();
        } catch (Exception e) {
        }
    }
}
