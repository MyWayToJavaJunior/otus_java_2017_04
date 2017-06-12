package ru.otus.homework10;

import ru.otus.homework10.common.db.datasets.AddressDataSet;
import ru.otus.homework10.common.db.DBSettings;
import ru.otus.homework10.common.db.datasets.PhoneDataSet;
import ru.otus.homework10.common.db.datasets.UserDataSet;
import ru.otus.homework10.common.db.DatabaseCreator;
import ru.otus.homework10.common.db.IDatabaseService;
import ru.otus.homework10.hibernate.HibernateDatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        DBSettings settings = DBSettings.getInstance();
        String settingsXMLFN = settings.getDefaultDbSettingsXmlFn();
        settings.loadFromXML(settingsXMLFN);

        System.out.println(settings.toString());

        if (!DatabaseCreator.dropHomework10Database(settings)) {
            System.out.println(MSG_DATABASE_DROP_FAILED);
        }

        if (!DatabaseCreator.createHomework10Database(settings)) {
            System.out.println(MSG_DATABASE_CREATION_FAILED);
        }

        try (IDatabaseService service = new HibernateDatabaseService(settingsXMLFN)) {

            UserDataSet user = new UserDataSet(FIRST_USER_ID, FIRST_USER_AGE, FIRST_USER_NAME);
            service.save(user);

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

        } catch (Exception e ) {
            System.err.println(e.getMessage());
        }

        //if (!DatabaseCreator.dropHomework10Database(settings)) {
        //    System.out.println(MSG_DATABASE_DROP_FAILED);
        //}
    }
}
