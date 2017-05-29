package ru.otus.homework08;


import org.json.simple.JSONObject;
import ru.otus.homework08.json.helper.JSONHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        SerializebleObject object = new SerializebleObject();
        object.fillPrivateCollectionsByFakeData();

        JSONObject jsonObject = JSONHelper.objectToJSON(object);
        //System.out.println(jsonObject.toJSONString());

        object = new SerializebleObject(0);
        System.out.println(object);
        JSONHelper.JSONToObject(jsonObject, object);
        System.out.println(object);
    }
}
