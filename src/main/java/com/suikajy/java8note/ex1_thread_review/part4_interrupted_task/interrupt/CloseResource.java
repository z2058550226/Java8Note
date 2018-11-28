package com.suikajy.java8note.ex1_thread_review.part4_interrupted_task.interrupt;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 为了解决线程无法被中断的问题，有一个略显笨拙，但行之有效的解决方案，
 * 即关闭任务在其上发生阻塞的底层资源。
 */
public class CloseResource {

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream socketInput =
                new Socket("localhost", 8080).getInputStream();
        exec.execute(new IOBlocked(socketInput));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Shutting down all threads");
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + socketInput.getClass().getName());
        socketInput.close(); // Releases blocked thread
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + System.in.getClass().getName());
        // todo：这里可能在后续JDK中更改了写法
        // 当前测试JDK是1.8，这里调用标准输入流的输入方法并不能像书中说的那样关闭流的读取！
        System.in.close(); // Releases blocked thread
        System.out.println("System.in.close() is invoked");
    }
}
