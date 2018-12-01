package com.suikajy.java8note.ex1_thread_review.part6_deadlock;

/**
 * 大学时玩的哲学家吃筷子问题
 * <p>
 * 这个问题由狄克斯特拉提出
 * <p>
 * 筷子代表死锁问题中的第一个条件：互斥资源，这个资源只能被一个线程使用，但每个线程需要有多个互斥资源才能运行
 */
public class Chopstick {
    private boolean taken = false;

    public synchronized void take() throws InterruptedException {
        while (taken) wait();
        taken = true;
    }

    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}
