package ru.otus.homework14;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework14.sort.methods.*;

public class ArraySorterTest {
    private static final int TEST_ARRAY_ELEMENTS_COUNT_FOR_BUBBLE_SORT_METHOD = 8_000;
    private static final int TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS = 8_000_000;
    private static final String MSG_ARRAY_NOT_SORTED = "Array not sorted";
    private static final String MSG_MULTITHREADING_SORT_TYPE_SLOWER_THAN_SERIAL = "Multithreading sort type slower than serial";

    @Test
    public void bubbleSortMethodTest(){
        SortMethod sortMethod = new BubbleSortMethod();
        ArraysSorter sorter = new ArraysSorter();
        int[] sourceArr = ArraysHelper.randomArr(TEST_ARRAY_ELEMENTS_COUNT_FOR_BUBBLE_SORT_METHOD);
        int[] workingArr = new int[TEST_ARRAY_ELEMENTS_COUNT_FOR_BUBBLE_SORT_METHOD];
        long serialSortTime, multiThreadingSortTime;

        serialSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sort(workingArr, sortMethod);
        serialSortTime = System.currentTimeMillis() - serialSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        multiThreadingSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sortParallel(workingArr, sortMethod);
        multiThreadingSortTime = System.currentTimeMillis() - multiThreadingSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        Assert.assertTrue(MSG_MULTITHREADING_SORT_TYPE_SLOWER_THAN_SERIAL, multiThreadingSortTime < serialSortTime);
    }

    @Test
    public void combSortMethodTest(){
        SortMethod sortMethod = new CombSortMethod();
        ArraysSorter sorter = new ArraysSorter();
        int[] sourceArr = ArraysHelper.randomArr(TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS);
        int[] workingArr = new int[TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS];
        long serialSortTime, multiThreadingSortTime;

        serialSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sort(workingArr, sortMethod);
        serialSortTime = System.currentTimeMillis() - serialSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        multiThreadingSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sortParallel(workingArr, sortMethod);
        multiThreadingSortTime = System.currentTimeMillis() - multiThreadingSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        Assert.assertTrue(MSG_MULTITHREADING_SORT_TYPE_SLOWER_THAN_SERIAL, multiThreadingSortTime < serialSortTime);
    }

    @Test
    public void quickSortMethodTest(){
        SortMethod sortMethod = new QuickSortMethod();
        ArraysSorter sorter = new ArraysSorter();
        int[] sourceArr = ArraysHelper.randomArr(TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS);
        int[] workingArr = new int[TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS];
        long serialSortTime, multiThreadingSortTime;

        serialSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sort(workingArr, sortMethod);
        serialSortTime = System.currentTimeMillis() - serialSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        multiThreadingSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sortParallel(workingArr, sortMethod);
        multiThreadingSortTime = System.currentTimeMillis() - multiThreadingSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        Assert.assertTrue(MSG_MULTITHREADING_SORT_TYPE_SLOWER_THAN_SERIAL, multiThreadingSortTime < serialSortTime);
    }

    @Test
    public void shellSortMethodTest(){
        SortMethod sortMethod = new ShellsSortMethod();
        ArraysSorter sorter = new ArraysSorter();
        int[] sourceArr = ArraysHelper.randomArr(TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS);
        int[] workingArr = new int[TEST_ARRAY_ELEMENTS_COUNT_FOR_MANY_SORT_METHODS];
        long serialSortTime, multiThreadingSortTime;

        serialSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sort(workingArr, sortMethod);
        serialSortTime = System.currentTimeMillis() - serialSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        multiThreadingSortTime = System.currentTimeMillis();
        System.arraycopy(sourceArr, 0, workingArr, 0, sourceArr.length);
        sorter.sortParallel(workingArr, sortMethod);
        multiThreadingSortTime = System.currentTimeMillis() - multiThreadingSortTime;
        Assert.assertTrue(MSG_ARRAY_NOT_SORTED, ArraysHelper.isArraySorted(workingArr));

        Assert.assertTrue(MSG_MULTITHREADING_SORT_TYPE_SLOWER_THAN_SERIAL, multiThreadingSortTime < serialSortTime);
    }
}
