package com.suikajy.java8note.ch3_lambda;

import com.suikajy.java8note.ch1.Apple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Test2 {

    public static void main(String[] args) {
        Comparator<Apple> comparator = Comparator.comparing(Apple::getWeight);
        // 逆序
        List<Apple> inventory = new ArrayList<>();
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
        // 比较器链
        
    }

}
