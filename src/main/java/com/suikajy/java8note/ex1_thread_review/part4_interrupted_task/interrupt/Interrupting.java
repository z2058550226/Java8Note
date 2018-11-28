package com.suikajy.java8note.ex1_thread_review.part4_interrupted_task.interrupt;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// 用来演示中断sleep状态下的线程
// sleep阻塞可以被完美的中断，并抛出中断异常
class SleepBlocked implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
        System.out.println("Exiting SleepBlock.run()");
    }
}

// 用来演示中断等待输入/输出状态下的线程
// 结论：IO阻塞无法被中断，这里也不会输出：“Exiting IOBlocked.run()”
// 所以在实行IO任务时，它具有锁住你多线程程序的潜在可能性。
class IOBlocked implements Runnable {
    private InputStream in;

    public IOBlocked(InputStream is) {
        in = is;
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for read():");
            in.read();
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted from blocked I/O");
            } else {
                System.out.println("Interrupted, but not by Thread.interrupt()");
            }
        }
        System.out.println("Exiting IOBlocked.run()");
    }
}

// 用来演示中断处于等待锁状态下的线程
// 结论：等待锁状态的阻塞也不会被中断
class SynchronizedBlocked implements Runnable {

    public synchronized void f() {
        while (true) // Never releases lock
            Thread.yield();
    }

    public SynchronizedBlocked() {
        new Thread() {
            @Override
            public void run() {
                f(); // Lock acquired by this thread
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }
}
// 中断线程在Java5中的做法是调用Future的cancel方法。而不是直接调用线程对象的interrupt方法
public class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    static void test(Runnable r) throws InterruptedException {
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Interrupting " + r.getClass().getSimpleName());
        f.cancel(true); // Interrupts if running.
        System.out.println("Interrupt sent to " + r.getClass().getSimpleName());
    }

    public static void main(String[] args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0); // ... since last 2 interrupts failed
    }
}
