package ru.otus.homework16.webserver;

import ru.otus.homework16.CacheControlServerMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRunner {
    private static final Logger logger = Logger.getLogger(ProcessRunner.class.getName());

    private static final String MESSAGE_SYSTEM_SERVER_RUN_COMMAND = "java -jar target/MessageSystemServer.jar";
    private static final String DB_SERVICE_RUN_COMMAND = "java -jar target/DBService.jar";
    private static final String CACHE_CONTROL_SERVER_RUN_COMMAND = "java -jar target/CacheControlServer.jar";

    private final StringBuffer out = new StringBuffer();
    private Process process;


    public void start(String command) throws IOException {
        logger.info(command);
        process = runProcess(command);
    }

    public void stop() {
        process.destroy();
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
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(type).append('>').append(line).append('\n');
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        ProcessRunner messageSystemServer = new ProcessRunner();
        ProcessRunner cacheControlServer = new ProcessRunner();
        ProcessRunner dbService = new ProcessRunner();
        try {
            messageSystemServer.start(MESSAGE_SYSTEM_SERVER_RUN_COMMAND);
            dbService.start(DB_SERVICE_RUN_COMMAND);
            cacheControlServer.start(CACHE_CONTROL_SERVER_RUN_COMMAND);

            System.out.println(cacheControlServer.getOutput());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
