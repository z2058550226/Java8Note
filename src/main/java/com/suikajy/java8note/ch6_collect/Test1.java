package com.suikajy.java8note.ch6_collect;

import java.util.stream.IntStream;

public class Test1 {

public static void main(String[] args) {
    //IntStream.rangeClosed(2, 100).filter(i -> {
    //    int iRoot = (int) Math.sqrt(i);
    //    return IntStream.rangeClosed(2, iRoot)
    //            .noneMatch(divider -> i % divider == 0);
    //}).forEach(System.out::println);

    IntStream.rangeClosed(2, 100).boxed()
            .collect(new PrimeCollector())
            .get(true)
            .forEach(System.out::println);
}

}
