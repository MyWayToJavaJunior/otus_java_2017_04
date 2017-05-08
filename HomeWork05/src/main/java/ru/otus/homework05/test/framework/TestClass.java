package ru.otus.homework05.test.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
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

    private void sortMethodsListByAnotationOrderParam(List<Method> list) {
        list.sort((o1, o2) -> {
            int res = 0;

            Class<? extends Annotation> ann = null;

            if (o1.isAnnotationPresent(beforeAnotation) && o2.isAnnotationPresent(beforeAnotation)) {
                ann = beforeAnotation;
            }
            else if (o2.isAnnotationPresent(afterAnotation) && o2.isAnnotationPresent(afterAnotation)) {
                ann = afterAnotation;
            }
            if (ann != null) {
                Annotation obj1 = o1.getAnnotation(ann);
                Annotation obj2 = o2.getAnnotation(ann);
                try {
                    int order1 = (int)obj1.annotationType().getMethod("order").invoke(obj1);
                    int order2 = (int)obj1.annotationType().getMethod("order").invoke(obj2);
                    res = Integer.compare(order1, order2);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return res;
        });
    }

    public void exeute() {
        sortMethodsListByAnotationOrderParam(beforeMethods);
        sortMethodsListByAnotationOrderParam(afterMethods);
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
