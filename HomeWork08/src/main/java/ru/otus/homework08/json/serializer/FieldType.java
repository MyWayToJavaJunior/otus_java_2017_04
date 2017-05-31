package ru.otus.homework08.json.serializer;

public enum FieldType {
    Simple, Array, Collection, Map, Unknow;

    public static FieldType getForClass(Class fieldClass) {
        boolean isSimple = fieldClass.isPrimitive() || Number.class.isAssignableFrom(fieldClass) ||
                           fieldClass.equals(String.class) || fieldClass.equals(Character.class) ||
                           fieldClass.equals(Boolean.class);
        if (isSimple) {
            return FieldType.Simple;
        }else if (fieldClass.isArray()){
            return FieldType.Array;
        }else if (java.util.Collection.class.isAssignableFrom(fieldClass)) {
            return FieldType.Collection;
        }else if (java.util.Map.class.isAssignableFrom(fieldClass)) {
            return FieldType.Map;
        }else {
            return FieldType.Unknow;
        }
    }

}
