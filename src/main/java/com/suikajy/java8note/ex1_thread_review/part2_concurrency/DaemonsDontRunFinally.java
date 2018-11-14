package com.suikajy.java8note.ex1_thread_review.part2_concurrency;

import java.util.concurrent.TimeUnit;

/**
 * 如果是后台线程，则不会执行finally中的语句
 * 这种设计是对的，因为后台线程可能不会以一种优雅的方式关闭
 * 所以非后台的Executor通常是一种更好的方式。
 */
class ADaemon implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("starting ADaemon");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        } finally {
            // 这句没有执行
            System.out.println("This should always run?");
        }
    }
}

public class DaemonsDontRunFinally {

    public static void main(String[] args) {
        Thread t = new Thread(new ADaemon());
        t.setDaemon(true);
        t.start();
    }

}
