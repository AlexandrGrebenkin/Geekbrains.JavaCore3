package com.geekbrains.javacore3.lesson1;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> fruits = new ArrayList<>();
    private float weight = 0;

    public float getWeight() {
        return weight;
    }

    public void addFruits(T... fruit){
        for (T f : fruit){
            fruits.add(f);
            weight += f.getWeight();
        }
    }

    public void addAll(Box<T> anotherBox){
        for (T f : anotherBox.getFruits()){
            fruits.add(f);
            weight += f.getWeight();
        }
    }

    public void removeAll(){
        fruits.clear();
        weight = 0;
        /*
        пробовал реализовать таким образом, но почему-то удаляется только 1 объект.
        for (T f : fruits()){
            fruits.remove(f);
            weight -= f.getWeight();
        }
         */
    }

    public ArrayList<T> getFruits() {
        return fruits;
    }

    public boolean compare(Box<? extends Fruit> anotherBox){
        return Math.abs(this.getWeight() - anotherBox.getWeight()) < 0.00001;
    }

    public static <T extends Fruit> void addAll(Box<T> source, Box<T> destination){
        destination.addAll(source);
        source.removeAll();
    }
}
