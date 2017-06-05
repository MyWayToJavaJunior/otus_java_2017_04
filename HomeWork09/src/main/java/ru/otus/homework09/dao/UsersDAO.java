package ru.otus.homework09.dao;

import ru.otus.homework09.db.CommonExecuter;

import java.sql.Connection;

public class UsersDAO {
    private final Connection connection;
    private final CommonExecuter executer;

    public UsersDAO(Connection connection) {
        this.connection = connection;
        executer = new CommonExecuter(connection);
    }

    public UsersDAO getUser(int id) {
        return null;
    }

    public boolean updateUser(UsersDataSet user) {
        try {
            if (user.getId() == null || user.getId() < 0) {
                executer.execInsertQuery(user);
            } else {
                executer.execInsertOrUpdateQuery(user);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
}
