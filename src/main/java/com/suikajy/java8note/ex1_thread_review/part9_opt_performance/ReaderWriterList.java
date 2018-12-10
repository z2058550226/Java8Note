package com.suikajy.java8note.ex1_thread_review.part9_opt_performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * ReadWriteLock - 这种锁在多个任务经常要读取一个数据结构的时候进行了优化
 * 读写锁可以使你同时有多个读取者，只要他们都不是图写入即可。如果写锁已经被其他任务持有，
 * 那么任何读取者都不能访问，直至这个写锁被释放为止。
 * <p>
 * ReentrantReadWriteLock还有其他大量的方法可以使用，涉及“公平性”和“政策性决策”等问题。
 * 这是一个相当复杂的工具，只有在你的程序需要更加高效的同步的时候才考虑引入这个锁。
 */
public class ReaderWriterList<T> {

    private ArrayList<T> lockedList;
    // Make the ordering fair:
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public ReaderWriterList(int size, T initialValue) {
        lockedList = new ArrayList<>(Collections.nCopies(size, initialValue));
    }

    public T set(int index, T element) {
        Lock wLock = lock.writeLock();
        wLock.lock();
        try {
            return lockedList.set(index, element);
        } finally {
            wLock.unlock();
        }
    }

    public T get(int index) {
        Lock rLock = lock.readLock();
        rLock.lock();
        try {
            // Show that multiple readers
            // may acquire the read lock:
            if (lock.getReadLockCount() > 1) {
                System.out.println(lock.getReadLockCount());
            }
            return lockedList.get(index);
        } finally {
            rLock.unlock();
        }
    }

    public static void main(String[] args) {
        new ReaderWriterListTest(30, 1);
    }
}

class ReaderWriterListTest {
    ExecutorService exec = Executors.newCachedThreadPool();
    private final static int SIZE = 100;
    private static Random rand = new Random(47);
    private ReaderWriterList<Integer> list =
            new ReaderWriterList<>(SIZE, 0);

    private class Writer implements Runnable {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) { // 2 second test
                    list.set(i, rand.nextInt());
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                // Acceptable way to exit
            }
            System.out.println("Writer finished, shutting down");
            exec.shutdownNow();
        }
    }

    private class Reader implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    for (int i = 0; i < SIZE; i++) {
                        list.get(i);
                        TimeUnit.MILLISECONDS.sleep(1);
                    }
                }
            } catch (InterruptedException e) {
                // Acceptable way to exit
            }
        }
    }

    public ReaderWriterListTest(int readers, int writers) {
        for (int i = 0; i < readers; i++) {
            exec.execute(new Reader());
        }
        for (int i = 0; i < writers; i++) {
            exec.execute(new Writer());
        }
    }
}
