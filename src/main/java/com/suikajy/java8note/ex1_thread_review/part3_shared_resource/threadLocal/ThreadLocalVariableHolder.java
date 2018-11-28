package com.suikajy.java8note.ex1_thread_review.part3_shared_resource.threadLocal;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程本地存储：threadLocal
 */

class Accessor implements Runnable {
    private final int id;

    Accessor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ThreadLocalVariableHolder.increment();
            System.out.println(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id + "：" + ThreadLocalVariableHolder.get();
    }
}

public class ThreadLocalVariableHolder {

    /**
     * ThreadLocal通常会根据对应线程产生对应的副本(每个线程在使用这个对象时都会先调用initialValue)，
     * 它的对象可以在相应线程中调用get和set方法。
     * <p>
     * ThreadLocal通常被声明成静态的。
     */
    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private Random rand = new Random(47);

        @Override
        protected Integer initialValue() {
            return rand.nextInt();
        }
    };

    public static void increment() {
        value.set(value.get() + 1);
    }

    public static int get() {
        return value.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(3); // Run for a while
        exec.shutdownNow();                // All Accessors will quit
    }

}
