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
            // 让步操作，一种对调度器的提示，提醒调度器可以执行别的任务了
            // 这个操作十分鸡肋，它仅仅是一种提示。
            // 如果有重要的控制或在调整应用时都不能依赖它。在实际使用中它经常被误用。
            Thread.yield();
        }
    }
}