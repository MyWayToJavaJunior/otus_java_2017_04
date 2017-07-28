package ru.otus.homework15;

import ru.otus.homework15.common.db.datasets.AddressDataSet;
import ru.otus.homework15.common.db.DBSettings;
import ru.otus.homework15.common.db.datasets.PhoneDataSet;
import ru.otus.homework15.common.db.datasets.UserDataSet;
import ru.otus.homework15.common.db.DatabaseCreator;
import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.MessageSystem;
import ru.otus.homework15.reflection.orm.ReflectionORMDatabaseService;
import ru.otus.homework15.server.CacheControlServer;

import java.util.ArrayList;
import java.util.List;

public class Main {

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

        //Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        DBSettings settings = DBSettings.getInstance();

        String settingsXMLFN = settings.getDefaultDbSettingsXmlFn();
        settings.loadFromXML(settingsXMLFN);

        System.out.println(settings.toString());

        if (!DatabaseCreator.dropHomework15Database(settings)) {
            System.out.println(MSG_DATABASE_DROP_FAILED);
        }

        if (!DatabaseCreator.createHomework15Database(settings)) {
            System.out.println(MSG_DATABASE_CREATION_FAILED);
        }

        if (!DatabaseCreator.createUsersTable(settings)) {
            System.out.println(MSG_USERS_TABLE_CREATION_FILED);
        }


        Address dbServiceAddress = new Address("dbService01");
        MessageSystem messageSystem = new MessageSystem();

        try (ReflectionORMDatabaseService service = new ReflectionORMDatabaseService(settingsXMLFN, dbServiceAddress)) {
            messageSystem.addReciever(service);

            messageSystem.start();

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
            System.out.println(firstUser);

            service.read(FIRST_USER_ID);
            service.read(FIRST_USER_ID);
            service.read(WRONG_USER_ID);


            CacheControlServer server = new CacheControlServer(service.getCache(), messageSystem, dbServiceAddress);


            try {
                server.startServer();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //if (!DatabaseCreator.dropHomework10Database(settings)) {
        //    System.out.println(MSG_DATABASE_DROP_FAILED);
        //}
    }
}
