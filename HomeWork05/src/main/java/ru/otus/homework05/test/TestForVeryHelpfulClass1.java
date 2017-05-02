package ru.otus.homework05.test;


import ru.otus.homework05.VeryHelpfulClass1;
import ru.otus.homework05.test.framework.annotations.OtusAfter;
import ru.otus.homework05.test.framework.OtusAssert;
import ru.otus.homework05.test.framework.annotations.OtusBefore;
import ru.otus.homework05.test.framework.annotations.OtusTest;

public class TestForVeryHelpfulClass1 {

    @OtusBefore
    public void beforeTest() {
        System.out.println("I am method with @OtusBeFore annotation in the TestForVeryHelpfulClass1 class");
    }

    @OtusTest
    public void concatTwoCharsTest() {
        VeryHelpfulClass1 vhpc1 = new VeryHelpfulClass1();
        OtusAssert.assertTrue(vhpc1.concatTwoChars('a', 'b').equals("ab"));
    }

    @OtusAfter
    public void afterTest() {
        System.out.println("I am method with @OtusAfter annotation in the TestForVeryHelpfulClass1 class");
    }
}
