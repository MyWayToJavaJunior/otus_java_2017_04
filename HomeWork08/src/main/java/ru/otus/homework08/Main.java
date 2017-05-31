package ru.otus.homework08;


import org.json.simple.JSONObject;
import ru.otus.homework08.json.serializer.FieldType;
import ru.otus.homework08.json.serializer.IJSONSerializer;
import ru.otus.homework08.json.serializer.JSONSerializer;
import ru.otus.homework08.working.classes.SerializebleObject;


public class Main {


    public static void main(String[] args) {

        System.out.println(FieldType.getForClass(Character.class));
        System.out.println(FieldType.getForClass(Boolean.class));
        System.out.println(FieldType.getForClass(Byte.class));

        IJSONSerializer serializer = new JSONSerializer();

        SerializebleObject serializebleObject = new SerializebleObject();
        serializebleObject.fillPrivateCollectionsByFakeData();

        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);

        System.out.println(serializebleObject);
        System.out.println(deSerializebleObject);

        System.out.println(jsonObject.toJSONString());
    }
}
