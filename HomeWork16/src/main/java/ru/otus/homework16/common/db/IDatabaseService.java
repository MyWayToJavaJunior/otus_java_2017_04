package ru.otus.homework16.common.db;

import ru.otus.homework16.cache.ICache;
import ru.otus.homework16.common.db.datasets.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseService extends AutoCloseable{

    void save(UserDataSet dataSet) throws SQLException;
    UserDataSet read(long id) throws SQLException;
    List<UserDataSet> readAll();

    ICache getCache();

    void close();
}
