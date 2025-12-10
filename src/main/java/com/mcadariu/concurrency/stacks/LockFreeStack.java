package com.mcadariu.concurrency.stacks;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> implements Stack<T> {
    static final class Node<E> {
        final E value;
        final Node<E> next;

        Node(E v, Node<E> n) {
            value = v;
            next = n;
        }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>(null);


    @Override
    public void push(T value) {
        Node<T> newNode;
        while (true) {
            Node<T> oldHead = head.get();
            newNode = new Node<>(value, oldHead);
            if (head.compareAndSet(oldHead, newNode)) return;
            Thread.onSpinWait();
        }
    }

    @Override
    public T pop() {
        while (true) {
            Node<T> oldHead = head.get();
            if (oldHead == null) return null;
            Node<T> newHead = oldHead.next;
            if (head.compareAndSet(oldHead, newHead)) return oldHead.value;
            Thread.onSpinWait();
        }
    }
}
