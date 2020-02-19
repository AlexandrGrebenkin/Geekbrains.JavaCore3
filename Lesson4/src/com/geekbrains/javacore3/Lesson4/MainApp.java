package com.geekbrains.javacore3.Lesson4;

public class MainApp {
    public static void main(String[] args) {
        ABC abc = new ABC();
        new Thread(() -> abc.printA()).start();
        new Thread(() -> abc.printB()).start();
        new Thread(() -> abc.printC()).start();
    }
}
