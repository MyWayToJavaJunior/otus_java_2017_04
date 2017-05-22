package ru.otus.homework07;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework07.ATM.ATM;

import java.util.Arrays;

public class ATMTest {

    @Test
    public void ATMTestCreate() {
        int[] denominations = {10, 50, 100};
        ATM atm = new ATM(denominations);
        Assert.assertTrue(Arrays.equals(denominations, atm.getDenominationsSupported()));
    }

    @Test
    public void ATMTestLoadGood() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int expected = 0;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            Assert.assertTrue(atm.load(denominations[i], amount));
            expected += amount * denominations[i];
        }

        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestLoadBad() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int expected = 0;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            Assert.assertFalse(atm.load(denominations[i] + 1, amount));
        }

        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestDepositGood() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int expected = 0;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            Assert.assertTrue(atm.deposit(denominations[i], amount));
            expected += amount * denominations[i];
        }

        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestDepositBad() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int expected = 0;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            Assert.assertFalse(atm.load(denominations[i] + 1, amount));
        }

        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestWithdrawGood() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int cash = 1510;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            atm.load(denominations[i], amount);
        }

        int expected = atm.getAvaivableCash() - cash;
        Assert.assertTrue(atm.withdraw(cash));

        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestWithdrawBad1() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int cash = 1910;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            atm.load(denominations[i], amount);
        }
        int expected = atm.getAvaivableCash();

        Assert.assertFalse(atm.withdraw(cash));
        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestWithdrawBad2() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        int cash = 1515;

        ATM atm = new ATM(denominations);
        for (int i = 0; i < denominations.length; i++) {
            atm.load(denominations[i], amount);
        }
        int expected = atm.getAvaivableCash();

        Assert.assertFalse(atm.withdraw(cash));
        Assert.assertEquals(expected, atm.getAvaivableCash());
    }

    @Test
    public void ATMTestSaveAndResore() {
        int amount = 10;
        int[] denominations = {10, 50, 100};
        ATM atm = new ATM(denominations);

        atm.saveInitialState();

        for (int i = 0; i < denominations.length; i++) {
            atm.load(denominations[i], amount);
        }
        int cash = atm.getAvaivableCash();

        atm.restoreFromInitialState();

        Assert.assertTrue(cash > 0);
        Assert.assertEquals(0, atm.getAvaivableCash());
    }
}
