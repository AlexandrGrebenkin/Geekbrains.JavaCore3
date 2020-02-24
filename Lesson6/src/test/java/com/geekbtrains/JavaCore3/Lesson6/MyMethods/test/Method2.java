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
public class Method2 {
    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,1,1,1,1,1},        false   },
                {new int[]{4,4,4,4,4,4,4},      false   },
                {new int[]{1,1,1,1,1,1,1,4},    true    },
                {new int[]{1,4,4,4,4,4,4},      true    },
                {new int[]{1,2,3,3,4,1,1,1,4},  false   }
        });
    }

    private MyMethods methods;
    private int[] ints;
    private boolean result;

    public Method2(int[] ints, boolean result) {
        this.ints = ints;
        this.result = result;
    }

    @Before
    public void init() {
        methods = new MyMethods();
    }

    @Test
    public void test() {
        Assert.assertEquals(result, methods.method2(ints));
    }
}
