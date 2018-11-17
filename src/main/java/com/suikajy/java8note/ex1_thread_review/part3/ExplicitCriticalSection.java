package com.suikajy.java8note.ex1_thread_review.part3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 显示同步锁和同步方法在多线程条件下的性能差异
 */


class ExplicitPairManager1 extends PairManager {
    private Lock lock = new ReentrantLock();

    // Synchronized the entire method
    @Override
    public synchronized void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(getPair());
        } finally {
            lock.unlock();
        }
    }

}

class ExplicitPairManager2 extends PairManager {
    private Lock lock = new ReentrantLock();

    // Use a critical section:
    @Override
    public void increment() {
        Pair temp;
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        } finally {
            lock.unlock();
        }
        store(temp);
    }
}

public class ExplicitCriticalSection {

    public static void main(String[] args) {
        PairManager
                pman1 = new ExplicitPairManager1(),
                pman2 = new ExplicitPairManager2();
        CriticalSection.testApproaches(pman1, pman2);
    }
}
