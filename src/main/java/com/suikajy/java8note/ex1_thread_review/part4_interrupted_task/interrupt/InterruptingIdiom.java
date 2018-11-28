package com.suikajy.java8note.ex1_thread_review.part4_interrupted_task.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 检查中断
 * <p>
 * 这个类的代码展示了一种线程被中断时的资源清理方式。
 * 这里的中断可以中断sleep，但无法中断for循环中的大运算量操作
 */
class NeedsCleanup {
    private final int id;

    public NeedsCleanup(int ident) {
        id = ident;
        System.out.println("NeedsCleanup " + id);
    }

    public void cleanup() {
        System.out.println("Cleaning up " + id);
    }
}

class Blocked3 implements Runnable {

    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // point1
                NeedsCleanup n1 = new NeedsCleanup(1);
                // Start try-finally immediately after definition
                // of n1, to guarantee proper cleanup of n1:
                try {
                    System.out.println("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    // point2
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    // Guarantee proper cleanup of n2:
                    try {
                        System.out.println("Calculating");
                        // A time-consuming. non-blocking operation:
                        System.out.println("start cal " + System.currentTimeMillis());
                        for (int i = 0; i < 250000000; i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("end cal " + System.currentTimeMillis());
                        System.out.println("Finished time-consuming operation");
                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        }
    }
}

public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        final int sleepTime = 1300;
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(sleepTime);
        System.out.println("Interrupt time: " + System.currentTimeMillis());
        t.interrupt();
    }
}
