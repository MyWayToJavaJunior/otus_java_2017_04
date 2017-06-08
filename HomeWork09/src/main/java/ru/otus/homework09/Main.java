package ru.otus.homework09;

import ru.otus.homework09.dao.DatabaseCreator;
import ru.otus.homework09.dao.UsersDAO;
import ru.otus.homework09.dao.UsersDataSet;
import ru.otus.homework09.db.helpers.ConnectionHelper;
import ru.otus.homework09.db.DBSettings;

import java.sql.Connection;

public class Main {

    private static final String FIRST_USER_NAME = "Igor";
    private static final String SECOND_USER_NAME = "Dambldor";
    private static final String THIRD_USER_NAME = "Harry Potter";
    private static final int FIRST_USER_AGE = 13;
    private static final int SECOND_USER_AGE = 150;
    private static final int THIRD_USER_AGE = 15;
    private static final long FIRST_USER_ID = 1L;
    private static final long SECOND_USER_ID = 2L;
    private static final long NO_ID = -1L;
    private static final int FIRST_USER_NEW_AGE = 35;
    private static final String FIRST_USER_NEW_NAME = "Vasya";

    private static final String MSG_DATABASE_DROP_FAILED = "Database drop failed";
    private static final String MSG_DATABASE_CREATION_FAILED = "Database creation failed";

    public static void main(String[] args) {
        DBSettings settings = DBSettings.getInstance();
        System.out.println(settings.toString());

        if (!DatabaseCreator.dropHomework09Database(settings)) {
            System.out.println(MSG_DATABASE_DROP_FAILED);
        }

        if (!DatabaseCreator.createHomework09Database(settings)) {
            System.out.println(MSG_DATABASE_CREATION_FAILED);
        }

        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {
            UsersDAO dao = new UsersDAO(connection);

            UsersDataSet user = new UsersDataSet(FIRST_USER_ID, FIRST_USER_AGE, FIRST_USER_NAME);
            dao.updateUser(user);

            dao.updateUser(new UsersDataSet(SECOND_USER_ID, SECOND_USER_AGE, SECOND_USER_NAME));
            dao.updateUser(new UsersDataSet(NO_ID, THIRD_USER_AGE, THIRD_USER_NAME));

            user.setAge(FIRST_USER_NEW_AGE);
            user.setName(FIRST_USER_NEW_NAME);
            dao.updateUser(user);

            UsersDataSet firstUser = dao.getUser(FIRST_USER_ID);
            System.out.println(firstUser);

        } catch (Exception e ) {
            System.err.println(e.getMessage());
        }


        if (!DatabaseCreator.dropHomework09Database(settings)) {
            System.out.println(MSG_DATABASE_DROP_FAILED);
        }

    }
}
