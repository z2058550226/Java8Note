package com.suikajy.java8note.ex1_thread_review;

public class LiftOff implements Runnable {

    protected int countDown = 10; // Default
    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff() {
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff") + "). ";
    }

    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.print(status());
            // 一种放权标志，告诉调度器，先别管我了。
            Thread.yield();
        }
    }
}