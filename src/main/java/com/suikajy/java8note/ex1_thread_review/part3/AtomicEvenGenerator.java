package com.suikajy.java8note.ex1_thread_review.part3;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicEvenGenerator extends IntGenerator {

    // 虽然用Atomic类能消除单个类的操作出现问题，
    // 但如果逻辑更复杂起来的时候还是使用锁更安全一些
    private AtomicInteger currentEvenValue = new AtomicInteger(0);

    @Override
    public int next() {
        return currentEvenValue.addAndGet(2);
    }

    public static void main(String[] args) {
        EvenChecker.test(new AtomicEvenGenerator());
    }
}
