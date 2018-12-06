package com.suikajy.java8note.ex1_thread_review.part7_new_component;


import java.util.List;
import java.util.concurrent.Exchanger;

import net.mindview.util.*;

class ExchangerProducer<T> implements Runnable{
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;

    public ExchangerProducer(Generator<T> generator, Exchanger<List<T>> exchanger, List<T> holder) {
        this.generator = generator;
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {

    }
}

public class ExchangerDemo {
}
