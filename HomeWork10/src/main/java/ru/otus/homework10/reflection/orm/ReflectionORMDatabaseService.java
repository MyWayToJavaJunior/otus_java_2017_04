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

    private final DBSettings settings;
    private final IMetaData usersDataSetMetaData;


    public ReflectionORMDatabaseService(String configuartionFileName) {
        settings = DBSettings.getInstance();
        settings.loadFromXML(configuartionFileName);
        usersDataSetMetaData = new MetaData();
        usersDataSetMetaData.read(UserDataSet.class);
    }

    @Override
    public void save(UserDataSet dataSet) throws SQLException {
        try (Connection connection = ConnectionHelper.getConnection(settings)) {
            (new ReflectionORMUsersDAO(connection, usersDataSetMetaData)).updateUser(dataSet);
        }
    }

    @Override
    public UserDataSet read(long id) throws SQLException {
        UserDataSet user;
        try (Connection connection = ConnectionHelper.getConnection(settings)) {
            user =  (new ReflectionORMUsersDAO(connection, usersDataSetMetaData)).getUser(id);
        }
        return user;
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException(MSG_NOT_IMPEMENTED);
    }


    @Override
    public void close()  {

    }
}
