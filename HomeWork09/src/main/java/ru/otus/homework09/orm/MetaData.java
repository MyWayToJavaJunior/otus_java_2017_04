package ru.otus.homework09.orm;

import java.util.ArrayList;
import java.util.List;

public class MetaData implements IMetaData {
    private boolean isEmpty = true;

    private String mappedTableName;
    private String idFieldName;
    private List<String> fieldsNamesToInsert;
    private List<String> fieldsNamesToUpdate;

    private List<DBFieldToClassFieldRelation> fieldsToInsert = new ArrayList<>();
    private List<DBFieldToClassFieldRelation> fieldsToUpdate = new ArrayList<>();

    public void clear(){
        isEmpty = true;
        mappedTableName = null;
        idFieldName = null;
        fieldsNamesToInsert = null;
        fieldsNamesToUpdate = null;
        fieldsToInsert = null;
        fieldsToUpdate = null;
    }

    @Override
    public boolean read(Class<?> clazz) {
        clear();

        if (!ORMReflectionHelper.isJPAEntity(clazz)) return false;

        String mappedTableName = ORMReflectionHelper.getMappedTableName(clazz);
        String idFieldName = ORMReflectionHelper.getIdFieldName(clazz);
        if (mappedTableName == null || idFieldName == null) return false;

        List<String> fieldsNamesToInsert = new ArrayList<>();
        List<String> fieldsNamesToUpdate = new ArrayList<>();
        List<DBFieldToClassFieldRelation> fieldsToInsert = new ArrayList<>();
        List<DBFieldToClassFieldRelation> fieldsToUpdate = new ArrayList<>();

        ORMReflectionHelper.walOnTableColumnFields(clazz, (field, columnAnnotation) -> {
            fieldsNamesToInsert.add(columnAnnotation.name());
            fieldsToInsert.add(new DBFieldToClassFieldRelation(columnAnnotation.name(), field));
            if (columnAnnotation.updatable()) {
                fieldsNamesToUpdate.add(columnAnnotation.name());
                fieldsToUpdate.add(new DBFieldToClassFieldRelation(columnAnnotation.name(), field));
            }
        });

        if (fieldsToInsert.isEmpty()) return false;
        this.mappedTableName = mappedTableName;
        this.idFieldName = idFieldName;

        this.fieldsNamesToInsert = fieldsNamesToInsert;
        this.fieldsNamesToUpdate = fieldsNamesToUpdate;
        this.fieldsToInsert = fieldsToInsert;
        this.fieldsToUpdate = fieldsToUpdate;
        this.isEmpty = false;

        return true;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public String getMappedTableName() {
        return mappedTableName;
    }

    @Override
    public String getIdFieldName() {
        return idFieldName;
    }

    @Override
    public List<String> getFieldsNamesToInsert() {
        return fieldsNamesToInsert;
    }

    @Override
    public List<String> getFieldsNamesToUpdate() {
        return fieldsNamesToUpdate;
    }

    @Override
    public List<DBFieldToClassFieldRelation> getFieldsToInsert() {
        return fieldsToInsert;
    }

    @Override
    public List<DBFieldToClassFieldRelation> getFieldsToUpdate() {
        return fieldsToUpdate;
    }

}
