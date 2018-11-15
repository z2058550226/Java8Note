package com.suikajy.java8note.ex1_thread_review.part2_concurrency;

import java.util.Arrays;

/**
 * 这个Demo表明了join多次调用的情况下，可以实现等待多个线程全部结束再执行需要的逻辑。
 */
public class JoinTest {

    public static void main(String[] args) {
        SleeperThread sleeper1 = new SleeperThread("sleeper1", 2000);
        SleeperThread sleeper2 = new SleeperThread("sleeper2", 4000);
        SleeperThread sleeper3 = new SleeperThread("sleeper3", 3000);
        JoinerThread joiner = new JoinerThread("joiner", sleeper1, sleeper2, sleeper3);
        /*sleeper1 is awaken
        sleeper1 has joined
        sleeper3 is awaken
        sleeper2 is awaken
        sleeper2 has joined
        sleeper3 has joined
        joiner was completed*/

    }

}

class JoinerThread extends Thread {
    private final SleeperThread[] sleeperThreads;

    JoinerThread(String name, SleeperThread... sleeperThreads) {
        super(name);
        this.sleeperThreads = sleeperThreads;
        start();
    }

    @Override
    public void run() {
        Arrays.stream(sleeperThreads).forEach(sleeperThread -> {
            try {
                sleeperThread.join();
                System.out.println(sleeperThread.getName() + " has joined");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(getName() + " was completed");
    }
}

class SleeperThread extends Thread {
    private final long duration;

    SleeperThread(String name, long duration) {
        super(name);
        this.duration = duration;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " isInterrupted");
        }
        System.out.println(getName() + " is awaken");
    }
}
