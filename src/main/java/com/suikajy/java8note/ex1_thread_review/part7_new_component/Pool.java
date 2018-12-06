package com.suikajy.java8note.ex1_thread_review.part7_new_component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 对象池 —— Semaphore /塞码佛儿/ 计数信号量
 *
 * 这个对象池管理者数量有限的对象，当要使用对象时可以签出它们，在使用完毕时可以将他们签回
 * Semaphore的作用就相当于签发许可证的门卫，它一共有size个许可证，当size个许可证用完时，如果
 * 还有线程来向Semaphore要许可证，那么Semaphore将会把这个线程阻塞(拒之门外)，直到有线程释放对象(调用checkIn)
 */
public class Pool<T> {

    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore available;

    public Pool(Class<T> classObject, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size, true);
        // Load pool with objects that can be checked out:
        for (int i = 0; i < size; i++) {
            try {
                // Assumes a default constructor:
                items.add(classObject.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T checkOut() throws InterruptedException {
        available.acquire();
        return getItem();
    }

    public void checkIn(T x) {
        if (releaseItem(x)) {
            available.release();
        }
    }

    private synchronized T getItem() {
        for (int i = 0; i < size; i++) {
            if (!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }
        return null; // Semaphore prevents reaching here
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1) return false; // Not in the list
        if (checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false; // Wasn't checked out
    }

}
