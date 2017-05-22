package ru.otus.homework07.ATM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMDepartment implements ATMBase {
    private final List<ATM> atms = new ArrayList<>();

    public ATMDepartment() {
        atms.add(new ATM(50, 100, 500, 1000));
        atms.add(new ATM(10, 50, 100, 500, 1000));
        atms.add(new ATM(10, 50, 100, 500, 5000));

        int atmNum = 0;
        atms.get(atmNum).load(50, 100);
        atms.get(atmNum).load(100, 50);
        atms.get(atmNum).load(500, 25);
        atms.get(atmNum).load(1000, 10);

        atmNum++;
        atms.get(atmNum).load(10, 300);
        atms.get(atmNum).load(50, 150);
        atms.get(atmNum).load(100, 75);
        atms.get(atmNum).load(500, 37);
        atms.get(atmNum).load(1000, 18);

        atmNum++;
        atms.get(atmNum).load(10, 400);
        atms.get(atmNum).load(50, 200);
        atms.get(atmNum).load(100, 100);
        atms.get(atmNum).load(500, 50);
        atms.get(atmNum).load(1000, 25);
        atms.get(atmNum).load(5000, 12);

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
}
