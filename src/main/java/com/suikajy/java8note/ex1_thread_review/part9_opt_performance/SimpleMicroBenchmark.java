package com.suikajy.java8note.ex1_thread_review.part9_opt_performance;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一种天真的比较并发性能的方式
 */
abstract class Incrementable {
    protected long counter = 0;

    public abstract void increment();
}

class SynchronizingTest extends Incrementable {

    @Override
    public synchronized void increment() {
        ++counter;
    }
}

class LockingTest extends Incrementable {

    private Lock lock = new ReentrantLock();

    @Override
    public void increment() {
        lock.lock();
        try {
            ++counter;
        } finally {
            lock.unlock();
        }
    }
}

public class SimpleMicroBenchmark {

    static long test(Incrementable incrementable) {
        long start = System.nanoTime();
        for (int i = 0; i < 10000000L; i++) {
            incrementable.increment();
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        long synchTime = test(new SynchronizingTest());
        long lockTime = test(new LockingTest());
        System.out.printf("synchronized: %1$10d\n",synchTime);
        System.out.printf("Lock:         %1$10d\n",lockTime);
    }
}
