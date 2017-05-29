package ru.otus.homework08.json.helper;

import java.util.*;

public class TypeFactory {

    private TypeFactory() {
    }

    static Object getSimpleTypeValueFromString(Class fieldTypeClass, String value) {
        if (String.class.isAssignableFrom(fieldTypeClass))
            return value;
        else if (boolean.class.isAssignableFrom(fieldTypeClass) || Boolean.class.isAssignableFrom(fieldTypeClass)) {
            return Boolean.parseBoolean(value);
        }
        else if (byte.class.isAssignableFrom(fieldTypeClass) || Byte.class.isAssignableFrom(fieldTypeClass)) {
            return Byte.parseByte(value);
        }
        else if (short.class.isAssignableFrom(fieldTypeClass) || Short.class.isAssignableFrom(fieldTypeClass)) {
            return Short.parseShort(value);
        }
        else if (int.class.isAssignableFrom(fieldTypeClass) || Integer.class.isAssignableFrom(fieldTypeClass)) {
            return Integer.parseInt(value);
        }
        else if (long.class.isAssignableFrom(fieldTypeClass) || Long.class.isAssignableFrom(fieldTypeClass)) {
            return Long.parseLong(value);
        }
        else if (float.class.isAssignableFrom(fieldTypeClass) || Float.class.isAssignableFrom(fieldTypeClass)) {
            return Float.parseFloat(value);
        }
        else if (double.class.isAssignableFrom(fieldTypeClass) || Double.class.isAssignableFrom(fieldTypeClass)) {
            return Double.parseDouble(value);
        }
        return null;
    }


    static Collection createCollection(Class collectionTypeClass) {
        Collection collection;

        if (List.class.isAssignableFrom(collectionTypeClass)) {
            collection = new ArrayList();
        }
        else if (Set.class.isAssignableFrom(collectionTypeClass)) {
            collection = new HashSet();
        }
        else {
            collection = new PriorityQueue();
        }
        return collection;
    }

}
