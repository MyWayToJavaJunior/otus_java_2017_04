package ru.otus.homework14.sort.methods;

public class CombSortMethod implements SortMethod {

    @Override
    public void sort(int[] a) {
        float loadFactor = 1.247f;
        int step = a.length;
        boolean end = false;

        while (!end) {
            end = true;

            step /= loadFactor;
            if (step < 1) {
                step = 1;
            }

            for (int i = 0; i < a.length - 1; i++) {
                if ((i + step) < a.length) {
                    if (a[i] > a[i + step]) {
                        exchangeItems(a, i, i + step);
                        end = false;
                    }
                } else {
                    end = false;
                }

            }

        }
    }

}
