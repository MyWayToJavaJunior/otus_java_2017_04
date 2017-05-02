package ru.otus.homework05.test.framework;

import ru.otus.homework05.test.framework.annotations.OtusAfter;
import ru.otus.homework05.test.framework.annotations.OtusTest;
import ru.otus.homework05.test.framework.annotations.OtusBefore;

import java.util.ArrayList;
import java.util.List;

public class TestsExecutor {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("No classes for testing");
            return;
        }

        TestClassesSource source = new TestClassesSource(OtusBefore.class, OtusTest.class, OtusAfter.class);

        if (args[0].equalsIgnoreCase("package")) {
            source.fill(args[1]);
        } else if (args[0].equalsIgnoreCase("classes")) {
            List<Class<?>> testClasses = new ArrayList<>();
            for (int i = 1; i < args.length; i++)
            {
                try {
                    Class<?> c = Class.forName(args[i]);
                    testClasses.add(c);
                } catch (ClassNotFoundException e) {
                    System.out.println(String.format("Class \"%s\" not found", args[i]));
                }
            }

            source.fill(testClasses.toArray(new Class<?>[0]));
        }

        source.executeAll();
    }
}
