package ru.otus.homework16.message.system;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.message.system.base.IMessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientChannel implements IMessageChannel {
    private static final Logger logger = Logger.getLogger(SocketClientChannel.class.getName());
    private static final int WORKERS_COUNT = 2;
    private static final int DEFAULT_THREAD_SLEEP_TIME = 100;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;
    private final List<Runnable> shutdownRegistrations;

    public SocketClientChannel(Socket socket) {
        this.socket = socket;
        this.shutdownRegistrations = new CopyOnWriteArrayList<>();
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty() && !stringBuilder.toString().isEmpty()) {
                    String json = stringBuilder.toString();
                    Message msg = getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException | ParseException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }

    private Message getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Message.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Message) new Gson().fromJson(json, msgClass);
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (!socket.isClosed()) {
                Message msg = output.poll(); //blocks
                if (msg != null) {
                    String json = new Gson().toJson(msg);
                    out.println(json);
                    out.println(); //end of message
                }
                Thread.sleep(DEFAULT_THREAD_SLEEP_TIME);
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() {

        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }
}
