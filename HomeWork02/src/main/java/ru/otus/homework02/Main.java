package ru.otus.homework02;

import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws Exception {

        ObjectSizeMeter objectSizeMeter = new ObjectSizeMeter();

        Supplier<Object> emptyStringSupplier = () -> new String(new char[0]);
        long sz = objectSizeMeter.measure(emptyStringSupplier);
        System.out.println("Empty string size: " + sz);

        Supplier<Object> notAnEmptyStringSupplier = () -> new String("TestStrTestStrTestStrTestStrTestStrTestStrTestStrTestStrTestStrTestStrTestStr".getBytes());
        sz = objectSizeMeter.measure(notAnEmptyStringSupplier);
        System.out.println("Not an empty string size: " + sz);

        Supplier<Object> emptyArraySupplier = () -> new int[0];
        sz = objectSizeMeter.measure(emptyArraySupplier);
        System.out.println("Empty arr size: " + sz);

        Supplier<Object> notAnEmptyArraySupplier = () -> new int[1000];
        sz = objectSizeMeter.measure(notAnEmptyArraySupplier);
        System.out.println("Not an empty array size: " + sz);
    }
}
