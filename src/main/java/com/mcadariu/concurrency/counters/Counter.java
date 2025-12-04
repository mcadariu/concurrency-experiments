package com.mcadariu.concurrency.counters;

public interface Counter {
    void increment();
    long getCount();
    void reset();
}
