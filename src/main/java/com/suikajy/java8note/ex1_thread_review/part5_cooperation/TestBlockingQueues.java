package com.suikajy.java8note.ex1_thread_review.part5_cooperation;


import com.suikajy.java8note.ex1_thread_review.part2_concurrency.LiftOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

/**
 * 生产者-消费者与队列
 *
 * 使用BlockingQueue可以忽视wait()和notify()的同步问题，因为
 * 它们被封装在了BlockingQueue里面
 */

class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> queue) {
        this.rockets = queue;
    }

    public void add(LiftOff lo) {
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.print("Interrupted during put()");
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                LiftOff rocket = rockets.take();// 会等待rockets.put的调用
                rocket.run(); // Use this thread
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}


public class TestBlockingQueues {
    private static void getKey() {
        try {
            // Compensate for Windows/Linux difference in the
            // length of the result produced by the Enter key:
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getKey(String message) {
        System.out.println(message);
        getKey();
    }

    private static void test(String msg, BlockingQueue<LiftOff> queue) {
        System.out.println(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++) {
            runner.add(new LiftOff(5));
        }
        getKey("Press 'Enter' (" + msg + ")");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue",// Unlimited size
                new LinkedBlockingDeque<>());
        test("ArrayBlockingQueue",// Fixed size
                new ArrayBlockingQueue<>(3));
        test("SynchronousQueue",// Size of 1
                new SynchronousQueue<>());
    }
}
