package com.geekbrains.javacore3.lesson1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        test();
    }

    //Task 1
    public static <T> void swap(T[] array, int pos1, int pos2){
        T temp = array[pos1];
        array[pos1] = array[pos2];
        array[pos2] = temp;
    }

    //Task 2
    public static <T> ArrayList<T> toArrayList(T[] array){
        ArrayList<T> arrayList = new ArrayList<>();
        //Collections.addAll(arrayList, array);
        for (T t : array){
            arrayList.add(t);
        }
        return arrayList;
    }

    public static void test(){
        System.out.println("Task 1:");
        Integer[] integers = new Integer[]{4, 6, 8, 2, 10};
        for (int i : integers){
            System.out.print(i + " ");
        }
        System.out.println();
        swap(integers, 2,3);
        for (int i : integers){
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("Task 2:");
        List<Integer> integerList = toArrayList(integers);
        for (int i : integerList){
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("Task 3:");

        Box<Apple> appleBox1 = new Box<>();
        Box<Apple> appleBox2 = new Box<>();
        Box<Apple> appleBox3 = new Box<>();

        Box<Orange> orangeBox1 = new Box<>();

        appleBox1.addFruits(new Apple(), new Apple(), new Apple(), new Apple());
        appleBox2.addFruits(new Apple(), new Apple());
        appleBox3.addFruits(new Apple(), new Apple());

        orangeBox1.addFruits(new Orange(), new Orange());

        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());
        System.out.println(appleBox3.getWeight());
        System.out.println(orangeBox1.getWeight());

        System.out.println(appleBox1.compare(appleBox2));
        System.out.println(appleBox2.compare(appleBox3));
        System.out.println(appleBox2.compare(orangeBox1));

        Box.addAll(appleBox3, appleBox2);

        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());
        System.out.println(appleBox3.getWeight());

        System.out.println(appleBox1.compare(appleBox2));
        System.out.println(appleBox2.compare(appleBox3));

    }



}
