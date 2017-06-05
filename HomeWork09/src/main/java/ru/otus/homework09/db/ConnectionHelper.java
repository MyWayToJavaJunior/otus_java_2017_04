package ru.otus.homework09.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static final String CONNECTION_STRING = "jdbc:mysql://%s:%d/%s?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&user=%s&password=%s";
    private static final DBSettings settings = DBSettings.getInstance();

    private ConnectionHelper() {

    }

    public static Connection getConnection(String host, int port, String dbName, String login, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = String.format(CONNECTION_STRING, host, port, dbName, login, password);

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(String host, String dbName, String login, String password) {
        return getConnection(host, settings.getPort(), dbName, login, password);
    }

    public static Connection getLocalConnection(String dbName, String login, String password) {
        return getConnection(settings.getHost(), settings.getPort(), dbName, login, password);
    }

    public static Connection getSystemDBLocalConnection(String login, String password) {
        return getConnection(settings.getHost(), settings.getPort(), DBSettings.MYSQL_SYSTEM_DB, login, password);
    }


}
