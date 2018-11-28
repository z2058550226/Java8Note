package com.suikajy.java8note.ex1_thread_review.part3_shared_resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 本类证明了volatile不能保证操作的原子性
 */
public class VolatileTest {

    public volatile int inc = 0;

    public void increase() {
        inc++;
    }

    public static void main(String[] args) {
        final VolatileTest test = new VolatileTest();
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++){
            exec.execute(() -> {
                for(int j=0;j<500;j++)
                    test.increase();
            });
        }
        exec.shutdown();

        while(Thread.activeCount()>2)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc); // 这里的线程
    }

}
