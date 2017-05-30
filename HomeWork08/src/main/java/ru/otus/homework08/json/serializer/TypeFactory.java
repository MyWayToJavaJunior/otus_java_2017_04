package ru.otus.homework08.json.serializer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TypeFactory {

    private TypeFactory() {
    }

    static Object getSimpleTypeValueFromString(Class fieldTypeClass, String value) {
        if (String.class == fieldTypeClass)
            return value;
        else if (boolean.class == fieldTypeClass || Boolean.class == fieldTypeClass) {
            return Boolean.parseBoolean(value);
        }
        else if (byte.class == fieldTypeClass || Byte.class == fieldTypeClass) {
            return Byte.parseByte(value);
        }
        else if (short.class == fieldTypeClass || Short.class == fieldTypeClass) {
            return Short.parseShort(value);
        }
        else if (int.class == fieldTypeClass || Integer.class == fieldTypeClass) {
            return Integer.parseInt(value);
        }
        else if (long.class == fieldTypeClass || Long.class == fieldTypeClass) {
            return Long.parseLong(value);
        }
        else if (float.class == fieldTypeClass || Float.class == fieldTypeClass) {
            return Float.parseFloat(value);
        }
        else if (double.class == fieldTypeClass || Double.class == fieldTypeClass) {
            return Double.parseDouble(value);
        }
        return null;
    }


    static Collection createCollection(Class collectionTypeClass, Object[] elems) {
        Collection collection = null;

        if (List.class == collectionTypeClass) {
            collection = new ArrayList();
        }
        else if (Set.class == collectionTypeClass) {
            collection = new HashSet();
        }
        else if (Queue.class == collectionTypeClass){
            collection = new PriorityQueue();
        }
        else {
            try {
                collection = (Collection) collectionTypeClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
        }
        if (collection != null) Collections.addAll(collection, elems);
        return collection;
    }

    static Object createNewObject(Class objectClass) {
        Object object = null;
        try {
            object = objectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
        return object;
    }


}
