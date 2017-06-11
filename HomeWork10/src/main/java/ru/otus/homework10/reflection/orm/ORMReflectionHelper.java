package ru.otus.homework10.reflection.orm;

import ru.otus.homework10.common.db.handlers.TableColumnClassFieldHandler;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ORMReflectionHelper {
    private ORMReflectionHelper() {

    }

    static boolean isJPAEntity(Class<?> clazz) {
        return  clazz.getAnnotation(Entity.class) != null;
    }

    static String getMappedTableName(Class<?> clazz) {
        Annotation tableAnnotation = clazz.getAnnotation(Table.class);
        return tableAnnotation == null? null: ((Table)tableAnnotation).name();
    }

    private static boolean isTableColumn(Field field) {
        return getColumnAnnotation(field) != null;
    }

    private static Column getColumnAnnotation(Field field) {
        return field.getAnnotation(Column.class);
    }

    private static boolean isSimpleColumn(Field field) {
        return field.getAnnotation(OneToOne.class) == null && field.getAnnotation(OneToMany.class) == null;
    }

    static String getIdFieldName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                Column columnAnnotation = getColumnAnnotation(field);
                if (columnAnnotation != null){
                    String name = (columnAnnotation).name();
                    return (name.isEmpty()? field.getName(): name);
                }
            }
        }
        return null;
    }

    private static boolean isSimpleObject(Class clazz) {
        return (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) ||
                clazz == String.class || clazz == Character.class || clazz == Boolean.class);
    }

    static void walOnTableColumnFields(Class clazz, TableColumnClassFieldHandler handler) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!isTableColumn(field)) continue;
            if (!isSimpleObject(field.getType())) continue;

            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Column columnAnnotation = ORMReflectionHelper.getColumnAnnotation(field);
            if (isSimpleColumn(field)) {
                handler.handle(field, columnAnnotation);
            }

            field.setAccessible(isAccessible);
        }
    }

    static List<Object> getFieldsValues(Object obj, List<DBFieldToClassFieldRelation> fields) throws IllegalAccessException {
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
