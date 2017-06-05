package ru.otus.homework09.dao;

import ru.otus.homework09.db.ORM;

import java.sql.Connection;

public class UsersDAO {
    private final Connection connection;
    private final ORM orm;

    public UsersDAO(Connection connection) {
        this.connection = connection;
        orm = new ORM(connection);
    }

    public UsersDataSet getUser(int id) {
        try {
            return orm.loadObject(UsersDataSet.class, id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean updateUser(UsersDataSet user) {
        try {
            if (user.getId() == null || user.getId() < 0) {
                orm.insertObject(user);
            } else {
                orm.updateObject(user);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
}
