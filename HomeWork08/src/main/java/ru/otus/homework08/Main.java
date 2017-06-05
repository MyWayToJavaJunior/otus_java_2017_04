package ru.otus.homework08;


import org.json.simple.JSONObject;
import ru.otus.homework08.json.serializer.IJSONSerializer;
import ru.otus.homework08.json.serializer.JSONSerializer;
import ru.otus.homework08.working.classes.SerializebleObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {


    public static void main(String[] args) {
        IJSONSerializer serializer = new JSONSerializer();

        List<List<Integer>> lists = new ArrayList<>();
        lists.add(Arrays.asList(1, 2, 3, 4));
        lists.add(Arrays.asList(5, 6, 7, 8));
        lists.add(Arrays.asList(8, 7, 6, 5));
        lists.add(Arrays.asList(4, 3, 2, 1));

        JSONObject jsonObject0 = serializer.serializeObjectToJSON(lists);
        System.out.println(jsonObject0.toJSONString());

        lists.clear();
        lists = (List<List<Integer>>) serializer.deserializeJSONToObject(jsonObject0, lists.getClass());
        System.out.println(lists);


        /*

        SerializebleObject serializebleObject = new SerializebleObject();
        serializebleObject.fillPrivateCollectionsByFakeData();

        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);

        System.out.println(serializebleObject);
        System.out.println(deSerializebleObject);

        System.out.println(jsonObject.toJSONString());
        */
    }
}
