package ru.otus.homework09.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ORMReflectionHelper {
    private ORMReflectionHelper() {

    }

    public static boolean isJPAEntity(Class<?> clazz) {
        return  clazz.getAnnotation(Entity.class) != null;
    }

    public static String getMappedTableName(Class<?> clazz) {
        Annotation tableAnnotation = clazz.getAnnotation(Table.class);
        return tableAnnotation == null? null: ((Table)tableAnnotation).name();
    }

    public static boolean isTableColumn(Field field) {
        return getColumnAnnotation(field) != null;
    }

    public static Column getColumnAnnotation(Field field) {
        return field.getAnnotation(Column.class);
    }

    public static String getIdFieldName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                Column columnAnnotation = getColumnAnnotation(field);
                if (columnAnnotation != null){
                    return (columnAnnotation).name();
                }
            }
        }
        return null;
    }

    public static boolean isSimpleObject(Class clazz) {
        return (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) ||
                clazz == String.class || clazz == Character.class || clazz == Boolean.class);
    }

    public static void walOnTableColumnFields(Object obj, TableColumnFieldHandler handler) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!isTableColumn(field)) continue;
            if (!isSimpleObject(field.getType())) continue;

            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Column columnAnnotation = ORMReflectionHelper.getColumnAnnotation(field);

            handler.handle(obj, field, columnAnnotation);

            field.setAccessible(isAccessible);
        }

    }
}
