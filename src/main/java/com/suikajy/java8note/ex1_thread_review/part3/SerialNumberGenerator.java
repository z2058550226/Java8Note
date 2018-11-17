package com.suikajy.java8note.ex1_thread_review.part3;

public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;

    public static synchronized int nextSerialNumber() {
        return serialNumber++; // Not thread-safe
    }
}
