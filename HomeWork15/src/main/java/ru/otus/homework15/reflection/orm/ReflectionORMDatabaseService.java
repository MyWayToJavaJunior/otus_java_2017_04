package ru.otus.homework15.reflection.orm;

import ru.otus.homework15.cache.Cache;
import ru.otus.homework15.cache.ICache;
import ru.otus.homework15.common.db.DBSettings;
import ru.otus.homework15.common.db.connection.pool.ConnectionPool;
import ru.otus.homework15.common.db.datasets.UserDataSet;
import ru.otus.homework15.common.db.IDatabaseService;
import ru.otus.homework15.message.system.Address;
import ru.otus.homework15.message.system.MessageSystemContext;
import ru.otus.homework15.message.system.base.IMessageReceiver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReflectionORMDatabaseService implements IDatabaseService, IMessageReceiver {
    private static final String MSG_NOT_IMPEMENTED = "Not impemented";

    private final MessageSystemContext messageSystemContext;
    private final DBSettings settings;
    private final IMetaData usersDataSetMetaData;
    private final Cache<Long, UserDataSet> cache;
    private final ConnectionPool connectionPool;


    public ReflectionORMDatabaseService(String configuartionFileName, MessageSystemContext messageSystemContext) {
        this.messageSystemContext = messageSystemContext;

        settings = DBSettings.getInstance();
        settings.loadFromXML(configuartionFileName);
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

    @Override
    public Address getAddress() {
        return messageSystemContext.getDbServiceAddress();
    }
}
