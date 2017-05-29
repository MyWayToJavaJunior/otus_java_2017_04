package ru.otus.homework08.json.helper;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class JSONHelper {

    static FieldType getFieldType(Class fieldClass) {
        if (fieldClass.isPrimitive() || Number.class.isAssignableFrom(fieldClass) || fieldClass.equals(String.class)) {
            return FieldType.Simple;
        }else if (fieldClass.isArray()){
            return FieldType.Array;
        }else if (Collection.class.isAssignableFrom(fieldClass)) {
            return FieldType.Collection;
        }else if (Map.class.isAssignableFrom(fieldClass)) {
            return FieldType.Map;
        }else {
            return FieldType.Unknow;
        }
    }

    public static JSONObject objectToJSON(Object object) {
        JSONObject json = new JSONObject();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {

            if ((f.getModifiers() & Modifier.FINAL) == Modifier.FINAL) continue;

            f.setAccessible(true);
            try {
                fieldToJSON(json, f.getName(), f.get(object));
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
        }
        return json;
    }

    static JSONArray arrayToJSONArray(Object array) {
        JSONArray jsonArray = new JSONArray();
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            Object arrayElem = Array.get(array, i);
            FieldType elemType = getFieldType(arrayElem.getClass());

            if (elemType == FieldType.Simple){
                jsonArray.add(arrayElem.toString());
            }
            else {
                jsonArray.add(objectToJSON(arrayElem));
            }
        }
        return jsonArray;
    }

    static JSONArray mapToJSONArray(Object map) {
        JSONArray jsonArray = new JSONArray();

        Map<Object, Object> m = (Map<Object, Object>) map;
        for (Map.Entry<Object, Object> mapElem : m.entrySet()) {
            if (getFieldType(mapElem.getKey().getClass()) != FieldType.Simple) continue;

            FieldType elemType = getFieldType(mapElem.getValue().getClass());
            JSONObject jsonMapElem = new JSONObject();
            if (elemType == FieldType.Simple){
                jsonMapElem.put(mapElem.getKey().toString(), mapElem.getValue().toString());
            }
            else {
                jsonMapElem.put(mapElem.getKey().toString(), objectToJSON(mapElem.getValue()));
            }
            jsonArray.add(jsonMapElem);
        }

        return jsonArray;
    }

    static void fieldToJSON(JSONObject json, String fieldName, Object field) {
        if (field == null) {
            json.put(fieldName, null);
            return;
        }

        FieldType fieldType = getFieldType(field.getClass());
        if (fieldType == FieldType.Simple) {
            json.put(fieldName, field.toString());
        }
        else if (fieldType == FieldType.Array){
            json.put(fieldName, arrayToJSONArray(field));
        }
        else if (fieldType == FieldType.Collection){
            json.put(fieldName, arrayToJSONArray(((Collection)field).toArray()));
        }
        else if (fieldType == FieldType.Map){
            json.put(fieldName, mapToJSONArray(field));
        }
        else {
            json.put(fieldName, objectToJSON(field));
        }
    }

    private static Object getSimpleFieldValueFromString(Class fieldTypeClass, String value) {
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

    private static Object JSONArrayToObject(Class arrayElemTypeClass, JSONArray jsonArray) {
        Object array = Array.newInstance(arrayElemTypeClass, jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            FieldType arrayElemFieldType = getFieldType(arrayElemTypeClass);
            if (arrayElemFieldType == FieldType.Simple) {
                Array.set(array, i, getSimpleFieldValueFromString(arrayElemTypeClass, jsonArray.get(i).toString()));
            }
        }
        return array;
    }

    private static Collection createCollection(Class collectionTypeClass) {
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

    public static void JSONToObject(JSONObject json, Object object) {

        for (Object key : json.keySet()) {
            String fieldName = key.toString();

            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                Class fieldTypeClass = field.getType();
                FieldType fieldType = getFieldType(fieldTypeClass);
                Object value = json.get(key);

                if (fieldType == FieldType.Simple) {
                    field.set(object, getSimpleFieldValueFromString(fieldTypeClass, value.toString()));
                }
                else if (value.getClass() == JSONArray.class) {

                    if (fieldType == FieldType.Array) {
                        field.set(object, JSONArrayToObject(fieldTypeClass.getComponentType(), (JSONArray) value));
                    }
                    else if (fieldType == FieldType.Collection) {
                        Class<?> arrayElemTypeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

                        Object array = JSONArrayToObject(arrayElemTypeClass, (JSONArray) value);
                        Collection collection = createCollection(fieldTypeClass);

                        for (int i = 0; i < Array.getLength(array); i++) {
                            collection.add(Array.get(array, i));
                        }
                        field.set(object, collection);
                    }
                    else if (fieldType == FieldType.Map) {
                    }
                }

            } catch (NoSuchFieldException | IllegalAccessException e1) {
                System.err.println(e1.getMessage());
            }

        }
    }

}
