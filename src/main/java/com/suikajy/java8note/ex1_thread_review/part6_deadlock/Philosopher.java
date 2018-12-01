package com.suikajy.java8note.ex1_thread_review.part6_deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 哲学家获取筷子的方式代表了死锁问题的第二个条件：至少有一个任务必须持有一个资源而且正在等待当前别的任务
 * 持有的资源，简而言之，就是必须拿着一根筷子等待另一根。
 * <p>
 * 哲学家释放筷子的方式代表了死锁成立的第三个条件：资源无法被抢占，他们很有礼貌，不会去抢别人的筷子
 * <p>
 * 筷子的获取方式代表了死锁问题成立的第四个条件：资源互斥构成闭环等待
 * <p>
 * 这其中，第四个条件是最容易破坏的，也是解决死锁问题最直接的方式：让其中一个哲学家先拿右边的筷子
 * 这样他就不会去阻止左侧的人拿筷子了，死锁也永远不会发生
 */

public class Philosopher implements Runnable {

    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);

    private void pause() throws InterruptedException {
        if (ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }

    public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " " + "thinking");
                pause();
                // Philosopher becomes hungry
                System.out.println(this + " " + "grabbing right");
                right.take();
                System.out.println(this + " " + "grabbing left");
                left.take();
                System.out.println(this + " " + "eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            System.out.println(this + " " + "exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}
