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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dummy dummy = (Dummy) o;

        return num == dummy.num;
    }

    @Override
    public int hashCode() {
        return num;
    }
}
