package ru.otus.homework07;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework07.ATM.MoneyCluster;

public class MoneyClusterTest {

    @Test
    public void MoneyClusterTestCreate() {
        int denomination = 10;
        int amount = 100;

        MoneyCluster cluster = new MoneyCluster(denomination, amount);
        Assert.assertEquals(amount, cluster.getAmount());
        Assert.assertEquals(denomination, cluster.getDenomination());
        Assert.assertEquals(denomination * amount, cluster.getAvaivableCash());
    }

    @Test
    public void MoneyClusterTestDepositGood() {
        int denomination = 50;
        int amount = 100;

        MoneyCluster cluster = new MoneyCluster(denomination, 0);
        Assert.assertTrue(cluster.deposit(denomination, amount));
        Assert.assertEquals(denomination * amount, cluster.getAvaivableCash());
    }

    @Test
    public void MoneyClusterTestDepositBad() {
        int denomination = 50;
        int amount = 100;

        MoneyCluster cluster = new MoneyCluster(denomination, 0);
        Assert.assertFalse(cluster.deposit(denomination * 2, amount));
        Assert.assertEquals(0, cluster.getAvaivableCash());
    }

    @Test
    public void MoneyClusterTestWithdrawGood() {
        int denomination = 50;
        int amount = 100;
        int cash = 900;

        MoneyCluster cluster = new MoneyCluster(denomination, amount);

        Assert.assertTrue(cluster.withdraw(cash));
        Assert.assertEquals(denomination * amount - cash, cluster.getAvaivableCash());
    }

    @Test
    public void MoneyClusterTestWithdrawBad1() {
        int denomination = 50;
        int amount = 100;
        int cash = 910;

        MoneyCluster cluster = new MoneyCluster(denomination, amount);

        Assert.assertFalse(cluster.withdraw(cash));
        Assert.assertTrue(cluster.getAvaivableCash() == denomination * amount);
    }


    @Test
    public void MoneyClusterTestWithdrawBad2() {
        int denomination = 50;
        int amount = 100;
        int cash = 5500;

        MoneyCluster cluster = new MoneyCluster(denomination, amount);

        Assert.assertFalse(cluster.withdraw(cash));
        Assert.assertTrue(cluster.getAvaivableCash() == denomination * amount);
    }



}
