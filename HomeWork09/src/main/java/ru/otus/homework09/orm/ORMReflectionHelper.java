package ru.otus.homework09.orm;

import ru.otus.homework09.db.handlers.TableColumnClassFieldHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    private static boolean isTableColumn(Field field) {
        return getColumnAnnotation(field) != null;
    }

    private static Column getColumnAnnotation(Field field) {
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

    private static boolean isSimpleObject(Class clazz) {
        return (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) ||
                clazz == String.class || clazz == Character.class || clazz == Boolean.class);
    }

    public static void walOnTableColumnFields(Class clazz, TableColumnClassFieldHandler handler) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!isTableColumn(field)) continue;
            if (!isSimpleObject(field.getType())) continue;

            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Column columnAnnotation = ORMReflectionHelper.getColumnAnnotation(field);

            handler.handle(field, columnAnnotation);

            field.setAccessible(isAccessible);
        }
    }

    public static List<Object> getFieldsValues(Object obj, List<DBFieldToClassFieldRelation> fields) throws IllegalAccessException {
        List<Object> res = new ArrayList<>();
        for (DBFieldToClassFieldRelation relation : fields) {
            Field field = relation.getClassField();
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);

            res.add(field.get(obj));

            field.setAccessible(isAccessible);
        }
        return res;
    }

}
