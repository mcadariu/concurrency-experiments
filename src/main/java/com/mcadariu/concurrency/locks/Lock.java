package com.mcadariu.concurrency.locks;

public interface Lock {
    void lock() throws InterruptedException;
    void unlock();
}
