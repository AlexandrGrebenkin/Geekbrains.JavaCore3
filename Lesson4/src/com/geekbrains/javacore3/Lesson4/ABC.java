package com.geekbrains.javacore3.Lesson4;

public class ABC {
    private int repeatCount = 5;
    private char currentLetter = 'A';

    public void printA(){
        synchronized (this) {
            try {
                for (int i = 0; i < repeatCount; i++) {
                    while (currentLetter != 'A'){
                        this.wait();
                    }
                    System.out.print('A');
                    currentLetter = 'B';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB(){
        synchronized (this) {
            try {
                for (int i = 0; i < repeatCount; i++) {
                    while (currentLetter != 'B'){
                        this.wait();
                    }
                    System.out.print('B');
                    currentLetter = 'C';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC(){
        synchronized (this) {
            try {
                for (int i = 0; i < repeatCount; i++) {
                    while (currentLetter != 'C'){
                        this.wait();
                    }
                    System.out.print('C');
                    System.out.print(' ');
                    currentLetter = 'A';
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
