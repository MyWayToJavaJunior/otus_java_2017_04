package ru.otus.homework07;

import ru.otus.homework07.ATM.ATM;
import ru.otus.homework07.ATM.ATMBase;
import ru.otus.homework07.ATM.ATMDepartment;

public class Main {
    public static void main(String... args) {

        ATMBase atmDepartment = new ATMDepartment();

        ((ATMDepartment)atmDepartment).printAvaivableCashDetails();
        System.out.println();

        int atmNum = 0;
        ((ATMDepartment)atmDepartment).printWithdrawProcess(atmNum++, 6400);
        ((ATMDepartment)atmDepartment).printWithdrawProcess(atmNum++, 32000);
        ((ATMDepartment)atmDepartment).printWithdrawProcess(atmNum++, 36450);

        System.out.println();
        ((ATMDepartment)atmDepartment).printAvaivableCashDetails();

        System.out.println();
        System.out.println("Восстановление до первоначального состояния");
        atmDepartment.restoreFromInitialState();
        ((ATMDepartment)atmDepartment).printAvaivableCashDetails();
    }
}
