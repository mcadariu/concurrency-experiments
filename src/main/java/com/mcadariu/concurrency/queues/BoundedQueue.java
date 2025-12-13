package com.mcadariu.concurrency.queues;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> implements Queue<T> {
    private final T[] items;
    private final Lock enqLock = new ReentrantLock();
    private final Lock deqLock = new ReentrantLock();
    private final Condition notEmptyCondition = deqLock.newCondition();
    private final Condition notFullCondition = enqLock.newCondition();

    private volatile int head = 0;
    private volatile int tail = 0;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public BoundedQueue(int capacity) {
        this.capacity = capacity;
        this.items = (T[]) new Object[capacity];
    }

    @Override
    public void enq(T value) throws InterruptedException {
        boolean mustWakeDequeuers = false;
        enqLock.lock();
        try {
            while (tail - head == capacity) {
                notFullCondition.await();
            }
            items[tail % capacity] = value;
            tail++;
            if (tail - head == 1) {
                mustWakeDequeuers = true;
            }
        } finally {
            enqLock.unlock();
        }
        if (mustWakeDequeuers) {
            deqLock.lock();
            try {
                notEmptyCondition.signalAll();
            } finally {
                deqLock.unlock();
            }
        }
    }

    @Override
    public T deq() throws InterruptedException {
        T value;
        boolean mustWakeEnqueuers = false;
        deqLock.lock();
        try {
            while (tail == head) {
                notEmptyCondition.await();
            }
            value = items[head % capacity];
            head++;
            if (tail - head == capacity - 1) {
                mustWakeEnqueuers = true;
            }
        } finally {
            deqLock.unlock();
        }
        if (mustWakeEnqueuers) {
            enqLock.lock();
            try {
                notFullCondition.signalAll();
            } finally {
                enqLock.unlock();
            }
        }
        return value;
    }
}
