package com.suikajy.java8note.ex1_thread_review.part3_shared_resource.atomic;

public class Atomicity {

    int i;

    void f1() {
        i++;
    }

    void f2() {
        i += 3;
    }

}
