package ru.otus.homework11;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.homework11.common.db.DBSettings;
import ru.otus.homework11.common.db.DatabaseCreator;
import ru.otus.homework11.common.db.connection.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ConnectionPoolTest {

    @BeforeClass
    public static void beforeAll(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        DatabaseCreator.createHomework10Database(settings);
    }

    @Test
    public void ConnectionPoolInitTest(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();

        try(ConnectionPool pool = new ConnectionPool(settings, ConnectionPool.DEFAULT_INITIAL_SIZE, ConnectionPool.DEFAULT_MINIMUM_SIZE, ConnectionPool.DEFAULT_MAXIMUM_SIZE)) {
            Assert.assertEquals(ConnectionPool.DEFAULT_INITIAL_SIZE, pool.getAvaivableConnectionsCount());
        }

    }

    @Test
    public void ConnectionPoolGetConnectionsTest(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();

        try(ConnectionPool pool = new ConnectionPool(settings, ConnectionPool.DEFAULT_INITIAL_SIZE, ConnectionPool.DEFAULT_MINIMUM_SIZE, ConnectionPool.DEFAULT_MAXIMUM_SIZE)) {
            final int connQueryCnt = ConnectionPool.DEFAULT_MAXIMUM_SIZE;
            List<Connection> connections = pool.getConnections(connQueryCnt);
            Assert.assertEquals(connQueryCnt, connections.size());
        }
    }

    @Test
    public void ConnectionPoolMinSizeReachTest(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();

        try(ConnectionPool pool = new ConnectionPool(settings, ConnectionPool.DEFAULT_INITIAL_SIZE, ConnectionPool.DEFAULT_MINIMUM_SIZE, ConnectionPool.DEFAULT_MAXIMUM_SIZE)) {
            final int connQueryCnt = Math.abs(pool.getMinimumSize() - pool.getInitialSize());
            pool.getConnections(connQueryCnt);
            Assert.assertEquals(pool.getMinimumSize(), pool.getAvaivableConnectionsCount());
            pool.getConnection();
            Assert.assertEquals(pool.getInitialSize(), pool.getAvaivableConnectionsCount());
        }
    }

    @Test
    public void ConnectionPoolMaxSizeReachTest(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();

        try(ConnectionPool pool = new ConnectionPool(settings, ConnectionPool.DEFAULT_INITIAL_SIZE, ConnectionPool.DEFAULT_MINIMUM_SIZE, ConnectionPool.DEFAULT_MAXIMUM_SIZE)) {
            List<Connection> connections = pool.getConnections(ConnectionPool.DEFAULT_MAXIMUM_SIZE);
            for (Connection connection : connections) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            Assert.assertEquals(pool.getMaximumSize(), pool.getAvaivableConnectionsCount());
        }
    }

    @AfterClass
    public static void afterAll(){
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        DatabaseCreator.createHomework10Database(settings);
    }
}
