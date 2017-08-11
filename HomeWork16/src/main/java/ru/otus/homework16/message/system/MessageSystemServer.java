package ru.otus.homework16.message.system;

import ru.otus.homework16.ServersConsts;
import ru.otus.homework16.message.system.base.IAddressableMessageChannel;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageChannel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
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
    private final List<IAddressableMessageChannel> channels;

    public MessageSystemServer(Address address) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channels = new CopyOnWriteArrayList<>();
        this.address = address;
    }

    public void start() throws Exception {

        executor.submit(this::processMessages);

        try (ServerSocket serverSocket = new ServerSocket(ServersConsts.MESSAGE_SYSTEM_SERVER_PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();

                SocketClientChannel socketClientChannel = new SocketClientChannel(client);
                IAddressableMessageChannel addressableMessageChannel = new AddressableMessageChannel(socketClientChannel);
                channels.add(addressableMessageChannel);

                socketClientChannel.init();
                socketClientChannel.addShutdownRegistration(() -> channels.remove(addressableMessageChannel));
            }
        }
    }

    private IAddressableMessageChannel findChannel(Address address) {
        for (IAddressableMessageChannel receiverChannel : channels) {
            Address receiverChannelAddress = receiverChannel.getAddress();
            if (receiverChannelAddress != null && receiverChannelAddress.equals(address)) {
                return receiverChannel;
            }
        }
        return null;
    }

    public void registerReceiver(IAddressableMessageChannel channel, RegisterMessage message) {
        if (message == null || channel == null) return;

        IAddressableMessageChannel existingChannel = findChannel(message.getSender());

        if (message.isRegister() && existingChannel == null) {
            logger.info("registerReceiver: " + message.getSender());
            channel.setAddress(message.getSender());
        }
        else if (existingChannel != null || channel.getAddress().equals(message.getSender())) {
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
            for (IMessageChannel channel : channels) {
                Message msg = channel.pool(); //get
                if (msg != null) {
                    logger.info("Message received");
                    if (msg instanceof RegisterMessage) {
                        msg.onDeliver(channel, this);
                    } else {
                        IAddressableMessageChannel receiverChannel = findChannel(msg.getReceiver());
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
