package com.suikajy.java8note.ch3_lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

@SuppressWarnings("WeakerAccess")
public class Java8Function {

    private static String s;

    public static void main(String[] args) {
        Predicate<String> emptyPre = s1 -> s1.isEmpty();

        Consumer<String> strCon = s -> Java8Function.s = s;
        List<String> sList = new ArrayList<>();
        Collections.addAll(sList, "one", "two", "three");
        foreach(sList, s -> System.out.println(s));

        Function<String, Integer> mapFun = s -> s.length();
        List<Integer> integerList = map(sList, mapFun);
        foreach(integerList, integer -> System.out.println(integer));

        IntPredicate intPredicate = i -> i > 0;

    }

    public static <T> void foreach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        List<R> resultList = new ArrayList<>();
        foreach(list, t -> resultList.add(function.apply(t)));
        return resultList;
    }
}
