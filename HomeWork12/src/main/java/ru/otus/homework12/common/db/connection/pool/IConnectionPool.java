package ru.otus.homework12.common.db.connection.pool;

import java.sql.Connection;
import java.util.List;

public interface IConnectionPool extends AutoCloseable {
    Connection getConnection();
    List<Connection> getConnections(int cnt);

    @Override
    void close();
}
