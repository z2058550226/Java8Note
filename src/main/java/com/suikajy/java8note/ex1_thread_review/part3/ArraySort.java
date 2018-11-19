package com.suikajy.java8note.ex1_thread_review.part3;

import java.util.Arrays;

public class ArraySort implements Runnable {

    private String num;

    public ArraySort(int num) {
        this.num = num + "";
    }

    public static void main(String[] args) {
        int[] nums = {11, 3, 222, 333, 444, 555, 1, 152, 93,2};
        Arrays.stream(nums)
                .forEach(num1 -> new Thread(new ArraySort(num1)).start());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(Integer.parseInt(num));
            System.out.println(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
