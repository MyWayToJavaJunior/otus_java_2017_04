package ru.otus.homework10.reflection.orm;

import ru.otus.homework10.common.UserDataSet;

import java.sql.Connection;

public class ReflectionORMUsersDAO {
    private final Connection connection;
    private final IORM orm;
    private IMetaData usersDataSetMetaData;

    public ReflectionORMUsersDAO(Connection connection) {
        usersDataSetMetaData = new MetaData();
        usersDataSetMetaData.read(UserDataSet.class);

        this.connection = connection;
        orm = new ORM(connection);
    }

    public UserDataSet getUser(Long id) {
        try {
            return orm.loadObject(UserDataSet.class, usersDataSetMetaData, id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean updateUser(UserDataSet user) {
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
