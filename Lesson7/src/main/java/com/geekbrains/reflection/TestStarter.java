package com.geekbrains.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestStarter {
    public static void start(Class c) {
        Object obj = null;
        try {
            obj = c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Method[] methods = c.getMethods();
        Method beforeMethod = null;
        Method afterMethod = null;
        Map<Integer, List<Method>> testMethodsMap = new TreeMap<>();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethod != null) {
                    throw new RuntimeException();
                }
                beforeMethod = methods[i];
            } else if (methods[i].isAnnotationPresent(AfterSuite.class)) {
                if (afterMethod != null) {
                    throw new RuntimeException();
                }
                afterMethod = methods[i];
            } else if (methods[i].isAnnotationPresent(Test.class)){
                Method testMethod = methods[i];
                int priority = methods[i].getAnnotation(Test.class).priority();
                if (priority < 1 || priority > 10) {
                    throw new RuntimeException("Invalid priority value");
                }
                if (!testMethodsMap.containsKey(priority)) {
                    List<Method> testMethodsList = new ArrayList<>();
                    testMethodsList.add(testMethod);
                    testMethodsMap.put(priority, testMethodsList);
                } else {
                    List<Method> testMethodList = testMethodsMap.get(priority);
                    testMethodList.add(testMethod);
                    testMethodsMap.replace(priority, testMethodsMap.get(priority), testMethodList);
                }
            }
        }
        try {
            beforeMethod.invoke(obj);
            for (Map.Entry<Integer,List<Method>> entry : testMethodsMap.entrySet()) {
                for (Method m : entry.getValue()) {
                        m.invoke(obj);
                }
            }
            afterMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        start(TestClass.class);
    }
}
