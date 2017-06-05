package ru.otus.homework09;

import ru.otus.homework09.dao.DatabaseCreator;
import ru.otus.homework09.dao.UsersDAO;
import ru.otus.homework09.dao.UsersDataSet;
import ru.otus.homework09.db.helpers.ConnectionHelper;
import ru.otus.homework09.db.DBSettings;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DBSettings settings = DBSettings.getInstance();
        System.out.println(settings.toString());

        if (!DatabaseCreator.dropHomework09Database(settings)) {
            System.out.println("Database drop filed");
        }

        if (!DatabaseCreator.createHomework09Database(settings)) {
            System.out.println("Database creation filed");
        }

        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {
            UsersDAO dao = new UsersDAO(connection);

            UsersDataSet user = new UsersDataSet(1L, 13, "Igor");
            dao.updateUser(new UsersDataSet(2L, 150, "Dambldor"));
            dao.updateUser(new UsersDataSet(-1L, 15, "Harry Potter"));

            user.setAge(35);
            user.setName("Vasya");
            dao.updateUser(user);

            UsersDataSet userDambldor = dao.getUser(2);
            System.out.println(userDambldor);

        } catch (Exception e ) {
            System.err.println(e.getMessage());
        }

/*
        if (!DatabaseCreator.dropHomework09Database(settings)) {
            System.out.println("Database drop filed");
        }
*/
    }
}
