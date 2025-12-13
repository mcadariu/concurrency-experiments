package com.mcadariu.concurrency.queues;

public interface Queue<T> {
    void enq(T value) throws InterruptedException;
    T deq() throws InterruptedException;
}
