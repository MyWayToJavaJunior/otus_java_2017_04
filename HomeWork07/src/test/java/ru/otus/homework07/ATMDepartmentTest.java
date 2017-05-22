package ru.otus.homework07;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework07.ATM.ATM;
import ru.otus.homework07.ATM.ATMDepartment;

public class ATMDepartmentTest {

    @Test
    public void ATMDepartmentTestCreate() {
        int[] denominations = {10, 20, 30};

        ATM[] atms = new ATM[2];
        for (int i = 1; i <= atms.length; i++) {
            atms[i - 1] = new ATM(denominations[0] * i, denominations[1] * i, denominations[2] * i);
        }

        ATMDepartment department = new ATMDepartment(atms);
        Assert.assertTrue(department.getAtms() != null);
        Assert.assertEquals(atms.length, department.getAtms().size());

        int i = 1;
        for (ATM atm: department.getAtms()) {
            Assert.assertEquals(denominations.length, atm.getDenominationsSupported().length);
            for (int j = 0; j < denominations.length; j++) {
                Assert.assertEquals(denominations[j] * i, atm.getDenominationsSupported()[j]);
            }
            i++;
        }
    }

    @Test
    public void ATMDepartmentTestGetAvaivableCash() {
        int amount = 10;
        int[] denominations = {10, 50, 100};

        ATMDepartment department = new ATMDepartment(new ATM(denominations[0]), new ATM(denominations[1]), new ATM(denominations[2]));

        int expected = 0;
        for (int i = 0; i < department.getAtms().size(); i++) {
            department.getAtms().get(i).load(denominations[i], amount);
            expected += denominations[i] * amount;
        }
        Assert.assertEquals(expected, department.getAvaivableCash());
    }

    @Test
    public void ATMDepartmentTestRestore() {
        ATMDepartment department = new ATMDepartment();

        int expected = department.getAvaivableCash();
        department.getAtms().get(0).deposit(50, 1000);
        Assert.assertFalse(expected == department.getAvaivableCash());

        department.restoreFromInitialState();
        Assert.assertEquals(expected, department.getAvaivableCash());
    }
}
