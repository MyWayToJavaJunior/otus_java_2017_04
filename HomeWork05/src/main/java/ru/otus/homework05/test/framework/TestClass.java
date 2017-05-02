package ru.otus.homework05.test.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    private Class<?> clazz;

    private Class<? extends Annotation> beforeAnotation;
    private Class<? extends Annotation> testAnotation;
    private Class<? extends Annotation> afterAnotation;

    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();
    private List<Method> afterMethods = new ArrayList<>();

    public TestClass(Class<?> clazz, Class<? extends Annotation> beforeAnotation,
                     Class<? extends Annotation> testAnotation,
                     Class<? extends Annotation> afterAnotation) {

        this.clazz = clazz;
        this.beforeAnotation = beforeAnotation;
        this.testAnotation = testAnotation;
        this.afterAnotation = afterAnotation;
    }

    public void addMethod(Method method) {
        if (method.isAnnotationPresent(beforeAnotation)) {
            beforeMethods.add(method);
        }
        else if (method.isAnnotationPresent(testAnotation)) {
            testMethods.add(method);
        }
        else if (method.isAnnotationPresent(afterAnotation)) {
            afterMethods.add(method);
        }
    }

    public void exeute() {
        for (Method testMethod : testMethods) {
            try {
                Object object = clazz.newInstance();

                for (Method beforeMethod : beforeMethods)
                    beforeMethod.invoke(object);

                try {

                    System.out.print(String.format("Executing test \"%s\"...", testMethod.getName()));
                    testMethod.invoke(object);
                    System.out.println("passed");

                } catch (InvocationTargetException e) {
                    String failMessage = e.getTargetException().getMessage();
                    System.out.println((failMessage == null) ? "fail" : String.format("fail with message \"%s\"", failMessage));
                }

                for (Method afterMethod : afterMethods)
                    afterMethod.invoke(object);

            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            System.out.println();
        }

    }
}
