package ru.otus.homework07;

import java.util.*;

public class ATM {
    private int[] denominationsSupported;
    private List<MoneyCluster> moneyClusters;
    private List<MoneyCluster> initialState;


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
        Collections.sort(moneyClusters, Collections.reverseOrder());

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

    public boolean deposit(int denomination, int amount) {
        if (moneyClusters == null || moneyClusters.size() == 0) return false;
        return moneyClusters.get(0).deposit(denomination, amount);
    }

    public boolean load(int denomination, int amount) {
        if (moneyClusters == null || moneyClusters.size() == 0) return false;
        return moneyClusters.get(0).setAmount(denomination, amount);
    }

    public boolean withdraw(int cash) {
        if (moneyClusters == null || moneyClusters.size() == 0) return false;
        return moneyClusters.get(0).withdraw(cash);
    }

    public int getAvaivableCash() {
        if (moneyClusters == null || moneyClusters.size() == 0) return 0;
        return moneyClusters.get(0).getAvaivableCash();
    }

    public void saveInitialState() {
        initialState = new ArrayList<>();
        moneyClusters.forEach(c -> initialState.add(new MoneyCluster(c)));
    }

    public void restoreFromInitialState() {
        moneyClusters.clear();
        initialState.forEach(c -> this.moneyClusters.add(new MoneyCluster(c)));
        linkMoneyClusters();
    }
}
