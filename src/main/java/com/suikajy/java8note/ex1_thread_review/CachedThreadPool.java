package com.suikajy.java8note.ex1_thread_review;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new LiftOff());
        }
        // 这个方法是为了防止新的任务被提交给这个Executor
        // 这个程序将在Executor中所有任务完成后尽快退出。
        exec.shutdown();
    }

}
