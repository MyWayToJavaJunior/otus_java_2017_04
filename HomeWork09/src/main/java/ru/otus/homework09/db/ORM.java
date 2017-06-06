package ru.otus.homework09.db;

import ru.otus.homework09.db.helpers.ORMReflectionHelper;
import ru.otus.homework09.db.sql.InsertOrUpdateSQLBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ORM implements IORM{
    private final Connection connection;
    CommonExecuter executer;

    public ORM(Connection connection) {
        this.connection = connection;
        executer = new CommonExecuter(connection);
    }

    public<T> T loadObject(Class<T> clazz, long id) throws IllegalAccessException, InstantiationException, SQLException {
        if (!ORMReflectionHelper.isJPAEntity(clazz)) return null;

        String tableName = ORMReflectionHelper.getMappedTableName(clazz);
        String idFieldName = ORMReflectionHelper.getIdFieldName(clazz);
        if (tableName == null || idFieldName == null) return null;

        String query = String.format("SELECT * FROM %s WHERE %s = ? LIMIT 1", tableName, idFieldName);
        return executer.execSelectOneTypedRec(query, clazz, id, (clazz1, resultSet) -> {
            T obj = clazz.newInstance();
            while (resultSet.next()) {
                ORMReflectionHelper.walOnTableColumnFields(obj, (obj1, field, columnAnnotation) -> {
                    try {
                        Object value = resultSet.getObject(columnAnnotation.name());
                        if (value != null) {
                            boolean isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            field.set(obj1, value);
                            field.setAccessible(isAccessible);
                        }
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }

                });
            }
            return obj;

        });
    }

    public boolean updateObject(Object obj) throws IllegalAccessException, SQLException {
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
        return executer.execInsertOrUpdate(query, valuesToInsert, valuesToUpdate);
    }

    public boolean insertObject(Object obj) throws SQLException, IllegalAccessException {
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
        return executer.execInsert(query, valuesToInsert);
    }
}
