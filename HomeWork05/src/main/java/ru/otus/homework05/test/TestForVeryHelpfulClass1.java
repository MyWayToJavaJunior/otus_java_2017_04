package ru.otus.homework05.test;


import ru.otus.homework05.VeryHelpfulClass1;
import ru.otus.homework05.test.framework.annotations.OtusAfter;
import ru.otus.homework05.test.framework.OtusAssert;
import ru.otus.homework05.test.framework.annotations.OtusBefore;
import ru.otus.homework05.test.framework.annotations.OtusTest;

public class TestForVeryHelpfulClass1 {

    @OtusBefore(order = 1)
    public void beforeTest() {
        System.out.println("I am method with @OtusBeFore(order = 1) annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusBefore(order = 3)
    public void beforeTest3() {
        System.out.println("I am method with @OtusBeFore(order = 3) annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusBefore(order = 2)
    public void beforeTest2() {
        System.out.println("I am method with @OtusBeFore(order = 2) annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusTest
    public void concatTwoCharsTest() {
        VeryHelpfulClass1 vhpc1 = new VeryHelpfulClass1();
        OtusAssert.assertTrue(vhpc1.concatTwoChars('a', 'b').equals("ab"));
    }

    @OtusAfter(order = 3)
    public void afterTest3() {
        System.out.println("I am method with @OtusAfter(order = 3) annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusAfter(order = 1)
    public void afterTest1() {
        System.out.println("I am method with @OtusAfter(order = 1) annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusAfter(order = 2)
    public void afterTest2() {
        System.out.println("I am method with @OtusAfter(order = 2) annotation in the TestForVeryHelpfulClass1 class");
    }
}
