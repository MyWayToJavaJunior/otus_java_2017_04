package ru.otus.homework11.common.db;

import ru.otus.homework11.common.db.handlers.IORMResultSetHandler;
import ru.otus.homework11.common.db.handlers.ISimpleResultSetHandler;
import ru.otus.homework11.common.db.sql.InsertOrUpdateSQLBuilder;

import java.sql.*;
import java.util.List;

public class CommonExecuter {
    private final Connection connection;

    public CommonExecuter(Connection connection) {
        this.connection = connection;
    }

    public <T> T execTypedSelect(String query, ISimpleResultSetHandler<T> handler) throws SQLException {
        T value;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(result);
            }
        }
        return value;
    }

    public <T> T execSelectOneTypedRec(String query, Class clazz, long id, IORMResultSetHandler<T> handler) throws SQLException, IllegalAccessException, InstantiationException {
        T value;
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setLong(1, id);
            stmt.execute();
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(clazz, result);
            }
        }
        return value;
    }

    public boolean execUpdate(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()){
            return stmt.execute(query);
        }
    }

    public boolean execInsertOrUpdate(String query, List<Object> valuesToInsert, List<Object> valuesToUpdate) throws SQLException, IllegalAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            InsertOrUpdateSQLBuilder.setStatmentParams(stmt, valuesToInsert, valuesToUpdate);
            return stmt.execute();
        }
    }

    public boolean execInsert(String query, List<Object> valuesToInsert) throws SQLException, IllegalAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            InsertOrUpdateSQLBuilder.setStatmentParams(stmt, valuesToInsert, 1);
            return stmt.execute();
        }
    }




}
