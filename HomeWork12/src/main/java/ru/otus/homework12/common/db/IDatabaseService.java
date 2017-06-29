package ru.otus.homework12.common.db;

import ru.otus.homework12.cache.ICache;
import ru.otus.homework12.common.db.datasets.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseService extends AutoCloseable{

    void save(UserDataSet dataSet) throws SQLException;
    UserDataSet read(long id) throws SQLException;
    List<UserDataSet> readAll();

    ICache getCache();

    void close();
}
