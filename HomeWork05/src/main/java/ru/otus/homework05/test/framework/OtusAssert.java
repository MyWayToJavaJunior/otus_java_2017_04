package ru.otus.homework05.test.framework;

public class OtusAssert {
    private OtusAssert() {

    }

    public static void fail(String failText) {
        if(failText == null) {
            throw new AssertionError();
        } else {
            throw new AssertionError(failText);
        }
    }

    public static void assertTrue(boolean conditionForCheck) {
        assertTrue(conditionForCheck, null);
    }

    public static void assertTrue(boolean conditionForCheck, String failText) {
        if (!conditionForCheck) {
            fail(failText);
        }
    }

    public static void assertNotNull(Object objectForCheck) {
        assertTrue(objectForCheck != null);
    }

    public static void assertNotNull(Object objectForCheck, String failText) {
        assertTrue(objectForCheck != null, failText);
    }
}
