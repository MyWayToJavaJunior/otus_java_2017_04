package ru.otus.homework08.working.classes;

public class Dummy {
    private int num = 0;

    public Dummy() {
    }

    public Dummy(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Dummy{" +
                "num=" + num +
                '}';
    }
}
