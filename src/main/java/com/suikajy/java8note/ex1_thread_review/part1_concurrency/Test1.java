package com.suikajy.java8note.ex1_thread_review.part1_concurrency;

public class Test1 {

    public static void main(String[] args) {

    }

    private static void test1() {
        LiftOff launch = new LiftOff();
        // 由主线程直接驱动一个Runnable
        launch.run();
    }

    private static void test2() {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new LiftOff());
            t.start();
        }
        System.out.println("Waiting for LiftOff");
    }

}
