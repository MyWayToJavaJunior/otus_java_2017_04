package ru.otus.homework07;

public class MoneyCluster implements Comparable<MoneyCluster> {
    private int denomination;
    private int amount;

    private MoneyCluster prevCluster;
    private MoneyCluster nextCluster;

    public MoneyCluster(int denomination) {
        this.denomination = denomination;
        amount = 0;
    }

    public MoneyCluster(int denomination, int amount) {
        this.denomination = denomination;
        this.amount = amount;
    }

    public MoneyCluster(MoneyCluster cluster) {
        this.denomination = cluster.getDenomination();
        this.amount = cluster.getAmount();
    }

    public int getDenomination() {
        return denomination;
    }

    public int getAmount() {
        return amount;
    }


    public void setPrevCluster(MoneyCluster prevCluster) {
        this.prevCluster = prevCluster;
    }

    public void setNextCluster(MoneyCluster nextCluster) {
        this.nextCluster = nextCluster;
    }

    public boolean setAmount(int denomination, int amount) {
        if (this.denomination == denomination) {
            this.amount = amount;
            return true;
        }
        else if (nextCluster != null) {
            return nextCluster.setAmount(denomination, amount);
        }
        return false;
    }

    public boolean deposit(int denomination, int amount) {
        if (this.denomination == denomination) {
            this.amount += amount;
            return true;
        }
        else if (nextCluster != null) {
            return nextCluster.deposit(denomination, amount);
        }
        return false;
    }

    public boolean withdraw(int cash) {
        if (cash <= 0) return true;

        if (denomination > 0 && amount > 0) {
            int cnt = cash / denomination;
            cnt = (cnt <= amount || cnt == 0)? cnt: amount;

            if (cnt > 0) {
                cash -= cnt * denomination;
                boolean res = (((nextCluster != null) && nextCluster.withdraw(cash)) || (cash == 0));
                if (res) {
                    this.amount -= cnt;
                }
                return res;
            }
        }
        return (nextCluster != null) && nextCluster.withdraw(cash);
    }


    public int getAvaivableCash() {
        return (amount * denomination) + ((nextCluster == null)? 0 : nextCluster.getAvaivableCash());
    }

    @Override
    public int compareTo(MoneyCluster o) {
        return Integer.compare(this.denomination, o.denomination);
    }
}
