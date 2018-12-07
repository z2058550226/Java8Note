package com.suikajy.java8note.ex1_thread_review.part7_new_component;


import java.util.List;
import java.util.concurrent.*;

import net.mindview.util.*;

/**
 * Exchanger demo
 * <p>
 * Exchanger的应用场景是：一个任务在创建对象，这些对象的生产代价很高昂，而另一个任务在消费这些对象。通过这种方式
 * 可以有更多的对象在被创建的同时被消费。
 *
 * Exchanger依然是一种生产-消费的并发模型。
 */

class ExchangerProducer<T> implements Runnable {
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;

    public ExchangerProducer(Exchanger<List<T>> exchanger, Generator<T> generator, List<T> holder) {
        this.generator = generator;
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("Next " + ExchangerDemo.size + " Fat instance will be generate");
                for (int i = 0; i < ExchangerDemo.size; i++) {
                    holder.add(generator.next());
                    System.out.println("Producer invoke exchange, holder's hashcode is " + holder.hashCode());
                    holder = exchanger.exchange(holder);
                    System.out.println("Producer's holder hashcode after exchange is " + holder.hashCode());
                }
            }
        } catch (InterruptedException e) {
            // OK to terminate this way.
        }
    }
}

class ExchangerConsumer<T> implements Runnable {
    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    private volatile T value;

    public ExchangerConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("Consumer invoke exchange");
                holder = exchanger.exchange(holder);
                System.out.println("Consumer's holder size after exchange is " + holder.size());
                for (T x :
                        holder) {
                    value = x; // Fetch out value
                    holder.remove(x); // OK for CopyOnWriteArrayList
                }
            }
        } catch (InterruptedException e) {
            // OK to terminate this way.
        }
        System.out.println("Final value: " + value);
    }
}

public class ExchangerDemo {
    static int size = 10;
    static int delay = 5; // Seconds

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> xc = new Exchanger<>();
        List<Fat>
                producerList = new CopyOnWriteArrayList<>(),
                consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducer<>(xc, BasicGenerator.create(Fat.class), producerList));
        exec.execute(new ExchangerConsumer<>(xc, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
    }
}
