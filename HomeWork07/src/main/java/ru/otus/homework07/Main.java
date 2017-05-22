package ru.otus.homework07;

import ru.otus.homework07.ATM.ATM;
import ru.otus.homework07.ATM.ATMBase;
import ru.otus.homework07.ATM.ATMDepartment;

public class Main {

    private static void printAvaivableCashDetails(ATMDepartment department) {
        int i = 1;
        for (ATM atm : department.getAtms()) {
            System.out.println(String.format("Доступные средства в АТМ%s - %s", i++, atm.getAvaivableCash()));
        }
        System.out.println(String.format("Доступные средства во всех ATM -  %s", department.getAvaivableCash()));
    }

    private static void printWithdrawProcess(ATMDepartment department, int atmNum, int cash) {
        if (department.getAtms().size() <= atmNum) return;

        System.out.format("Снятие %s с ATM %s...", cash, atmNum + 1);
        boolean res = department.getAtms().get(atmNum).withdraw(cash);
        System.out.println(res? "успех": "провал");
    }

    public static void main(String... args) {

        ATMBase atmDepartment = new ATMDepartment();

        printAvaivableCashDetails((ATMDepartment) atmDepartment);
        System.out.println();

        int atmNum = 0;
        printWithdrawProcess((ATMDepartment) atmDepartment, atmNum++, 6400);
        printWithdrawProcess((ATMDepartment) atmDepartment, atmNum++, 32000);
        printWithdrawProcess((ATMDepartment) atmDepartment, atmNum++, 36450);

        System.out.println();
        printAvaivableCashDetails((ATMDepartment) atmDepartment);

        System.out.println();
        System.out.println("Восстановление до первоначального состояния");
        atmDepartment.restoreFromInitialState();
        printAvaivableCashDetails((ATMDepartment) atmDepartment);
    }
}
