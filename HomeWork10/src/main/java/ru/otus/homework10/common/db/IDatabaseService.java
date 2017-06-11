package ru.otus.homework10.common.db;

import ru.otus.homework10.common.UserDataSet;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseService extends Closeable{

    void save(UserDataSet dataSet) throws SQLException;
    UserDataSet read(long id) throws SQLException;
    List<UserDataSet> readAll();

    void loadConfiguration(String configuartionFileName);
    void openConnection();
    void closeConnection();

    void close();
}
