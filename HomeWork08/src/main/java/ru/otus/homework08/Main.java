package ru.otus.homework08;


import org.json.simple.JSONObject;
import ru.otus.homework08.json.helper.JSONSerializer;
import ru.otus.homework08.working.classes.SerializebleObject;

public class Main {


    public static void main(String[] args) {

        JSONSerializer serializer = new JSONSerializer();

        SerializebleObject object = new SerializebleObject();
        object.fillPrivateCollectionsByFakeData();

        JSONObject jsonObject = serializer.objectToJSON(object);
        //System.out.println(jsonObject.toJSONString());

        SerializebleObject object2 = new SerializebleObject();
        serializer.JSONToObject(jsonObject, object2);
        System.out.println(object.strCompare(object2, true));
    }
}
