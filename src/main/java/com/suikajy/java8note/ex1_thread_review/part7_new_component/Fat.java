package com.suikajy.java8note.ex1_thread_review.part7_new_component;

/**
 * 这是一个创建起来耗时的对象
 *
 * volatile修饰的变量d是一个频繁赋值的变量，所以这里用到了volatile的另一个特性：防止运算优化，减慢运行效率
 */
public class Fat {
    private volatile double d; // Prevent optimization
    private static int counter = 0;
    private final int id = counter++;

    public Fat() {
        // Expensive, interruptable operation
        for (int i = 0; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double) i;
        }
    }

    public void operation() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Fat id: " + id;
    }
}
