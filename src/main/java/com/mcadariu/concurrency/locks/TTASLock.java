package com.mcadariu.concurrency.locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock implements Lock {
    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        for (;;) {
            while (state.get()) {
            }
            if (state.compareAndSet(false, true)) {
                return;
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
