package com.geekbtrains.JavaCore3.Lesson6.MyMethods.test;

import com.geekbrains.JavaCore3.Lesson6.MyMethods;
import org.junit.Test;

public class Method1Errors {
    @Test(expected = RuntimeException.class)
    public void test(){
        MyMethods methods = new MyMethods();
        methods.method1(new int[]{1,2,3,5,6,7,8,9});
    }
}
