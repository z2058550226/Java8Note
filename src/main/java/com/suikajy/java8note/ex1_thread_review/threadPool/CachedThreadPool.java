package com.suikajy.java8note.ex1_thread_review.threadPool;

import com.suikajy.java8note.ex1_thread_review.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // 这种线程池维护的线程数量是无界的，闲置的线程会被复用
        // 通常使用线程池的首选，当发现问题时可以切换到FixedThreadPool
        // 这种线程池适合计算密集型任务。
        for (int i = 0; i < 5; i++) {
            exec.execute(new LiftOff());
        }
        // 这个方法是为了防止新的任务被提交给这个Executor，减小变数
        // 这个程序将在Executor中所有任务完成后尽快退出。
        exec.shutdown();
    }

}
