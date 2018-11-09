package com.suikajy.java8note.ch1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Test1 {

    public static void main(String[] args) {
        List<Apple> apples = filterApples(new ArrayList<>(),
                (Apple apple) -> "green".equals(apple.getColor()));
//        Object lambda = () -> System.out.println("lambda");
        List<String> s = new ArrayList<>();
        s.sort((String s1, String s2) -> s1.compareTo(s2));
    }

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    private static List<Apple> filterApples(List<Apple> inventory, Function<Apple, Boolean> filter) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (filter.apply(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

}
