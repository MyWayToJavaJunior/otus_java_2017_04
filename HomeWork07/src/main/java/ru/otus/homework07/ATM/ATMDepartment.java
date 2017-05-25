package ru.otus.homework07.ATM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMDepartment implements ATMBase {
    private final List<ATM> atms = new ArrayList<>();

    public ATMDepartment() {
        final int[] defaultDenominationsForFirstATM = {50, 100, 500, 1000};
        final int[] defaultDenominationsForSecondATM = {10, 50, 100, 500, 1000};
        final int[] defaultDenominationsForThirdATM = {10, 50, 100, 500, 5000};
        final int defaultDenominationLoad = 100;

        atms.add(new ATM(defaultDenominationsForFirstATM));
        atms.add(new ATM(defaultDenominationsForSecondATM));
        atms.add(new ATM(defaultDenominationsForThirdATM));

        for (ATM atm : atms) {
            for (int denomination : atm.getDenominationsSupported()) {
                atm.load(denomination, defaultDenominationLoad);
            }
        }

        saveInitialState();
    }

    public ATMDepartment(ATM... atms) {
        Collections.addAll(this.atms, atms);
    }

    public List<ATM> getAtms() {
        return atms;
    }

    @Override
    public int getAvaivableCash() {
        int res = 0;
        for (ATM atm: atms) {
            res += atm.getAvaivableCash();
        }
        return res;
    }

    @Override
    public void restoreFromInitialState() {
        atms.forEach(ATM::restoreFromInitialState);
    }

    @Override
    public void saveInitialState() {
        atms.forEach(ATM::saveInitialState);
    }


    public void printAvaivableCashDetails() {
        int i = 1;
        for (ATM atm : atms) {
            System.out.println(String.format("Доступные средства в АТМ%s - %s", i++, atm.getAvaivableCash()));
        }
        System.out.println(String.format("Доступные средства во всех ATM -  %s", getAvaivableCash()));
    }

    public void printWithdrawProcess(int atmNum, int cash) {
        if (atms.size() <= atmNum) return;

        System.out.format("Снятие %s с ATM %s...", cash, atmNum + 1);
        boolean res = atms.get(atmNum).withdraw(cash);
        System.out.println(res? "успех": "провал");
    }

}
