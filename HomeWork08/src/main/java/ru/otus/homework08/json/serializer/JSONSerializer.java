package ru.otus.homework08.json.serializer;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.*;
import java.util.*;

/*
* Класс гарантировано не работает с:
*   простыми объектами (т.е. serializeObjectToJSON примитивный тип, обертку, строку или коллекцию ниего хорошего не произойдет )
*   объектами содержащими поля с типом "внутренний класс"
*   объектами содержащими поля типа Map, у которых тип ключа не примитивный тип, не обертка и не строка
*   объектами содержащими поля с типами "коллекция коллекций" и "карты карт"
*/

public class JSONSerializer implements IJSONSerializer {
    private static final String MAP_ELEM_KEY_HDR = "key";
    private static final String MAP_ELEM_VALUE_HDR = "val";
    private static final String COLLECTION_ELEM_KEY_HDR = "elem";

    private JSONArray arrayToJSONArray(Object array) {
        JSONArray jsonArray = new JSONArray();
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            Object arrayElem = Array.get(array, i);
            FieldType elemType = FieldType.getForClass(arrayElem.getClass());

            if (elemType == FieldType.Simple){
                jsonArray.add(arrayElem.toString());
            }
            else {
                jsonArray.add(serializeObjectToJSON(arrayElem));
            }
        }
        return jsonArray;
    }

    private JSONArray mapToJSONArray(Object map) {
        JSONArray jsonArray = new JSONArray();

        Map<Object, Object> m = (Map) map;
        for (Map.Entry<Object, Object> mapElem : m.entrySet()) {
            if (FieldType.getForClass(mapElem.getKey().getClass()) != FieldType.Simple) continue;

            JSONObject jsonMapElem = new JSONObject();
            jsonMapElem.put(MAP_ELEM_KEY_HDR, mapElem.getKey().toString());
            if (FieldType.getForClass(mapElem.getValue().getClass()) == FieldType.Simple){
                jsonMapElem.put(MAP_ELEM_VALUE_HDR, mapElem.getValue().toString());
            }
            else {
                jsonMapElem.put(MAP_ELEM_VALUE_HDR, serializeObjectToJSON(mapElem.getValue()));
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

        FieldType fieldType = FieldType.getForClass(field.getClass());
        if (fieldType == FieldType.Simple) {
            json.put(fieldName, field.toString());
        }
        else if (fieldType == FieldType.Array || fieldType == FieldType.Collection){
            Object array = (fieldType == FieldType.Array)? field : ((Collection)field).toArray();
            json.put(fieldName, arrayToJSONArray(array));
        }
        else if (fieldType == FieldType.Map){
            json.put(fieldName, mapToJSONArray(field));
        }
        else {
            json.put(fieldName, serializeObjectToJSON(field));
        }
    }

    public JSONObject serializeObjectToJSON(Object object) {
        JSONObject json = new JSONObject();


        if (FieldType.getForClass(object.getClass()) != FieldType.Unknow) {
            fieldToJSON(json, COLLECTION_ELEM_KEY_HDR, object);
        }
        else {
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
        }
        return json;
    }

    private Object JSONArrayToObjectArray(Class arrayElemTypeClass, JSONArray jsonArray) {
        FieldType arrayElemFieldType = FieldType.getForClass(arrayElemTypeClass);
        Object array = Array.newInstance(arrayElemTypeClass, jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            if (arrayElemFieldType == FieldType.Simple) {
                Array.set(array, i, TypeFactory.getSimpleTypeValueFromString(arrayElemTypeClass, jsonArray.get(i).toString()));
            }
            else {
                Object elem = deserializeJSONToObject((JSONObject) jsonArray.get(i), arrayElemTypeClass);
                Array.set(array, i, elem);
            }
        }
        return array;
    }

    private Collection JSONArrayToCollection(Field field, JSONArray jsonArray) {
        Object array = JSONArrayToObjectArray(ReflectionHelper.getFieldGenericType(field, 0), jsonArray);
        return TypeFactory.createCollection(field.getType(), ((Object[]) array));
    }

    private Map JSONArrayToMap(Field field, JSONArray jsonArray) throws IllegalAccessException, InstantiationException {
        Class<?> mapKeyTypeClass = ReflectionHelper.getFieldGenericType(field, 0);
        Class<?> mapValueTypeClass = ReflectionHelper.getFieldGenericType(field, 1);

        HashMap<Object, Object> map = new HashMap<>();
        if (FieldType.getForClass(mapKeyTypeClass) == FieldType.Simple) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonArrayElem = (JSONObject) jsonArray.get(i);
                Object mapKey = TypeFactory.getSimpleTypeValueFromString(mapKeyTypeClass, jsonArrayElem.get(MAP_ELEM_KEY_HDR).toString());
                Object mapValue = jsonArrayElem.get(MAP_ELEM_VALUE_HDR);

                if (FieldType.getForClass(mapValueTypeClass) == FieldType.Simple) {
                    map.put(mapKey, TypeFactory.getSimpleTypeValueFromString(mapValueTypeClass, mapValue.toString()));
                }
                else {
                    Object obj = deserializeJSONToObject((JSONObject) mapValue, mapValueTypeClass);
                    map.put(mapKey, obj);
                }
            }
        }
        return map;
    }

    public Object deserializeJSONToObject(JSONObject json, Class objectClass) {
        Object object = TypeFactory.createNewObject(objectClass);
        if (object == null) return null;

        for (Object key : json.keySet()) {
            String fieldName = key.toString();

            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                Class fieldTypeClass = field.getType();
                FieldType fieldType = FieldType.getForClass(fieldTypeClass);
                Object value = json.get(key);

                if (value == null) {
                    field.set(object, null);
                }
                else if (fieldType == FieldType.Simple) {
                    field.set(object, TypeFactory.getSimpleTypeValueFromString(fieldTypeClass, value.toString()));
                }
                else if (value.getClass() == JSONArray.class) {
                    if (fieldType == FieldType.Array) {
                        field.set(object, JSONArrayToObjectArray(fieldTypeClass.getComponentType(), (JSONArray) value));
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
                    Object obj = deserializeJSONToObject((JSONObject) value, fieldTypeClass);
                    field.set(object, obj);
                }
            } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                System.err.println(e.getMessage());
            }

        }
        return object;
    }

}
