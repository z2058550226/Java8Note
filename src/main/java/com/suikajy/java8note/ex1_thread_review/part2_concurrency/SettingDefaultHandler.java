package com.suikajy.java8note.ex1_thread_review.part2_concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可以对线程设置默认的异常捕获处理器
 */

public class SettingDefaultHandler {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceptionThread());// caught java.lang.RuntimeException
        exec.shutdown();
    }

}
