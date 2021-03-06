package ru.otus.homework13.reflection.orm;

import ru.otus.homework13.cache.Cache;
import ru.otus.homework13.cache.ICache;
import ru.otus.homework13.common.db.DBSettings;
import ru.otus.homework13.common.db.connection.pool.ConnectionPool;
import ru.otus.homework13.common.db.datasets.UserDataSet;
import ru.otus.homework13.common.db.IDatabaseService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReflectionORMDatabaseService implements IDatabaseService {
    private static final String MSG_NOT_IMPEMENTED = "Not impemented";

    private final DBSettings settings;
    private final IMetaData usersDataSetMetaData;
    private final Cache<Long, UserDataSet> cache;
    private final ConnectionPool connectionPool;


    public ReflectionORMDatabaseService() {
        settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        usersDataSetMetaData = new MetaData();
        usersDataSetMetaData.read(UserDataSet.class);

        cache = new Cache<>(Cache.DEFAULT_CACHED_OBJECT_LIFE_TIME, Cache.DEFAULT_CACHED_OBJECT_IDLE_TIME, Cache.DEFAULT_CACHE_MAX_SIZE);
        connectionPool = new ConnectionPool(settings, ConnectionPool.DEFAULT_INITIAL_SIZE, ConnectionPool.DEFAULT_MINIMUM_SIZE, ConnectionPool.DEFAULT_MAXIMUM_SIZE);
    }

    @Override
    public void save(UserDataSet dataSet) throws SQLException {
        Long id = dataSet.getId();
        try (Connection connection = connectionPool.getConnection()) {
            if ((new ReflectionORMUsersDAO(connection, usersDataSetMetaData)).updateUser(dataSet) && id != null) {
                cache.put(id, dataSet);
            }
        }
    }

    @Override
    public UserDataSet read(long id) throws SQLException {
        UserDataSet user = cache.get(id);
        if (user == null) {
            try (Connection connection = connectionPool.getConnection()) {
                user = (new ReflectionORMUsersDAO(connection, usersDataSetMetaData)).getUser(id);
                if (user != null) {
                    cache.put(id, user);
                }
            }
        }
        return user;
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException(MSG_NOT_IMPEMENTED);
    }

    @Override
    public ICache getCache() {
        return cache;
    }


    @Override
    public void close()  {
        connectionPool.close();
        cache.close();
    }
}
