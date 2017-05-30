package ru.otus.homework08.json.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static Class<?> getFieldGenericType(Field field, int argumentNum) {
        Type[] arguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        if (argumentNum >= arguments.length) return null;

        Class<?> res = (Class<?>)arguments[argumentNum];
        return res;
    }
}
