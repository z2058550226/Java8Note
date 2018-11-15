package com.suikajy.java8note.ex1_thread_review.part3;

public abstract class IntGenerator {

    /**
     * 关于volatile：volatile
     */
    private volatile boolean canceled = false;

    public abstract int next();

    // Allow this to be canceled
    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
