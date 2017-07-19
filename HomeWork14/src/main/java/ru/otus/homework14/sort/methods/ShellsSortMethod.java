package ru.otus.homework14.sort.methods;

public class ShellsSortMethod implements SortMethod{
    @Override
    public void sort(int[] a)
    {
        int i, j, k;
        int t;
        int n = a.length;

        for(k = n/2; k > 0; k /=2)
            for(i = k; i < n; i++)
            {
                t = a[i];
                for(j = i; j>=k; j-=k)
                {
                    if(t < a[j-k])
                        a[j] = a[j-k];
                    else
                        break;
                }
                a[j] = t;
            }
    }
}
