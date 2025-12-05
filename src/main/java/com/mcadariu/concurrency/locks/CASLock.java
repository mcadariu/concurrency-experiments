package com.mcadariu.concurrency.locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class CASLock implements Lock {
    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (!state.compareAndSet(false, true)) {
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
