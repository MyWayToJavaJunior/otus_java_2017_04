package ru.otus.homework14.sort.methods;

public interface SortMethod {
    void sort(int[] a);

    default void exchangeItems(int[] a, int i1, int i2) {
        a[i1] = a[i2] ^ a[i1];
        a[i2] = a[i2] ^ a[i1];
        a[i1] = a[i2] ^ a[i1];

    }

}
