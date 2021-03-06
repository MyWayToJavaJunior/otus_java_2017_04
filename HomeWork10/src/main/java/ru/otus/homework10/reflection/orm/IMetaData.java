package ru.otus.homework10.reflection.orm;

import java.util.List;

public interface IMetaData {
    void clear();
    boolean read(Class<?>  clazz);
    boolean isEmpty();
    String getMappedTableName();
    String getIdFieldName();
    List<String> getFieldsNamesToInsert();
    List<String> getFieldsNamesToUpdate();

    List<DBFieldToClassFieldRelation> getFieldsToInsert();
    List<DBFieldToClassFieldRelation> getFieldsToUpdate();
}
