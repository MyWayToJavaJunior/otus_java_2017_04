package ru.otus.homework07.ATM;

public interface ATMBase {
    int getAvaivableCash();
    void restoreFromInitialState();
    void saveInitialState();
}
