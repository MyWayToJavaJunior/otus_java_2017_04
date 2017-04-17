package ru.otus.homework03;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        NewArrayList<Integer> list = new NewArrayList<>();
        LinkedList<Integer> target = new LinkedList<>();

        Random r = new Random();
        Integer[]  tmp = new Integer[5];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i]  = r.nextInt(100);
        }

        Collections.addAll(target, tmp);

        Collections.addAll(list, tmp);
        System.out.println("Our list and copy target before sort");
        System.out.println(list.toString());

        System.out.println();

        Collections.sort(list);
        System.out.println("Our list after sort");
        System.out.println(list.toString());

        System.out.println();

        Collections.copy(target, list);
        System.out.println("Copy target after copy");
        System.out.println(Arrays.toString(target.toArray()));

    }
}
