package ru.otus.homework14.sort.methods;

public class QuickSortMethod implements SortMethod{

    @Override
    public void sort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    private void quickSort(int[] a, int l, int r) {
        int i, j, p;

        do {
            i = l;
            j = r;
            p = (l + r) >> 1;

            do {
                while (Integer.compare(a[i], a[p]) < 0) i++;

                while (Integer.compare(a[j], a[p]) > 0) j--;

                if (i <= j) {
                    exchangeItems(a, i, j);

                    if (p == i)
                        p = j;
                    else if (p == j)
                        p = i;
                    i++;
                    j--;
                }
            } while (i <= j);

            if (l < j) quickSort(a, l, j);

            l = i;
        }
        while (i < r) ;
    }

}
