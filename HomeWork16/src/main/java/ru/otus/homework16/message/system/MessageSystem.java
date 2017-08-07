package ru.otus.homework16.message.system;

import ru.otus.homework16.message.system.base.IMessageSystem;
import ru.otus.homework16.message.system.base.IMessageReceiver;
import ru.otus.homework16.message.system.base.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem implements IMessageSystem {
    public static final int DEFAULT_SLEEP_TIME = 10;
    private boolean started = false;

    private final Map<Address, MessageSystemReceiverContext> receiverContextMap = new ConcurrentHashMap<>();

    private Thread createReceiverThread(Address receiverAddress) {
        Thread t = new Thread(()-> {
            while (true) {
                try {
                    ConcurrentLinkedQueue<Message> receiverQueue = receiverContextMap.get(receiverAddress).getMessagesQueue();
                    while (!receiverQueue.isEmpty()) {
                        Message message = receiverQueue.poll();
                        message.onDeliver(receiverContextMap.get(receiverAddress).getReceiver());
                    }
                    Thread.sleep(DEFAULT_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
                if (Thread.currentThread().isInterrupted()) break;
            }
        });
        t.setDaemon(true);
        return t;
    }

    public void addReciever(IMessageReceiver receiver) {
        Thread t = createReceiverThread(receiver.getAddress());

        MessageSystemReceiverContext receiverContext = new MessageSystemReceiverContext(receiver, t);
        receiverContextMap.put(receiver.getAddress(), receiverContext);

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

    public void removeReceiver(IMessageReceiver receiver) {
        Thread t = receiverContextMap.get(receiver.getAddress()).getThread();
        stopThread(t);
        receiverContextMap.remove(receiver.getAddress());
    }

    public void sendMessage(Message message) {
        ConcurrentLinkedQueue<Message> receiverQueue = receiverContextMap.get(message.getReceiver()).getMessagesQueue();
        if (receiverQueue != null) receiverQueue.add(message);
    }

    public void stop() {
        for (Map.Entry<Address, MessageSystemReceiverContext> c : receiverContextMap.entrySet()) {
            stopThread(c.getValue().getThread());
        }
        started = false;
    }

    public void start() {
        for (Map.Entry<Address, MessageSystemReceiverContext> c : receiverContextMap.entrySet()) {
            c.getValue().getThread().start();
        }
        started = true;
    }

    private class MessageSystemReceiverContext {
        private final IMessageReceiver receiver;
        private final ConcurrentLinkedQueue<Message> messagesQueue;
        private final Thread thread;

        public MessageSystemReceiverContext(IMessageReceiver receiver, Thread thread) {
            this.receiver = receiver;
            this.messagesQueue = new ConcurrentLinkedQueue<>();
            this.thread = thread;
        }

        public IMessageReceiver getReceiver() {
            return receiver;
        }

        public ConcurrentLinkedQueue<Message> getMessagesQueue() {
            return messagesQueue;
        }

        public Thread getThread() {
            return thread;
        }
    }

}
