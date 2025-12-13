package com.mcadariu.concurrency.queues;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements Queue<T> {
    private static class Node<E> {
        final E value;
        final AtomicReference<Node<E>> next;

        Node(E value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> sentinel = new Node<>(null);
        this.head = new AtomicReference<>(sentinel);
        this.tail = new AtomicReference<>(sentinel);
    }

    @Override
    public void enq(T value) {
        Node<T> node = new Node<>(value);
        while (true) {
            Node<T> last = tail.get();
            Node<T> next = last.next.get();

            Node<T> currentTail;
            while ((currentTail = tail.get()) != last) {
                last = currentTail;
                next = last.next.get();
                Thread.onSpinWait();
            }

            if (next == null) {
                if (last.next.compareAndSet(next, node)) {
                    tail.compareAndSet(last, node);
                    return;
                }
            } else {
                tail.compareAndSet(last, next);
            }
        }
    }

    @Override
    public T deq() {
        while (true) {
            Node<T> first = head.get();
            Node<T> last = tail.get();
            Node<T> next = first.next.get();

            Node<T> currentHead;
            while ((currentHead = head.get()) != first) {
                first = currentHead;
                last = tail.get();
                next = first.next.get();
                Thread.onSpinWait();
            }

            if (first == last) {
                if (next == null) {
                    return null;
                }
                tail.compareAndSet(last, next);
            } else {
                T value = next.value;
                if (head.compareAndSet(first, next)) {
                    return value;
                }
            }
        }
    }
}
