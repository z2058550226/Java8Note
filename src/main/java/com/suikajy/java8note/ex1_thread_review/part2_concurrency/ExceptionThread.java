package com.suikajy.java8note.ex1_thread_review.part2_concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 一个线程中的异常在其他线程中无法捕获
 */
public class ExceptionThread implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(new ExceptionThread());
        } catch (RuntimeException ue) {
            System.out.println("Exception has been handled!");
        }
        TimeUnit.SECONDS.sleep(1);
        System.out.println("main thread over");
    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
