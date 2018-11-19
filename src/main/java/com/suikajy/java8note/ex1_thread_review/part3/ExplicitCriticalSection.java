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
    public void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(getPair());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair getPair() {
        lock.lock();
        try {
            return super.getPair();
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

    // 如果使用可重入锁，那么这个类中原本使用synchronized的方法也要改成对应锁来
    // 控制访问，因为父类中的synchronized默认指定this作为锁对象，所以这会导致锁对象不一致
    // 因此在此显式使用可重入锁来重写这个方法。
    @Override
    public Pair getPair() {
        lock.lock();
        try {
            return super.getPair();
        } finally {
            lock.unlock();
        }

    }
}

public class ExplicitCriticalSection {

//    public static Lock sLock = new ReentrantLock();

    public static void main(String[] args) {
        PairManager
                pman1 = new ExplicitPairManager1(),
                pman2 = new ExplicitPairManager2();
        CriticalSection.testApproaches(pman1, pman2);
    }
}
