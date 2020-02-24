package com.geekbtrains.JavaCore3.Lesson6.MyMethods.test;

import com.geekbrains.JavaCore3.Lesson6.MyMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Method1 {
    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,2,3,4,5,6,7,8},    new int[] {5,6,7,8}},
                {new int[]{1,2,3,4,5,4,7,8},    new int[] {7,8}    },
                {new int[]{4,3,2,1},            new int[]{3,2,1}   },
                {new int[]{1,2,3,4,4},          new int[0]         },
                {new int[]{4,4,4,4,4,4},        new int[0]         }
        });
    }

    private MyMethods methods;
    private int[] ints;
    private int[] result;

    public Method1( int[] ints, int[] result) {
        this.ints = ints;
        this.result = result;
    }

    @Before
    public void init() {
        methods = new MyMethods();
    }

    @Test
    public void test() {
        Assert.assertArrayEquals(result, methods.method1(ints));
    }
}
