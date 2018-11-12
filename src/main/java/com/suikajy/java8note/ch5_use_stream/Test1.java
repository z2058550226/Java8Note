package com.suikajy.java8note.ch5_use_stream;

import com.suikajy.java8note.ch4_stream.Dish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.suikajy.java8note.ch4_stream.Dish.menu;
import static java.util.stream.Collectors.toList;


public class Test1 {

    public static void main(String[] args) {
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());

        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct() // 去除重复
                .forEach(System.out::println);
        List<Dish> dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .limit(3)
                .collect(toList());
        List<Dish> dishesWithOutTop2 = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .collect(toList());

        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(toList());

        List<String> uniqueCharacters =
                words.stream()
                        .map(w -> w.split(""))
                        .flatMap(Arrays::stream)
                        .distinct()
                        .collect(Collectors.toList());

        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }

        boolean isHealthy = menu.stream()
                .allMatch(d -> d.getCalories() < 1000);

        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(dish -> System.out.println(dish.getName()));

        List<CharSequence> list = listOf(10, i -> "i: " + i);
        List<Animal> list1 = listOf(10, i -> new Dog("dog: " + i));

//int calories = menu.stream()
//        .map(Dish::getCalories)
//        .reduce(0, Integer::sum);
        int calories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();

        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count()); // 50

        // 求100以内的勾股数
        IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(1, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                .filter(t -> t[2] % 1 == 0)
                ).forEach(it -> System.out.println(it[0]));
    }

    public static <T> String joinToString(T[] arr, Function<? super T, String> transform) {
        return joinToString(arr, "", "", ", ", transform);
    }

    public static <T> String joinToString(T[] arr, String prefix, String postfix, String separator, Function<? super T, String> transform) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        boolean isEmpty = true;
        for (T t : arr) {
            if (isEmpty) {
                sb.append(transform.apply(t));
                isEmpty = false;
            } else {
                sb.append(separator);
                sb.append(transform.apply(t));
            }
        }
        sb.append(postfix);
        return sb.toString();
    }

    public static <T> List<T> listOf(int length, IntFunction<T> function) {
        List<T> list = new ArrayList<>();
        for (int i = 1; i < length + 1; i++) {
            list.add(function.apply(i));
        }
        return list;
    }

    public static class Animal {
        final String name;

        public Animal(String name) {
            this.name = name;
        }
    }

    public static class Dog extends Animal {
        public Dog(String name) {
            super(name);
        }
    }

}
