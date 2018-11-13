package com.suikajy.java8note.ch6_collect;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.*;

public class PrimeCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> {
            Map<Boolean, List<Integer>> map = new HashMap<>();
            map.put(true, new ArrayList<>());
            map.put(false, new ArrayList<>());
            return map;
        };
    }

    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (map, i) -> map
                .get(isPrime(map.get(true), i))
                .add(i);
    }

    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (map1, map2) -> {
            map1.get(true).addAll(map2.get(true));
            map2.get(false).addAll(map2.get(false));
            return map1;
        };
    }

    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
    }

    private boolean isPrime(List<Integer> acc, Integer candidate) {
        int candidateRoot = (int) Math.sqrt(candidate);
        return takeWhile(acc, i -> i < candidateRoot)
                .stream()
                .noneMatch(i -> candidate % i == 0);

//        return acc.stream()
//                .filter(i -> i < candidateRoot)
//                .noneMatch(i -> candidate % i == 0);
    }

    // 使用takeWhile来加速filter。因为filter会对流中的每一个数字判断，但其实到了候选数字的根即可停止了。
    private static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }
}
