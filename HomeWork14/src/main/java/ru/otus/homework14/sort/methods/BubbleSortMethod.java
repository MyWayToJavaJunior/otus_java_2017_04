package ru.otus.homework14.sort.methods;

public class BubbleSortMethod implements SortMethod{

    @Override
    public void sort(int[] a) {
        boolean end = false;
        while (!end) {
            end = true;
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > a[i + 1]) {
                    exchangeItems(a, i, i + 1);
                    end = false;
                }
            }
        }
    }

}
