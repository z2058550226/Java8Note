package com.suikajy.java8note.ch5_use_stream;

import com.suikajy.java8note.ch4_stream.Dish;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.suikajy.java8note.ch4_stream.Dish.menu;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;


@SuppressWarnings("Duplicates")
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
                ).forEach(it ->
                System.out.println(
                        doubleJoinToString(it, "", "", ", ", d -> (int) d + "")));
        long uniqueWords = 0;
        try (Stream<String> lines =
                     Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException e) {
        }
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);

        Comparator<Dish> dishCaloriesComparator =
                comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish =
                menu.stream()
                        .collect(maxBy(dishCaloriesComparator));
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })
                        )
                );

//Map<Dish.Type, Optional<Dish>> mostCaloricByType =
//        menu.stream()
//                .collect(groupingBy(Dish::getType,
//                        maxBy(comparingInt(Dish::getCalories))));
Map<Dish.Type, Dish> mostCaloricByType =
        menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(comparingInt(Dish::getCalories)),
                                Optional::get)));
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(
                                dish -> { if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                else return CaloricLevel.FAT; },
                                toCollection(HashSet::new) )));
        System.out.println(caloricLevelsByType);
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

    public static String doubleJoinToString(double[] arr, String prefix, String postfix, String separator, DoubleFunction<String> transform) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        boolean isEmpty = true;
        for (double t : arr) {
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

    public enum CaloricLevel {DIET, NORMAL, FAT}

}