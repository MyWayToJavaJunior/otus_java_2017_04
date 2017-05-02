package ru.otus.homework05.test;

import ru.otus.homework05.VeryHelpfulClass2;
import ru.otus.homework05.test.framework.OtusAssert;
import ru.otus.homework05.test.framework.annotations.OtusTest;

public class TestForVeryHelpfulClass2 {
    @OtusTest
    public void replaceNullTest() {
        VeryHelpfulClass2 vhpc2 = new VeryHelpfulClass2();
        OtusAssert.assertNotNull(vhpc2.replaceNull(null));
    }
}
