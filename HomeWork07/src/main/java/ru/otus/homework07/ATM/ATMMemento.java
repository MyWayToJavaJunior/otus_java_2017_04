package ru.otus.homework07.ATM;

import java.util.ArrayList;
import java.util.List;

class ATMMemento {
    private final List<MoneyCluster> state;

    ATMMemento(List<MoneyCluster> state) {
        this.state = new ArrayList<>();
        for (MoneyCluster c: state) {
            this.state.add(new MoneyCluster(c));
        }
    }

    List<MoneyCluster> getState() {
        return state;
    }
}
