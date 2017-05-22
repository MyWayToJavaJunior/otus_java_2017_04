package ru.otus.homework07;

public interface ATMBase {
    int getAvaivableCash();
    void restoreFromInitialState();
    void saveInitialState();
}
