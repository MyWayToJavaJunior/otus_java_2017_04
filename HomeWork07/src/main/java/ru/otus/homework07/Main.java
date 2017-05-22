package ru.otus.homework07;

public class Main {
    public static void main(String... args) {

        /*
        Посмотреть паттерны у tully
        Возможно переделать сохранение состояния
        Сделать ATMComposition по соотвествующему паттерну {getAvaivableCash(), restoreFromInitialState()}
        Написать тесты для MoneyCluster, ATM, ATMComposition

        Уточнить задание и все переделать

        11x50
        550

        * 550
        * cnt = 550/50 = 11
        * cnt = 10
        * cash = 550 - 500 = 50

        10x50
        10x10
        600

        * 110
        * cnt = 110/50 = 2
        * cnt = 2
        * cahs = 110 - 100 = 10



        * */

        ATM atm1 = new ATM(100, 1000, 50, 500);
        ATM atm2 = new ATM(100, 10, 1000, 50, 500);

        atm1.load(50, 10);
        atm1.saveInitialState();

        atm2.load(50, 10);
        atm2.load(10, 10);
        atm2.saveInitialState();

        System.out.println(String.format("Доступные средства в АТМ1 =  %s", atm1.getAvaivableCash()));
        System.out.println(String.format("Доступные средства в АТМ2 = %s", atm2.getAvaivableCash()));

        int cash;
        boolean res;


        cash = 500;
        System.out.println(String.format("Снимаем %s р с АТМ1", cash));
        res = atm1.withdraw(cash);
        System.out.println(res? "Успех": "Провал");
        System.out.println(String.format("Доступные средства в АТМ1 =  %s", atm1.getAvaivableCash()));

        System.out.println();

        cash = 600;
        System.out.println(String.format("Снимаем %s р с АТМ2", cash));
        res = atm2.withdraw(cash);
        System.out.println(res? "Успех": "Провал");
        System.out.println(String.format("Доступные средства в АТМ2 = %s", atm2.getAvaivableCash()));

        System.out.println();

        System.out.println("Восстановление до первоначального состояния");
        atm1.restoreFromInitialState();
        atm2.restoreFromInitialState();

        System.out.println(String.format("Доступные средства в АТМ1 =  %s", atm1.getAvaivableCash()));
        System.out.println(String.format("Доступные средства в АТМ2 = %s", atm2.getAvaivableCash()));
    }
}
