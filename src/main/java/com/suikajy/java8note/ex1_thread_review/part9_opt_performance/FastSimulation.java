package com.suikajy.java8note.ex1_thread_review.part9_opt_performance;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 乐观加锁 - 使用Atomic对象的decrementAndGet()这类方法都是原子性的，他们内部都是有锁存在的。
 * 但某些Atomic类还允许乐观加锁。比如在你执行某项计算并准备更新某个值的时候，实际上并没有使用互斥
 * 你需要使用compareAndSet()方法将旧值和新值一起提交给这个方法，如果旧值与他在Atomic对象中发现的值
 * 不一致，那么这个操作就失败。
 */
public class FastSimulation {

    static final int N_ELEMENTS = 100000;
    static final int N_GENES = 30;
    static final int N_EVOLVERS = 50;
    static final AtomicInteger[][] GRID = new AtomicInteger[N_ELEMENTS][N_GENES];
    static Random rand = new Random(47);

    static class Evolver implements Runnable {

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < N_ELEMENTS; i++) {
            for (int j = 0; j < N_GENES; j++) {
                GRID[i][j] = new AtomicInteger(rand.nextInt(100));
            }
        }
        for (int i = 0; i < N_EVOLVERS; i++) {
            exec.execute(new Evolver());
        }
        TimeUnit.MILLISECONDS.sleep(5000);
        exec.shutdownNow();
    }

}
