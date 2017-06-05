package ru.otus.homework09.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonExecuter {
    private static final String SELECT_ONE_REC_SQL = "SELECT * FROM %s WHERE %s = %d LIMIT 1";

    private final Connection connection;

    public CommonExecuter(Connection connection) {
        this.connection = connection;
    }

    public <T> T execSelectTQuery(String query, IResultSetHandler<T> handler) throws SQLException {
        T value = null;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(result);
            }
        }
        return value;
    }

    public Object execSelectOneTRecQuery(Class<?> clazz, long id, IResultSetHandler handler) throws SQLException {
        String tableName = "";
        String idFieldName = "";


        Object value = null;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(String.format(SELECT_ONE_REC_SQL, tableName, idFieldName, id));
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(result);
            }
        }
        return value;
    }

    public void execSelectQuery(String query, IResultSetHandler handler) throws SQLException {
        try (Statement stmt = connection.createStatement()){
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                handler.handle(result);
            }
        }
    }

    public boolean execUpdateQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()){
            return stmt.execute(query);
        }
    }

    public boolean execInsertOrUpdateQuery(Object obj) throws SQLException, IllegalAccessException {
        if (!ORMReflectionHelper.isJPAEntity(obj.getClass())) return false;
        String tableName = ORMReflectionHelper.getMappedTableName(obj.getClass());
        if (tableName == null) return false;

        List<String> fieldsToInsert = new ArrayList<>();
        List<Object> valuesToInsert = new ArrayList<>();
        List<String> fieldsToUpdate = new ArrayList<>();
        List<Object> valuesToUpdate = new ArrayList<>();

        ORMReflectionHelper.walOnTableColumnFields(obj, (obj1, field, columnAnnotation) -> {
            Object fieldValue = field.get(obj1);
            fieldsToInsert.add(columnAnnotation.name());
            valuesToInsert.add(fieldValue);

            if (columnAnnotation.updatable()) {
                fieldsToUpdate.add(columnAnnotation.name());
                valuesToUpdate.add(fieldValue);
            }
        });

        String query = InsertOrUpdateSQLBuilder.buildInsertOrUpdateSQL(tableName, fieldsToInsert, fieldsToUpdate);
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            InsertOrUpdateSQLBuilder.setStatmentParams(stmt, valuesToInsert, valuesToUpdate);
            return stmt.execute();
        }
    }

    public boolean execInsertQuery(Object obj) throws SQLException, IllegalAccessException {
        if (!ORMReflectionHelper.isJPAEntity(obj.getClass())) return false;
        String tableName = ORMReflectionHelper.getMappedTableName(obj.getClass());
        if (tableName == null) return false;

        List<String> fieldsToInsert = new ArrayList<>();
        List<Object> valuesToInsert = new ArrayList<>();

        ORMReflectionHelper.walOnTableColumnFields(obj, (obj1, field, columnAnnotation) -> {
            Object fieldValue = field.get(obj1);
            if (columnAnnotation.insertable()) {
                fieldsToInsert.add(columnAnnotation.name());
                valuesToInsert.add(fieldValue);
            }
        });

        String query = InsertOrUpdateSQLBuilder.buildInsertSQL(tableName, fieldsToInsert);
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            InsertOrUpdateSQLBuilder.setStatmentParams(stmt, valuesToInsert, 0);
            return stmt.execute();
        }
    }




}
