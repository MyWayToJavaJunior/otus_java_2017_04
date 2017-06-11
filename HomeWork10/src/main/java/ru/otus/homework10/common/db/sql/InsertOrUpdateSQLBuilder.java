package ru.otus.homework10.common.db.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertOrUpdateSQLBuilder {

    private static void addFieldsToInsert(StringBuilder queryBuilder, List<String> fieldsToInsert) {
        queryBuilder.append("(");
        for (int i = 0; i < fieldsToInsert.size(); i++) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(fieldsToInsert.get(i));
        }
        queryBuilder.append(")");

        queryBuilder.append("VALUES");
        queryBuilder.append("(");
        for (int i = 0; i < fieldsToInsert.size(); i++) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("?");
        }
        queryBuilder.append(")");
    }

    private static void addFieldsToUpdate(StringBuilder queryBuilder, List<String> fieldsToUpdate) {
        queryBuilder.append(" ON DUPLICATE KEY UPDATE ");
        for (int i = 0; i < fieldsToUpdate.size(); i++) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(fieldsToUpdate.get(i));
            queryBuilder.append(" = ?");
        }
    }

    public static String buildInsertSQL(String tableName, List<String> fieldsToInsert) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName);
        addFieldsToInsert(queryBuilder, fieldsToInsert);
        return queryBuilder.toString();
    }

    public static String buildInsertOrUpdateSQL(String tableName, List<String> fieldsToInsert, List<String> fieldsToUpdate) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName);
        addFieldsToInsert(queryBuilder, fieldsToInsert);
        addFieldsToUpdate(queryBuilder, fieldsToUpdate);
        return queryBuilder.toString();
    }

    public static void setStatmentParams(PreparedStatement statement, List<Object> values, int startIndex) throws SQLException {
        int index = startIndex;
        for (Object param : values) {
            statement.setObject(index++, param);
        }
    }

    public static void setStatmentParams(PreparedStatement statement, List<Object> valuesToInsert, List<Object> valuesToUpdate) throws SQLException {
        setStatmentParams(statement, valuesToInsert, 1);
        setStatmentParams(statement, valuesToUpdate, valuesToInsert.size() + 1);
    }
}
