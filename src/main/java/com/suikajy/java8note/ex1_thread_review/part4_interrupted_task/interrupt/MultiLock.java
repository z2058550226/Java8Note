package com.suikajy.java8note.ex1_thread_review.part4_interrupted_task.interrupt;

/**
 * 被互斥所阻塞
 * <p>
 * 就像Interrupting.java中看到的一样，如果你尝试着在一个对象上调用其synchronized方法，
 * 而这个对象的锁已经被其他任务获得，那么调用任务将被挂起（阻塞），直至这个锁可获得。
 * 下面的示例说明了同一个互斥刻意如何能被同一个任务多次获得：
 */
public class MultiLock {

    public synchronized void f1(int count) {
        if (count-- > 0) {
            System.out.println("f1() calling f2() with count " + count);
            f2(count);
        }
    }

    public synchronized void f2(int count) {
        if (count-- > 0) {
            System.out.println("f2() calling f1() with count " + count);
            f1(count);
        }
    }

    public static void main(String[] args) {
        final MultiLock multiLock = new MultiLock();
        new Thread() {
            @Override
            public void run() {
                multiLock.f1(10);
            }
        }.start();
    }

}
