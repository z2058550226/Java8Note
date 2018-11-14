package com.suikajy.java8note.ex1_thread_review.part2_concurrency;


import java.util.concurrent.TimeUnit;

/**
 * 可以通过isDaemon()方法来确定线程是否是一个后台线程。
 * 如果是一个后台线程，那么它所创建的线程也将自动被设置为后台线程
 */
class Daemon implements Runnable {
    private Thread[] t = new Thread[10];

    @Override
    public void run() {
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new DaemonSpawn());
            t[i].start();
            System.out.println("DaemonSpawn " + i + " started. ");
        }
        for (int i = 0; i < t.length; i++) {
            System.out.println("t[" + i + "].isDaemon() = " + t[i].isDaemon() + ".");
        }
        while (true) Thread.yield();
    }
}

class DaemonSpawn implements Runnable {

    @Override
    public void run() {
        while (true) Thread.yield();
    }
}

public class Daemons {
    public static void main(String[] args) throws InterruptedException {
        Thread d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();
        System.out.println("d.isDaemon() = " + d.isDaemon() + ". ");
        // Allow the daemon threads to
        // finish their startup processes
        TimeUnit.SECONDS.sleep(1);
    }
}
