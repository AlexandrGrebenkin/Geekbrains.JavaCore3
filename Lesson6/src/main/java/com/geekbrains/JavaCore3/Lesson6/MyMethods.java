package com.geekbrains.JavaCore3.Lesson6;

import java.util.Arrays;

public class MyMethods {
    public int[] method1(int[] ints) {
        int index = -1;
        for (int i = ints.length-1; i >= 0; i--) {
            if (ints[i] == 4) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new RuntimeException();
        } else if (index + 1 == ints.length) {
            return new int[0];
        }
        int[] result = Arrays.copyOfRange(ints, index + 1, ints.length);
        return result;
    }

    public boolean method2(int[] ints) {
        boolean isFour = false;
        boolean isOne = false;
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == 1) {
                isOne = true;
            } else if (ints[i] == 4) {
                isFour = true;
            } else {
                return false;
            }
        }
        return isFour && isOne;
    }

    public static void main(String[] args) {
        MyMethods methods = new MyMethods();

        int[] ints = {1,2,3,5,6,7};
        System.out.println(Arrays.toString(methods.method1(ints)));
    }


}
