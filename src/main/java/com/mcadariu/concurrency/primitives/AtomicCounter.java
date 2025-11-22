package com.mcadariu.concurrency.primitives;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter implements Counter {
    private final AtomicLong count = new AtomicLong(0);

    @Override
    public void increment() {
        count.incrementAndGet();
    }

    @Override
    public long getCount() {
        return count.get();
    }

    @Override
    public void reset() {
        count.set(0);
    }
}
