package com.suikajy.java8note.ex1_thread_review.part7_new_component;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

/**
 * DelayQueue demo
 *
 * DelayQueue接收实现了Delayed的参数，当一个线程调用DelayQueue::take()方法时
 * 会阻塞当前线程，然后根据Delayed实例对象的getDelay()方法的返回值判断需要阻塞
 * 多长时间来获取这个DelayQueue中的对象
 */

class DelayedTask implements Runnable, Delayed {

    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
        sequence.add(this);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(
                trigger - System.nanoTime(), NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if (trigger < that.trigger) return -1;
        if (trigger > that.trigger) return 1;
        return 0;
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    @Override
    public String toString() {
        return String.format("[%1$-4d]", delta) + " Task " + id;
    }

    public String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class Endsentinel extends DelayedTask {

        private ExecutorService exec;

        public Endsentinel(int delay, ExecutorService e) {
            super(delay);
            exec = e;
        }

        @Override
        public void run() {
            for (DelayedTask pt : sequence) {
                System.out.println(pt.summary() + " ");
            }
            System.out.println();
            System.out.println(this + " Calling shutdownNow()");
            exec.shutdownNow();
        }
    }
}

class DelayedTaskConsumer implements Runnable {

    private DelayQueue<DelayedTask> q;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                q.take().run(); // Run task with the current thread
            }
        } catch (InterruptedException e) {
            // Acceptable way to exit
        }
        System.out.println("Finished DelayedTaskConsumer");
    }
}

public class DelayQueueDemo {

    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        // Fill with tasks that have random delays:
        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(rand.nextInt(50000)));
        }
        // Set the stopping point
        queue.add(new DelayedTask.Endsentinel(50000, exec));
        exec.execute(new DelayedTaskConsumer(queue));
    }
}
