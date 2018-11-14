package com.suikajy.java8note.ex1_thread_review.part1_concurrency;

import java.util.ArrayList;
import java.util.concurrent.*;

// Callable 可以从线程执行中获取返回值（类似C的POSIX线程）
public class CallableDemo {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // submit方法会返回一个Future对象，这个对象可以用isDone()检查是否已完成
            // 调用get()方法用来获取结果。也可以直接调用get()，这样会阻塞直至准备结果就绪
            results.add(exec.submit(new TaskWithResult(i)));
        }
        for (Future<String> fs : results) {
            try {
                System.out.println(fs.get());
            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (ExecutionException e) {
                System.out.println(e);
            }finally {
                exec.shutdown();
            }
        }
    }
}

// 同一类文件中声明的非public类表示当前包级别可用
// 它的包名和这个文件中声明的public类的包名是相同的
class TaskWithResult implements Callable<String> {

    private int id;

    TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithResult " + id;
    }
}