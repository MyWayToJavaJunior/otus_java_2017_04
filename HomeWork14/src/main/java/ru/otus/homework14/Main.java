package ru.otus.homework14;

import ru.otus.homework14.sort.methods.*;

import java.util.Arrays;

public class Main {

    private static final int TEST_ARRAY_ELEMENTS_COUNT = 8_000;

    public static void main(String[] args) {
        int[] arr = ArraysHelper.randomArr(TEST_ARRAY_ELEMENTS_COUNT);
        int[] tmpArr;
        long l;

        SortMethod bubbleSortMethod = new BubbleSortMethod();
        SortMethod combSortMethod = new CombSortMethod();
        SortMethod quickSortMethod = new QuickSortMethod();
        SortMethod shellsSortMethod = new ShellsSortMethod();
        ArraysSorter sorter = new ArraysSorter();

        SortMethod method = bubbleSortMethod;

        tmpArr = Arrays.copyOf(arr, arr.length);
        l = System.currentTimeMillis();
        sorter.sort(tmpArr, method);
        l = System.currentTimeMillis() - l;
        System.out.println("serial: " + l + "ms; sorted: " + ArraysHelper.isArraySorted(tmpArr));

        tmpArr = Arrays.copyOf(arr, arr.length);
        l = System.currentTimeMillis();
        sorter.sortParallel(tmpArr, method);
        l = System.currentTimeMillis() - l;
        System.out.println("multithread: " + l + "ms; sorted: " + ArraysHelper.isArraySorted(tmpArr));
    }
}
