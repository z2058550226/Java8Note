package com.suikajy.java8note.ex1_thread_review.part3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 通过让多个线程共享IntGenerator，在取消IntGenerator的同时，
 * 所有线程都会完成结算，进而实现线程的取消。
 */
public class EvenChecker implements Runnable {

    private IntGenerator generator;
    private final int id;

    public EvenChecker(IntGenerator generator, int id) {
        this.generator = generator;
        this.id = id;
    }

    @Override
    public void run() {
//        System.out.println(Thread.currentThread() + " started! id：" + id);
        while (!generator.isCanceled()) {
            int val = generator.next();
            if (val % 2 != 0) {
                System.out.println("id: " + id + ", " + val + " not even!");
                generator.cancel(); // Cancels all EvenCheckers
            }
        }
    }

    // Test any type of IntGenerator
    public static void test(IntGenerator gp, int count) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            exec.execute(new EvenChecker(gp, i));
        }
        exec.shutdown();
    }

    // Default value for count
    public static void test(IntGenerator gp) {
        test(gp, 10);
    }

    public static void main(String[] args) {
        test(new IntGenerator() {
            int num = 0;

            @Override
            public int next() {
                return num++;
            }
        });
    }
}

/**
 * 偶数生成器
 */
class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    /**
     * Brian的同步规则：
     * 如果你正在写一个变量，它可能接下来将被另一个线程读取，或者正在读取上一次已经被
     * 另一个线程写过的变量，那么你必须使用同步，并且，读写线程都必须用相同的监视器锁同步
     */
    @Override
    public synchronized int next() {
        ++currentEvenValue; // Danger point here!
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}
