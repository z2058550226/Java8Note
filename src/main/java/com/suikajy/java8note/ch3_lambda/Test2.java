package com.suikajy.java8note.ch3_lambda;

import com.suikajy.java8note.ch1.Apple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Test2 {

    public static void main(String[] args) {
        // 比较器符合
        Comparator<Apple> comparator = Comparator.comparing(Apple::getWeight);
        // 逆序
        List<Apple> inventory = new ArrayList<>();
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
        // 比较器链
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getCountry));

        // 谓词符合
        Predicate<Apple> redApple = apple -> "red".equals(apple.getColor());
        // negate取非
        Predicate<Apple> notRedApple = redApple.negate();
        // and与运算
        Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150);
        // or或运算
        Predicate<Apple> redAndHeavyAppleOrGreen = redApple
                .and(apple -> apple.getWeight() > 150)
                .or(apple -> "green".equals(apple.getColor()));

        // 函数复合
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        // andThen 这在数学上写作g(f(x))
        Function<Integer, Integer> h = f.andThen(g);
        int result = h.apply(1); // 4
        // compose 这和 andThen 相反，这里写作f(g(x))
        Function<Integer, Integer> i = f.compose(g);
        result = i.apply(1); // 3
        // 这看起来比较抽象，实际使用的时候其实是一种类似流水线（pipeline）的结构
        Function<String, String> addHeader = Letter::addHeader;
        // 同过andThen组成了一个 addHeader | checkSpelling | addFooter 的流水线
        Function<String, String> transformationPipeline =
                addHeader.andThen(Letter::checkSpelling)
                        .andThen(Letter::addFooter);

    }

    private static class Letter {
        public static String addHeader(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }

        public static String addFooter(String text) {
            return text + " Kind regards";
        }

        public static String checkSpelling(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }

}
