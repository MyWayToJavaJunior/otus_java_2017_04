package ru.otus.homework07.ATM;

import java.util.*;

public class ATM implements ATMBase{

    private int[] denominationsSupported;
    private List<MoneyCluster> moneyClusters;
    private ATMMemento initialState;


    public ATM(int... denominationsSupported) {
        this.denominationsSupported = denominationsSupported;

        Map<Integer, MoneyCluster> tmp = new HashMap<>();
        for (int denomination : denominationsSupported) {
            if (denomination <= 0) continue;

            tmp.put(denomination, new MoneyCluster(denomination));
        }

        moneyClusters = new ArrayList<>(tmp.values());
        linkMoneyClusters();
        saveInitialState();
    }

    private void linkMoneyClusters() {
        moneyClusters.sort(Collections.reverseOrder());

        MoneyCluster prevCluster = null;
        for (MoneyCluster cluster : moneyClusters) {
            if (prevCluster != null) {
                prevCluster.setNextCluster(cluster);
            }

            cluster.setPrevCluster(prevCluster);
            cluster.setNextCluster(null);

            prevCluster = cluster;
        }
    }

    public int[] getDenominationsSupported() {
        return denominationsSupported;
    }

    public boolean deposit(int denomination, int amount) {
        return !(moneyClusters == null || moneyClusters.size() == 0) && moneyClusters.get(0).deposit(denomination, amount);
    }

    public boolean load(int denomination, int amount) {
        return !(moneyClusters == null || moneyClusters.size() == 0) && moneyClusters.get(0).setAmount(denomination, amount);
    }

    public boolean withdraw(int cash) {
        return !(moneyClusters == null || moneyClusters.size() == 0) && moneyClusters.get(0).withdraw(cash);
    }

    public int getAvaivableCash() {
        if (moneyClusters == null || moneyClusters.size() == 0) return 0;
        return moneyClusters.get(0).getAvaivableCash();
    }

    private ATMMemento saveToMemento() {
        return new ATMMemento(moneyClusters);
    }

    private void setMemento(ATMMemento memento) {
        moneyClusters.clear();
        memento.getState().forEach(c -> this.moneyClusters.add(new MoneyCluster(c)));
        linkMoneyClusters();
    }

    public void saveInitialState() {
        initialState = saveToMemento();
    }

    public void restoreFromInitialState() {
        setMemento(initialState);
    }
}
