package com.mcadariu.concurrency.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements Lock {
    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() throws InterruptedException {
        int delay = 1;
        while (true) {
            while (state.get()) {
            }
            if (state.compareAndSet(false, true)) return;
            TimeUnit.NANOSECONDS.sleep(delay);
            delay = Math.min(delay * 2, 1_000_000);
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
