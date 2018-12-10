package com.suikajy.java8note.ex1_thread_review.part9_opt_performance;

import net.mindview.util.CountingIntegerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {Args: 1 10 10} （Fast verification check during build）
 * Rough comparison of thread-safe List performance.
 *
 * 这个demo测试了同步锁List和CopyOnWriteList的并发效率差距
 *
 * 结论：同步锁读写总数相同的情况下，时间大致是一致的
 *
 */

abstract class ListTest extends Tester<List<Integer>> {

    public ListTest(String testId, int nReaders, int nWriters) {
        super(testId, nReaders, nWriters);
    }

    class Reader extends TestTask {

        long result = 0;

        @Override
        void test() {
            for (long i = 0; i < testCycles; i++) {
                for (int index = 0; index < containerSize; index++) {
                    result += testContainer.get(index);
                }
            }
        }

        @Override
        void putResults() {
            readResult += result;
            readTime += duration;
        }
    }

    class Writer extends TestTask {

        @Override
        void test() {
            for (long i = 0; i < testCycles; i++) {
                for (int index = 0; index < containerSize; index++) {
                    testContainer.set(index, writeData[index]);
                }
            }
        }

        @Override
        void putResults() {
            writeTime += duration;
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    void startReadersAndWriters() {
        for (int i = 0; i < nReaders; i++) {
            exec.execute(new Reader());
        }
        for (int i = 0; i < nWriters; i++) {
            exec.execute(new Writer());
        }
    }
}

class SynchronizedArrayLsitTest extends ListTest {

    public SynchronizedArrayLsitTest(int nReaders, int nWriters) {
        super("Synched ArrayList", nReaders, nWriters);
    }

    @Override
    List<Integer> containerInitializer() {
        return Collections.synchronizedList(
                new ArrayList<>(
                        new CountingIntegerList(containerSize)));
    }
}

class CopyOnWriteArrayListTest extends ListTest {

    public CopyOnWriteArrayListTest(int nReaders, int nWriters) {
        super("CopyOnWriteArrayList", nReaders, nWriters);
    }

    @Override
    List<Integer> containerInitializer() {
        return new CopyOnWriteArrayList<>(new CountingIntegerList(containerSize));
    }
}

public class ListComparisons {
    public static void main(String[] args) {
        // 三个参数分别是：整体测试重复次数，读取和写入重复次数，集合大小
        Tester.initMain(new String[]{"1", "10", "10"});
        new SynchronizedArrayLsitTest(10, 0);
        new SynchronizedArrayLsitTest(9, 1);
        new SynchronizedArrayLsitTest(5, 5);
        new CopyOnWriteArrayListTest(10, 0);
        new CopyOnWriteArrayListTest(9, 1);
        new CopyOnWriteArrayListTest(5, 5);
        Tester.exec.shutdown();
    }
}
