package com.suikajy.java8note.ex1_thread_review.part1_concurrency;

import java.util.concurrent.TimeUnit;

// 后台（daemon）线程
public class SimpleDaemons implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread daemon = new Thread(new SimpleDaemons());
            // 必须要在start之前调用这个方法，才能把它设置为后台线程。
            daemon.setDaemon(true); // Must call before start()
            daemon.start();
        }
        System.out.println("All daemons started");
        // 一旦main()完成了工作或者被终止，那么这个程序就终止了
        // 因为出了后台线程之外已经没有其他线程在运行了。
        // 这里将main()设置了短暂的休眠，调试这个时间来验证后台线程的终止
        TimeUnit.MILLISECONDS.sleep(175);
    }

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            System.out.println("sleep() interrupted");
        }
    }
}
