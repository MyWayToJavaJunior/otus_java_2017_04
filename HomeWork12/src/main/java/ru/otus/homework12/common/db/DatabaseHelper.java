package ru.otus.homework12.common.db;

import ru.otus.homework12.common.db.connection.pool.ConnectionHelper;
import ru.otus.homework12.common.db.sql.CreateTableSQLBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseHelper {

    private static final String CHECK_DATABASE_PRESENT_SQL = "SELECT * FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = '%s'";
    private static final String CHECK_TABLE_PRESENT_SQL = "SELECT * FROM information_schema.TABLES WHERE TABLE_NAME = '%s' AND TABLE_SCHEMA = '%s'";
    private static final String CREATE_DATABASE_SQL = "CREATE DATABASE IF NOT EXISTS %s";
    private static final String DROP_DATABASE_SQL = "DROP DATABASE IF EXISTS %s";
    private static final String SELECT_ALL_SQL = "SELECT * FROM %s";
    private static final String CLEAR_TABLE_SQL = "TRUNCATE TABLE %s";

    public static boolean createDatabase(DBSettings settings) {
        boolean res = false;

        try (Connection connection = ConnectionHelper.getSystemDBLocalConnection(settings.getLogin(), settings.getPassword())) {

            CommonExecuter commonExecuter = new CommonExecuter(connection);
            commonExecuter.execUpdate(String.format(CREATE_DATABASE_SQL, settings.getDatabseName()));
            res = commonExecuter.execTypedSelect(String.format(CHECK_DATABASE_PRESENT_SQL, settings.getDatabseName()), ResultSet::next);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }

    public static boolean dropDatabase(DBSettings settings) {
        boolean res = false;
        try (Connection connection = ConnectionHelper.getSystemDBLocalConnection(settings.getLogin(), settings.getPassword())) {

            CommonExecuter commonExecuter = new CommonExecuter(connection);
            commonExecuter.execUpdate(String.format(DROP_DATABASE_SQL, settings.getDatabseName()));
            res = !commonExecuter.execTypedSelect(String.format(CHECK_DATABASE_PRESENT_SQL, settings.getDatabseName()), ResultSet::next);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }

    public static boolean createTable(DBSettings settings, String tableName, List<String> fields, List<String> filedsTypes, List<String> primaryKeyFileds) {
        boolean res = false;
        if (fields.size() == 0 || filedsTypes.size() == 0 || primaryKeyFileds.size() == 0 || fields.size() != filedsTypes.size())
            return res;

        CreateTableSQLBuilder builder = new CreateTableSQLBuilder(tableName);
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String fieldType = filedsTypes.get(i);
            builder.addField(field, fieldType, primaryKeyFileds.contains(field));
        }

        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {

            CommonExecuter commonExecuter = new CommonExecuter(connection);
            String query = builder.build();

            commonExecuter.execUpdate(query);
            res = commonExecuter.execTypedSelect(String.format(CHECK_TABLE_PRESENT_SQL, tableName, settings.getDatabseName()), ResultSet::next);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return res;
    }

    public static boolean clearTable(DBSettings settings, String tableName) {
        boolean res = false;
        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {

            CommonExecuter commonExecuter = new CommonExecuter(connection);
            commonExecuter.execUpdate(String.format(CLEAR_TABLE_SQL, tableName));
            res = !commonExecuter.execTypedSelect(String.format(SELECT_ALL_SQL, tableName), ResultSet::next);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }
}
