package ru.otus.homework04;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GCMonitoring gcMonitoring = new GCMonitoring();
        gcMonitoring.startMonitoring();

        List<Dummy> dummies = new ArrayList<>();
        try {
            while (true) {
                dummies.add(new Dummy());
                dummies.add(new Dummy());
                dummies.add(new Dummy());
                dummies.add(new Dummy());
                try {
                    Thread.sleep(450);
                } catch (InterruptedException e) {
                }
                dummies.remove(0);
                dummies.remove(0);
                dummies.remove(0);
            }
        } catch (OutOfMemoryError e) {
            System.err.println(e.getMessage());
        }
        gcMonitoring.stopMonitoring();
        gcMonitoring.saveSatToFile(String.format("target/%s", (args.length > 0) ? args[0] : "statfile.txt"));
    }
}
