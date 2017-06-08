package ru.otus.homework09.dao;

import ru.otus.homework09.db.IORM;
import ru.otus.homework09.db.ORM;
import ru.otus.homework09.reflection.IMetaData;
import ru.otus.homework09.reflection.MetaData;

import java.sql.Connection;

public class UsersDAO {
    private final Connection connection;
    private final IORM orm;
    private IMetaData usersDataSetMetaData;

    public UsersDAO(Connection connection) {
        usersDataSetMetaData = new MetaData();
        usersDataSetMetaData.read(UsersDataSet.class);

        this.connection = connection;
        orm = new ORM(connection);
    }

    public UsersDataSet getUser(Long id) {
        try {
            return orm.loadObject(UsersDataSet.class, usersDataSetMetaData, id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean updateUser(UsersDataSet user) {
        try {
            if (user.getId() == null || user.getId() < 0) {
                orm.insertObject(user, usersDataSetMetaData);
            } else {
                orm.updateObject(user, usersDataSetMetaData);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
}
