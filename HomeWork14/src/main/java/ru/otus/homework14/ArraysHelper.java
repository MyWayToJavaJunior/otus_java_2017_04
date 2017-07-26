package ru.otus.homework14;

import java.util.Random;

public class ArraysHelper {

    public static boolean isArraySorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] randomArr(int elemsCnt) {
        Random random = new Random();
        return random.ints(0, elemsCnt).limit(elemsCnt).toArray();
    }

    public static int[] serialArr(int elemsCnt) {
        int[] a = new int[elemsCnt];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        return a;
    }
}
