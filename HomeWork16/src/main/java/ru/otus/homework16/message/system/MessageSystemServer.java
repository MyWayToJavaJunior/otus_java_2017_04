package ru.otus.homework16.message.system;

import ru.otus.homework16.ServersConsts;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSystemServer implements IMessageReceiver{
    private static final Logger logger = Logger.getLogger(MessageSystemServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int PROCESS_MESSAGES_DELAY = 100;

    private final Address address;

    private final ExecutorService executor;
    private final List<MessageChannel> channels;
    private final Map<Address, MessageChannel> receivers;

    public MessageSystemServer(Address address) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channels = new CopyOnWriteArrayList<>();
        receivers = new ConcurrentHashMap<>();
        this.address = address;
    }

    public void start() throws Exception {

        executor.submit(this::processMessages);

        try (ServerSocket serverSocket = new ServerSocket(ServersConsts.MESSAGE_SYSTEM_SERVER_PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                channel.addShutdownRegistration(() -> channels.remove(channel));
                channels.add(channel);
            }
        }
    }

    public void registerReceiver(MessageChannel channel, RegisterMessage message) {
        if (message.isRegister()) {
            logger.info("registerReceiver: " + message.getSender());
            receivers.put(message.getSender(), channel);
        }
        else if (channel.equals(receivers.get(message.getSender()))) {
            try {
                channel.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void processMessages() {
        while (true) {
            for (MessageChannel channel : channels) {
                Message msg = channel.pool(); //get
                if (msg != null) {
                    logger.info("Message received");
                    if (msg instanceof RegisterMessage) {
                        msg.onDeliver(channel, this);
                    } else {
                        MessageChannel receiverChannel = receivers.get(msg.getReceiver());
                        if (receiverChannel != null) {
                            logger.info("Message sended to " + msg.getReceiver());
                            receiverChannel.send(msg);
                        }
                    }
                }
            }
            try {
                Thread.sleep(PROCESS_MESSAGES_DELAY);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
