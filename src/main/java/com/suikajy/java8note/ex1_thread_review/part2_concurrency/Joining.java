package com.suikajy.java8note.ex1_thread_review.part2_concurrency;


class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interruped. " + "isInterrupted():" + isInterrupted());
            return;
        }
        System.out.println(getName() + " has awakened");
    }
}

public class Joining {


}
