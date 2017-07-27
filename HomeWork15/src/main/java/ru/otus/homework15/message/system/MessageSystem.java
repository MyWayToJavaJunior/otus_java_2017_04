package ru.otus.homework15.message.system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageSystem {
    public static final int DEFAULT_SLEEP_TIME = 10;
    private final Map<Address, MessageReceiver> receivers = new ConcurrentHashMap<>();
    private final Map<Address, MessageSender> senders = new ConcurrentHashMap<>();
    private final Map<Address, Thread> messagesProcessingThreads = new ConcurrentHashMap<>();

    public void addReciever(MessageReceiver receiver) {
        receivers.put(receiver.getAddress(), receiver);
        messagesProcessingThreads.put(receiver.getAddress(), new Thread(()-> {
            while (true) {
                try {
                    receiver.processMessages();
                    Thread.sleep(DEFAULT_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
                if (Thread.currentThread().isInterrupted()) break;
            }
        })).setDaemon(true);
    }

    private void stopThread(Thread t) {
        if (t == null) return;

        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }

    public void removeReceiver(MessageReceiver receiver) {
        Thread t = messagesProcessingThreads.get(receiver.getAddress());
        stopThread(t);
        receivers.remove(receiver.getAddress());
    }

    public void addSender(MessageSender sender) {
        senders.put(sender.getAddress(), sender);
    }

    public void removeSender(MessageSender sender) {
        senders.remove(sender.getAddress());
    }

    public void sendMessage(Message message) {
        MessageReceiver receiver = receivers.get(message.getReceiver());
        if (receiver != null) receiver.receive(message);
    }

    public void stop() {
        for (Thread t : messagesProcessingThreads.values()) {
            stopThread(t);
        }
    }

    public void start() {
        for (Thread t : messagesProcessingThreads.values()) {
            t.start();
        }
    }

}
