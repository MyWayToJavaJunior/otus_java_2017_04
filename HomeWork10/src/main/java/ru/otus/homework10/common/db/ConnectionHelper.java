package ru.otus.homework10.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static final String BASE_CONNECTION_STRING = "jdbc:mysql://%s:%d/%s?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String CONNECTION_STRING = BASE_CONNECTION_STRING + "&useSSL=false&user=%s&password=%s";

    private ConnectionHelper() {

    }

    public static String buildShortURL(DBSettings settings) {
        return String.format(BASE_CONNECTION_STRING, settings.getHost(), settings.getPort(), settings.getDatabseName());
    }


    public static String buildURL(String host, int port, String dbName, String login, String password) {
        return String.format(CONNECTION_STRING, host, port, dbName, login, password);
    }

    public static Connection getConnection(String host, int port, String dbName, String login, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());


            return DriverManager.getConnection(buildURL(host, port, dbName, login, password));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Connection getLocalConnection(String dbName, String login, String password) {
        return getConnection(DBSettings.DEFAULT_HOST, DBSettings.DEFAULT_MYSQL_PORT, dbName, login, password);
    }

    static Connection getSystemDBLocalConnection(String login, String password) {
        return getConnection(DBSettings.DEFAULT_HOST, DBSettings.DEFAULT_MYSQL_PORT, DBSettings.MYSQL_SYSTEM_DB, login, password);
    }


}
