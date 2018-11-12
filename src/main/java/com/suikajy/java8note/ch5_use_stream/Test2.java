package com.suikajy.java8note.ch5_use_stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Test2 {

    private static Trader raoul = new Trader("Raoul", "Cambridge");
    private static Trader mario = new Trader("Mario", "Milan");
    private static Trader alan = new Trader("Alan", "Cambridge");
    private static Trader brian = new Trader("Brian", "Cambridge");

    private static List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );


    public static void main(String[] args) {
        //test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
    }

    private static void test1() {
        List<Transaction> result = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(result);
    }

    private static void test2() {
        /*List<String> result = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());*/
        // distinct和collect结合可以用toSet简化。
        Set<String> result = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .collect(Collectors.toSet());
        System.out.println(result);
    }

    private static void test3() {
        List<Trader> result = transactions.stream()
                .map(Transaction::getTrader)
                .filter(it -> "Cambridge".equals(it.getCity()))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());
        System.out.println(result);
    }

    private static void test4() {
        List<String> result = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(result);
    }

    private static void test5() {
        boolean b = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .anyMatch("Milan"::equals);
        System.out.println("Is Milan trader exist? : " + b);
    }

    private static void test6() {
        Integer result = transactions.stream()
                .filter(it -> "Cambridge".equals(it.getTrader().getCity()))
                .map(Transaction::getValue)
                .reduce(0, (i1, i2) -> i1 + i2);
        System.out.println("Cambridge total value is: " + result);
    }

    private static void test7() {
        transactions.stream()
                .max(Comparator.comparingInt(Transaction::getValue))
                .ifPresent(it -> System.out.println(it.getValue()));
    }

    private static void test8() {
        transactions.stream()
                .min(Comparator.comparingInt(Transaction::getValue))
                .ifPresent(System.out::println);
    }
}
