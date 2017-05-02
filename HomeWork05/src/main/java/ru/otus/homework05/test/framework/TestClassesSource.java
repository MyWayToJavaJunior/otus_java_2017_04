package ru.otus.homework05.test.framework;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestClassesSource {
    private Class<? extends Annotation> beforeAnotation;
    private Class<? extends Annotation> testAnotation;
    private Class<? extends Annotation> afterAnotation;

    private Map<String, TestClass> testClasses = new HashMap<>();

    public TestClassesSource(Class<? extends Annotation> beforeAnotation,
                             Class<? extends Annotation> testAnotation,
                             Class<? extends Annotation> afterAnotation) {
        this.beforeAnotation = beforeAnotation;
        this.testAnotation = testAnotation;
        this.afterAnotation = afterAnotation;
    }

    private static ConfigurationBuilder prepareReflectionsConfiguration(String packageName) {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
        configuration.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packageName)));
        configuration.setUrls(ClasspathHelper.forClassLoader());

        MethodAnnotationsScanner annotationsScanner = new MethodAnnotationsScanner();
        configuration.setScanners(annotationsScanner);

        return configuration;
    }

    private void fill(Set<Method> methods) {
        for (Method m : methods) {
            String methodClassName = m.getDeclaringClass().getName();
            if (!testClasses.containsKey(methodClassName)) {
                testClasses.put(methodClassName, new TestClass(m.getDeclaringClass(), beforeAnotation, testAnotation, afterAnotation));
            }

            TestClass testClass = testClasses.get(methodClassName);
            testClass.addMethod(m);
        }
    }

    void fill(String packageName) {

        Reflections ref = new Reflections(prepareReflectionsConfiguration(packageName));

        Set<Method> methods = new HashSet<>(ref.getMethodsAnnotatedWith(beforeAnotation));
        methods.addAll(ref.getMethodsAnnotatedWith(testAnotation));
        methods.addAll(ref.getMethodsAnnotatedWith(afterAnotation));
        fill(methods);

    }

    void fill(Class<?>...classesOfTest) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> c : classesOfTest) {
            for (Method m : c.getMethods()) {
                if (m.isAnnotationPresent(beforeAnotation) || m.isAnnotationPresent(testAnotation) || m.isAnnotationPresent(afterAnotation)) {
                    methods.add(m);
                }
            }
        }
        fill(methods);
    }

    public void executeAll() {
        testClasses.forEach((s, testClass) -> testClass.exeute());
    }
}
