package com.suikajy.java8note.ex1_thread_review.part1_concurrency.threadPool;

import com.suikajy.java8note.ex1_thread_review.part1_concurrency.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPool {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        // 这种线程池只维护一个线程，所有任务都会在前一个任务执行完再执行。
        for (int i = 0; i < 5; i++) {
            exec.execute(new LiftOff());
        }
        exec.shutdown();
    }
}
