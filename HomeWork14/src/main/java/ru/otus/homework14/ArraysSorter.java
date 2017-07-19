package ru.otus.homework14;

import ru.otus.homework14.sort.methods.SortMethod;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ArraysSorter {
    public void sort(int[] a, SortMethod method) {
        method.sort(a);
    }

    public void sortParallel(int[] a, SortMethod method) {
        int threadsCnt = 4;
        int partLen = a.length / threadsCnt;

        CountDownLatch latch = new CountDownLatch(threadsCnt);

        SortThread[] threads = new SortThread[threadsCnt];

        for (int i = 0; i < threadsCnt; i++) {
            int from = i * partLen;
            int to = from + partLen;
            if (i == threadsCnt - 1) to = a.length;

            threads[i] = new SortThread(method, a, from, to, latch);
            threads[i].start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        int[] res = threads[0].arr;
        for (int i = 1; i < threadsCnt; i++) {
            res = finalMerge(res, threads[i].arr);
        }

        System.arraycopy(res, 0, a, 0, res.length);

    }


    static class SortThread extends Thread {
        private final SortMethod method;
        private final CountDownLatch latch;
        private final int[] arr;

        public SortThread(SortMethod method, int[] source, int from, int to, CountDownLatch latch) {
            arr = Arrays.copyOfRange(source, from, to);
            this.latch = latch;
            this.method = method;
        }

        @Override
        public void run() {
            method.sort(arr);
            latch.countDown();
        }
    }

    private static int[] finalMerge(int firstArr[], int secondArr[]) {
        int firstArrLen = firstArr.length;
        int secondArrLen = secondArr.length;

        int[] resArr = new int[firstArrLen + secondArrLen];

        int firstArrPos = 0;
        int secondArrPos = 0;

        for (int i = 0; i < resArr.length; i++) {

            if (firstArrPos >= firstArrLen) {
                resArr[i] = secondArr[secondArrPos];
                secondArrPos++;
                continue;
            }

            if (secondArrPos >= secondArrLen) {
                resArr[i] = firstArr[firstArrPos];
                firstArrPos++;
                continue;
            }

            if (firstArr[firstArrPos] < secondArr[secondArrPos]) {
                resArr[i] = firstArr[firstArrPos];
                firstArrPos++;
            } else {
                resArr[i] = secondArr[secondArrPos];
                secondArrPos++;
            }
        }
        return resArr;
    }

}
