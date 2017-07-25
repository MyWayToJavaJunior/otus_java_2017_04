package ru.otus.homework15.reflection.orm;

import ru.otus.homework15.common.db.CommonExecuter;
import ru.otus.homework15.common.db.sql.InsertOrUpdateSQLBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ORM implements IORM{
    private final Connection connection;
    CommonExecuter executer;

    public ORM(Connection connection) {
        this.connection = connection;
        executer = new CommonExecuter(connection);
    }

    @Override
    public<T> T loadObject(Class<T> clazz, IMetaData metaData, long id) throws IllegalAccessException, InstantiationException, SQLException {
        if (clazz == null || metaData.isEmpty()) return null;

        String query = String.format("SELECT * FROM %s WHERE %s = ? LIMIT 1", metaData.getMappedTableName(), metaData.getIdFieldName());
        return executer.execSelectOneTypedRec(query, clazz, id, (clazz1, resultSet) -> {
            T obj = null;
            while (resultSet.next()) {
               obj = clazz.newInstance();
               for (DBFieldToClassFieldRelation relation : metaData.getFieldsToInsert()){
                    try {
                        Field field = relation.getClassField();
                        Object value = resultSet.getObject(relation.getDbFieldName());
                        if (value != null) {
                            boolean isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            field.set(obj, value);
                            field.setAccessible(isAccessible);
                        }
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }

                }
            }
            return obj;

        });
    }

    @Override
    public boolean updateObject(Object obj, IMetaData metaData) throws IllegalAccessException, SQLException {
        if (metaData.isEmpty()) return false;

        List<String> fieldsNamesToInsert = metaData.getFieldsNamesToInsert();
        List<String> fieldsNamesToUpdate = metaData.getFieldsNamesToUpdate();
        List<Object> valuesToInsert = ORMReflectionHelper.getFieldsValues(obj, metaData.getFieldsToInsert());
        List<Object> valuesToUpdate = ORMReflectionHelper.getFieldsValues(obj, metaData.getFieldsToUpdate());

        String query = InsertOrUpdateSQLBuilder.buildInsertOrUpdateSQL(metaData.getMappedTableName(), fieldsNamesToInsert, fieldsNamesToUpdate);
        return executer.execInsertOrUpdate(query, valuesToInsert, valuesToUpdate);
    }

    @Override
    public boolean insertObject(Object obj, IMetaData metaData) throws SQLException, IllegalAccessException {
        if (!ORMReflectionHelper.isJPAEntity(obj.getClass())) return false;
        String tableName = ORMReflectionHelper.getMappedTableName(obj.getClass());
        if (tableName == null) return false;

        List<String> fieldsNamesToInsert = metaData.getFieldsNamesToInsert();
        List<Object> valuesToInsert = ORMReflectionHelper.getFieldsValues(obj, metaData.getFieldsToInsert());

        String query = InsertOrUpdateSQLBuilder.buildInsertSQL(tableName, fieldsNamesToInsert);
        return executer.execInsert(query, valuesToInsert);
    }
}
