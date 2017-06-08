package ru.otus.homework09.reflection;

import java.lang.reflect.Field;

public class DBFieldToClassFieldRelation {
    private String dbFieldName;
    private Field classField;

    public DBFieldToClassFieldRelation(String dbFieldName, Field classField) {
        this.dbFieldName = dbFieldName;
        this.classField = classField;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public Field getClassField() {
        return classField;
    }
}
