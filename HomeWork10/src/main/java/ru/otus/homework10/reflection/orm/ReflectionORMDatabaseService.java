package ru.otus.homework10.reflection.orm;

import ru.otus.homework10.common.db.DBSettings;
import ru.otus.homework10.common.db.datasets.UserDataSet;
import ru.otus.homework10.common.db.ConnectionHelper;
import ru.otus.homework10.common.db.IDatabaseService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReflectionORMDatabaseService implements IDatabaseService {
    private static final String MSG_NOT_IMPEMENTED = "Not impemented";
    private static final String MSG_CONNECTION_IS_CLOSED = "Connection is closed";

    private final DBSettings settings;

    ReflectionORMUsersDAO dao;
    Connection connection;

    public ReflectionORMDatabaseService() {
        settings = DBSettings.getInstance();
    }

    @Override
    public void save(UserDataSet dataSet) throws SQLException {
        if (dao == null) {
            throw new SQLException(MSG_CONNECTION_IS_CLOSED);
        }
        dao.updateUser(dataSet);
    }

    @Override
    public UserDataSet read(long id) throws SQLException {
        if (dao == null) {
            throw new SQLException(MSG_CONNECTION_IS_CLOSED);
        }

        return dao.getUser(id);
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException(MSG_NOT_IMPEMENTED);
    }

    @Override
    public void loadConfiguration(String configuartionFileName) {
        settings.loadFromXML(configuartionFileName);
    }

    @Override
    public void openConnection() {
        closeConnection();
        connection = ConnectionHelper.getConnection(settings.getHost(), settings.getPort(),
                settings.getDatabseName(), settings.getLogin(), settings.getPassword());
        dao = new ReflectionORMUsersDAO(connection);
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        connection = null;
        dao = null;
    }

    @Override
    public void close()  {
        closeConnection();
    }
}
