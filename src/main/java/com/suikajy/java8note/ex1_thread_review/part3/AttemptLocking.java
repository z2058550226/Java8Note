package com.suikajy.java8note.ex1_thread_review.part3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized关键字有它的局限性，比如在尝试获取锁失败的情况下，就放弃他。
 * 要实现这些，你必须使用concurrent类库中API
 */
public class AttemptLocking {

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 不计时尝试获取锁
     */
    public void untimed() {
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            if (captured)
                lock.unlock();
        }
    }

    /**
     * 计时尝试获取锁
     */
    public void timed() {
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("tryLock(2, TimeUnit.SECONDS): " + captured);
        } finally {
            if (captured) lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final AttemptLocking al = new AttemptLocking();
        al.untimed(); // True -- lock is available
        al.timed();   // True -- lock is available
        // Now create a separate task to grab the lock:
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                al.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        Thread.yield(); // Give the 2nd task a chance
        TimeUnit.SECONDS.sleep(1);
        al.untimed(); // False -- lock grabbed by task
        al.timed(); // False -- lock grabbed by task
    }

}
