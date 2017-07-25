package ru.otus.homework15.common.db.connection.pool;

import ru.otus.homework15.common.db.DBSettings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPool implements IConnectionPool{
    public static final int DEFAULT_INITIAL_SIZE = 5;
    public static final int DEFAULT_MINIMUM_SIZE = 3;
    public static final int DEFAULT_MAXIMUM_SIZE = 8;


    private final DBSettings settings;

    private final int initialSize;
    private final int minimumSize;
    private final int maximumSize;

    public ConnectionPool(DBSettings settings, int initialSize, int minimumSize, int maximumSize) {
        this.settings = settings;
        this.minimumSize = Math.min(minimumSize, maximumSize);
        this.initialSize = Math.max(Math.min(initialSize, maximumSize), this.minimumSize);
        this.maximumSize = maximumSize;

        addConnections(initialSize);
    }

    private final Queue<ConnectionForPool> pool = new ConcurrentLinkedQueue<>();


    public int getInitialSize() {
        return initialSize;
    }

    public int getMinimumSize() {
        return minimumSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    @Override
    public Connection getConnection() {
        Connection connection;
        synchronized (pool) {
            connection = pool.poll();
            if (pool.size() < minimumSize) {
                addConnections(initialSize - pool.size());
            }
        }
        return connection;
    }

    @Override
    public List<Connection> getConnections(int count) {

        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            connections.add(getConnection());
        }
        return connections;
    }

    private void addConnections(int connectionsCount) {
        for (int i = 0; i < connectionsCount; i++) {
            pool.add(new ConnectionForPool(this, ConnectionHelper.getConnection(settings)));
        }
    }

    @Override
    public void close() {
        synchronized (pool) {
            for (ConnectionForPool c : pool) {
                try {
                    c.totalClose();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public int getAvaivableConnectionsCount() {
        return pool.size();
    }

    private class ConnectionForPool extends ConnectionForPoolBase {
        ConnectionForPool(ConnectionPool connectionPool, Connection connection) {
            super(connection);
        }

        @Override
        public void close() throws SQLException {
            synchronized (pool) {
                if (pool.size() == maximumSize) {
                    totalClose();
                } else {
                    pool.add(this);
                }
            }

        }
    }

}
