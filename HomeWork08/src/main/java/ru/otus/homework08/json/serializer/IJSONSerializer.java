package ru.otus.homework08.json.serializer;

import org.json.simple.JSONObject;

public interface IJSONSerializer {
    JSONObject serializeObjectToJSON(Object object);
    Object deserializeJSONToObject(JSONObject json, Class objectClass);
}
