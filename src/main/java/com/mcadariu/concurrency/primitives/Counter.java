package com.mcadariu.concurrency.primitives;

public interface Counter {
    void increment();
    long getCount();
    void reset();
}
