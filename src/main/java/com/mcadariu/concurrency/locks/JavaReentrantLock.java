package com.mcadariu.concurrency.locks;

import java.util.concurrent.locks.ReentrantLock;

public class JavaReentrantLock implements Lock {
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
