package com.suikajy.java8note.ex1_thread_review.part1_concurrency.threadPool;

import com.suikajy.java8note.ex1_thread_review.part1_concurrency.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        // 指定了线程池维护线程的数量为5，当所有线程都在运行时没有
        // 得到线程的任务会等待其他任务执行完之后复用那个线程
        // 这种线程池适合IO密集运算
        for (int i = 0; i < 15; i++) {
            exec.execute(new LiftOff(3));
        }
        exec.shutdown();
    }
}
