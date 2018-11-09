package com.suikajy.java8note.ch3_lambda;

public class Test1 {

    @FunctionalInterface
    interface Adder {

        int add(int a, int b);

        default boolean isResultIllegal(long result) {
            return result < Integer.MAX_VALUE;
        }

        Adder ZERO_ADDER = (a, b) -> 0;

        enum SealedAdder implements Adder {
            MY_ADDER;

            @Override
            public int add(int a, int b) {
                long result = (long) a + (long) b;
                if (isResultIllegal(result)) {
                    return (int) result;
                }
                throw new IllegalArgumentException("the number is too big!");
            }
        }
    }

    // 抽象类不可以指定为函数式接口
    abstract class Minus {
        abstract int minus(int a, int b);
    }

    private static int customAdd(int a, int b, Adder adder) {
        return adder.add(a, b);
    }

    static class MyAdder implements Adder {

        @Override
        public int add(int a, int b) {
            long result = (long) a + (long) b;
            if (isResultIllegal(result)) {
                return (int) result;
            }
            throw new IllegalArgumentException("the number is too big!");
        }
    }

    public static void main(String[] args) {
        // 这种写法是错误的isResultIllegal这个方法并不能直接在lambda中使用
        /*customAdd(1, 2, (a, b) -> {
            long result = (long) a + (long) b;
            if (isResultIllegal(result)) {
                return (int) result;
            }
            throw new IllegalArgumentException("the number is too big!");
        });*/
        customAdd(1, 2, new MyAdder());
        customAdd(1, 2, Adder.SealedAdder.MY_ADDER);
    }

}
