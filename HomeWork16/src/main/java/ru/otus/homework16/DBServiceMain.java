package ru.otus.homework16;

import ru.otus.homework16.common.db.datasets.AddressDataSet;
import ru.otus.homework16.common.db.DBSettings;
import ru.otus.homework16.common.db.datasets.PhoneDataSet;
import ru.otus.homework16.common.db.datasets.UserDataSet;
import ru.otus.homework16.common.db.DatabaseCreator;
import ru.otus.homework16.message.system.Address;
import ru.otus.homework16.message.system.base.Message;
import ru.otus.homework16.reflection.orm.ReflectionORMDatabaseService;
import ru.otus.homework16.message.system.RegisterMessage;
import ru.otus.homework16.message.system.SocketClientChannel;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DBServiceMain {

    private static final Logger logger = Logger.getLogger(DBServiceMain.class.getName());
    private static final String FIRST_USER_NAME = "Igor";
    private static final int FIRST_USER_AGE = 13;
    private static final long FIRST_USER_ID = 1L;
    private static final int FIRST_USER_NEW_AGE = 35;
    private static final String FIRST_USER_NEW_NAME = "Vasya";

    private static final String MSG_DATABASE_DROP_FAILED = "Database drop failed";
    private static final String MSG_DATABASE_CREATION_FAILED = "Database creation failed";
    private static final String FIRST_USER_STREET = "Trafalgar square";
    private static final int FIRST_USER_MAIL_INDEX = 17;
    private static final String MSG_USERS_TABLE_CREATION_FILED = "Users table creation filed";
    private static final int WRONG_USER_ID = 32;

    public static void main(String[] args) {
        DBSettings settings = DBSettings.getInstance();

        settings.loadFromDefaultXMLResourceFile();

        logger.info(settings.toString());

        if (!DatabaseCreator.dropHomework16Database(settings)) {
            logger.severe(MSG_DATABASE_DROP_FAILED);
        }

        if (!DatabaseCreator.createHomework16Database(settings)) {
            logger.severe(MSG_DATABASE_CREATION_FAILED);
        }

        if (!DatabaseCreator.createUsersTable(settings)) {
            logger.severe(MSG_USERS_TABLE_CREATION_FILED);
        }

        try (ReflectionORMDatabaseService service = new ReflectionORMDatabaseService(settings, new Address(ServersConsts.DB_SERVICE_ADDRESS_01))) {

            UserDataSet user = new UserDataSet(null, FIRST_USER_AGE, FIRST_USER_NAME);
            service.save(user);

            user = service.read(FIRST_USER_ID);
            user.setAge(FIRST_USER_NEW_AGE);
            user.setName(FIRST_USER_NEW_NAME);
            user.setAddress(new AddressDataSet(FIRST_USER_STREET, FIRST_USER_MAIL_INDEX));

            List<PhoneDataSet> phones = new ArrayList<>();
            phones.add(new PhoneDataSet(8452, "33-33-33"));
            phones.add(new PhoneDataSet(8453, "75-53-53"));
            user.setPhones(phones);

            service.save(user);

            UserDataSet firstUser = service.read(FIRST_USER_ID);

            service.read(FIRST_USER_ID);
            service.read(FIRST_USER_ID);
            service.read(WRONG_USER_ID);

            Socket socket = new Socket(ServersConsts.MESSAGE_SYSTEM_SERVER_HOST, ServersConsts.MESSAGE_SYSTEM_SERVER_PORT);
            SocketClientChannel clientChannel = new SocketClientChannel(socket);
            clientChannel.init();

            RegisterMessage registerMessage = new RegisterMessage(null, service.getAddress(), true);
            clientChannel.send(registerMessage);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                while (!executor.isShutdown()) {
                    try {
                        logger.info("Message received");
                        Message message = clientChannel.take();
                        message.onDeliver(clientChannel, service);
                    } catch (InterruptedException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
