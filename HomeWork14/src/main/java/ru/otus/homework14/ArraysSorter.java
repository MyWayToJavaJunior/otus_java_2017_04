package ru.otus.homework14;

import java.util.Arrays;

public class ArraysSorter {
    public static <T> void sort(T[] array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        int step = array.length / 4;

        for (int i = 0; i < array.length; i+=step) {
            int start = i;
            int stop = ((i + step >= array.length)? array.length: i + step) - 1;
            System.out.println(start + " - " + stop);
        }
    }
}
