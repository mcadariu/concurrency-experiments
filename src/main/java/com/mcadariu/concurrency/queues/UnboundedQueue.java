package com.mcadariu.concurrency.queues;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnboundedQueue<T> implements Queue<T> {
    private static class Node<E> {
        E value;
        volatile Node<E> next = null;

        Node(E value) {
            this.value = value;
        }
    }

    private final Lock enqLock = new ReentrantLock();
    private final Lock deqLock = new ReentrantLock();
    private final Condition notEmptyCondition = deqLock.newCondition();

    private volatile Node<T> head;
    private volatile Node<T> tail;

    public UnboundedQueue() {
        head = new Node<>(null);
        tail = head;
    }

    @Override
    public void enq(T value) {
        Node<T> node = new Node<>(value);
        enqLock.lock();
        try {
            tail.next = node;
            tail = node;
        } finally {
            enqLock.unlock();
        }
        deqLock.lock();
        try {
            notEmptyCondition.signalAll();
        } finally {
            deqLock.unlock();
        }
    }

    @Override
    public T deq() throws InterruptedException {
        T value;
        deqLock.lock();
        try {
            while (head.next == null) {
                notEmptyCondition.await();
            }
            value = head.next.value;
            head = head.next;
        } finally {
            deqLock.unlock();
        }
        return value;
    }
}
