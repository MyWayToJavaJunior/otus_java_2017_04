package ru.otus.homework15.message.system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSystem {
    public static final int DEFAULT_SLEEP_TIME = 10;
    private boolean started = false;

    private final Map<Address, MessageReceiver> receivers = new ConcurrentHashMap<>();
    private final Map<Address, MessageSender> senders = new ConcurrentHashMap<>();
    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesQueues = new ConcurrentHashMap<>();
    private final Map<Address, Thread> messagesProcessingThreads = new ConcurrentHashMap<>();

    public void addReciever(MessageReceiver receiver) {
        receivers.put(receiver.getAddress(), receiver);
        messagesQueues.put(receiver.getAddress(), new ConcurrentLinkedQueue<>());

        Thread t = new Thread(()-> {
            while (true) {
                try {
                    ConcurrentLinkedQueue<Message> receiverQueue = messagesQueues.get(receiver.getAddress());
                    while (!receiverQueue.isEmpty()) {
                        Message message = receiverQueue.poll();
                        message.onDeliver(receiver);
                    }
                    Thread.sleep(DEFAULT_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
                if (Thread.currentThread().isInterrupted()) break;
            }
        });
        t.setDaemon(true);
        messagesProcessingThreads.put(receiver.getAddress(), t);
        if (started) t.start();
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
        messagesQueues.remove(receiver.getAddress());
    }

    public void addSender(MessageSender sender) {
        senders.put(sender.getAddress(), sender);
    }

    public void removeSender(MessageSender sender) {
        senders.remove(sender.getAddress());
    }

    public void sendMessage(Message message) {
        ConcurrentLinkedQueue<Message> receiverQueue = messagesQueues.get(message.getReceiver());
        if (receiverQueue != null) receiverQueue.add(message);
    }

    public void stop() {
        for (Thread t : messagesProcessingThreads.values()) {
            stopThread(t);
        }
        started = false;
    }

    public void start() {
        for (Thread t : messagesProcessingThreads.values()) {
            t.start();
        }
        started = true;
    }

}
