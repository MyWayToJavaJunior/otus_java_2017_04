package ru.otus.homework16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private static final String MESSAGE_SYSTEM_SERVER_RUN_COMMAND = "java -jar target/MessageSystemServer.jar";
    private static final String DB_SERVICE_RUN_COMMAND = "java -jar target/DBService.jar";
    private static final String CACHE_CONTROL_SERVER_RUN_COMMAND = "java -jar target/CacheControlServer.jar";
    private static final int SLEEP_INTERVAL = 1000;

    private final StringBuffer out = new StringBuffer();
    private Process process;


    public void start(String command) throws IOException {
        process = runProcess(command);
    }

    public void stop() {
        process.destroy();
    }

    public void waitFor() {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getOutput() {
        return out.toString();
    }




    private Process runProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StreamListener errors = new StreamListener(p.getErrorStream(), "ERROR");
        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");

        output.start();
        errors.start();
        return p;
    }

    private class StreamListener extends Thread {
        private final Logger logger = Logger.getLogger(StreamListener.class.getName());

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    builder.setLength(0);
                    builder.append(type).append('>').append(line).append('\n');
                    out.append(builder.toString());
                    if (type.equals("ERROR")) {
                        System.err.println(line);
                    }
                    else {
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    private static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
        }
    }


    public static void main(String[] args) {
        Launcher messageSystemServer = new Launcher();
        Launcher cacheControlServer = new Launcher();
        Launcher dbService = new Launcher();
        try {
            messageSystemServer.start(MESSAGE_SYSTEM_SERVER_RUN_COMMAND);
            sleep(SLEEP_INTERVAL);

            dbService.start(DB_SERVICE_RUN_COMMAND);
            sleep(SLEEP_INTERVAL);

            cacheControlServer.start(CACHE_CONTROL_SERVER_RUN_COMMAND);

            messageSystemServer.waitFor();
            dbService.waitFor();
            cacheControlServer.waitFor();

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
