package com.geekbrains.reflection;

public class TestClass {
    @BeforeSuite
    public void before() {
        System.out.println("before");
    }

    @AfterSuite
    public void after() {
        System.out.println("after");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("test1");
    }

    @Test(priority = 10)
    public void test2() {
        System.out.println("test2");
    }

    @Test()
    public void test3() {
        System.out.println("test3");
    }

    @Test(priority = 10)
    public void test4() {
        System.out.println("test4");
    }

    @Test(priority = 10)
    public void test5() {
        System.out.println("test5");
    }

}
