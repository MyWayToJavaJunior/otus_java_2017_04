package ru.otus.homework08.json.helper;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class JSONSerializer {
    private static final String MAP_ELEM_KEY_HDR = "key";
    private static final String MAP_ELEM_VALUE_HDR = "val";

    private FieldType getFieldType(Class fieldClass) {
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

    private JSONArray arrayToJSONArray(Object array) {
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

    private JSONArray mapToJSONArray(Object map) {
        JSONArray jsonArray = new JSONArray();

        Map<Object, Object> m = (Map) map;
        for (Map.Entry<Object, Object> mapElem : m.entrySet()) {
            if (getFieldType(mapElem.getKey().getClass()) != FieldType.Simple) continue;

            FieldType elemType = getFieldType(mapElem.getValue().getClass());
            JSONObject jsonMapElem = new JSONObject();

            jsonMapElem.put(MAP_ELEM_KEY_HDR, mapElem.getKey().toString());
            if (elemType == FieldType.Simple){
                jsonMapElem.put(MAP_ELEM_VALUE_HDR, mapElem.getValue().toString());
            }
            else {
                jsonMapElem.put(MAP_ELEM_VALUE_HDR, objectToJSON(mapElem.getValue()));
            }
            jsonArray.add(jsonMapElem);
        }

        return jsonArray;
    }

    private void fieldToJSON(JSONObject json, String fieldName, Object field) {
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

    public JSONObject objectToJSON(Object object) {
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


    private Object JSONArrayToObject(Class arrayElemTypeClass, JSONArray jsonArray) {
        Object array = Array.newInstance(arrayElemTypeClass, jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            FieldType arrayElemFieldType = getFieldType(arrayElemTypeClass);
            if (arrayElemFieldType == FieldType.Simple) {
                Array.set(array, i, TypeFactory.getSimpleTypeValueFromString(arrayElemTypeClass, jsonArray.get(i).toString()));
            }
            else {
                try {
                    Object elem = arrayElemTypeClass.newInstance();
                    JSONToObject((JSONObject) jsonArray.get(i), elem);
                    Array.set(array, i, elem);
                } catch (InstantiationException | IllegalAccessException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return array;
    }

    private Collection JSONArrayToCollection(Field field, JSONArray jsonArray) {
        Class<?> arrayElemTypeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

        Object array = JSONArrayToObject(arrayElemTypeClass, jsonArray);
        Collection collection = TypeFactory.createCollection(field.getType());

        for (int i = 0; i < Array.getLength(array); i++) {
            collection.add(Array.get(array, i));
        }

        return collection;
    }

    private Map JSONArrayToMap(Field field, JSONArray jsonArray) throws IllegalAccessException, InstantiationException {
        Class<?> mapKeyTypeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Class<?> mapValueTypeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];

        Map map = new HashMap<>();
        if (getFieldType(mapKeyTypeClass) == FieldType.Simple) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonArrayElem = (JSONObject) jsonArray.get(i);
                Object mapKey = TypeFactory.getSimpleTypeValueFromString(mapKeyTypeClass, jsonArrayElem.get(MAP_ELEM_KEY_HDR).toString());
                Object mapValue = jsonArrayElem.get(MAP_ELEM_VALUE_HDR);

                if (getFieldType(mapValueTypeClass) == FieldType.Simple) {
                    map.put(mapKey, TypeFactory.getSimpleTypeValueFromString(mapKeyTypeClass, mapValue.toString()));
                }
                else {
                    Object obj = mapValueTypeClass.newInstance();
                    JSONToObject((JSONObject) mapValue, obj);
                    map.put(mapKey, obj);
                }
            }

        }
        return map;
    }

    public void JSONToObject(JSONObject json, Object object) {

        for (Object key : json.keySet()) {
            String fieldName = key.toString();

            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                Class fieldTypeClass = field.getType();
                FieldType fieldType = getFieldType(fieldTypeClass);
                Object value = json.get(key);

                if (value == null) {
                    field.set(object, null);
                }
                else if (fieldType == FieldType.Simple) {
                    field.set(object, TypeFactory.getSimpleTypeValueFromString(fieldTypeClass, value.toString()));
                }
                else if (value.getClass() == JSONArray.class) {
                    if (fieldType == FieldType.Array) {
                        field.set(object, JSONArrayToObject(fieldTypeClass.getComponentType(), (JSONArray) value));
                    }
                    else if (fieldType == FieldType.Collection) {
                        field.set(object, JSONArrayToCollection(field, (JSONArray)value));
                    }
                    else if (fieldType == FieldType.Map) {
                        Map map = JSONArrayToMap(field, (JSONArray) value);
                        field.set(object, map);
                    }
                }
                else if (fieldType == FieldType.Unknow) {
                    Object obj = fieldTypeClass.newInstance();
                    JSONToObject((JSONObject) value, obj);
                    field.set(object, obj);
                }
            } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                System.err.println(e.getMessage());
            }

        }
    }

}
