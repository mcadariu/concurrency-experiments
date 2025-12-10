package com.mcadariu.concurrency.stacks;

public class SynchronizedStack<T> implements Stack<T> {
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E v, Node<E> n) {
            value = v;
            next = n;
        }
    }

    private Node<T> head = null;


    @Override
    public synchronized void push(T value) {
        head = new Node<>(value, head);
        notifyAll();
    }

    @Override
    public synchronized T pop() {
        if (head == null) {
            return null;
        }
        T v = head.value;
        head = head.next;
        return v;
    }
}
