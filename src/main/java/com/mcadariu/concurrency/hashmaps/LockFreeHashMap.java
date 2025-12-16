package com.mcadariu.concurrency.hashmaps;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeHashMap<K, V> implements ConcurrentHashMap<K, V> {
    private static class Node<K, V> {
        final K key;
        final V value;
        final int hash;
        final AtomicMarkableReference<Node<K, V>> next;

        Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = new AtomicMarkableReference<>(null, false);
        }
    }

    private final AtomicMarkableReference<Node<K, V>>[] table;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public LockFreeHashMap(int capacity) {
        this.capacity = capacity;
        this.table = new AtomicMarkableReference[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new AtomicMarkableReference<>(null, false);
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private static class Window<K, V> {
        Node<K, V> pred;
        Node<K, V> curr;

        Window(Node<K, V> pred, Node<K, V> curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }

    private Window<K, V> find(K key, int hash) {
        int index = hash % capacity;
        AtomicMarkableReference<Node<K, V>> head = table[index];

        retry: while (true) {
            Node<K, V> pred = null;
            Node<K, V> curr = head.getReference();

            while (curr != null) {
                Node<K, V> succ = curr.next.getReference();
                boolean[] marked = {false};
                succ = curr.next.get(marked);

                while (marked[0]) {
                    if (pred == null) {
                        if (!head.compareAndSet(curr, succ, false, false)) {
                            continue retry;
                        }
                    } else {
                        if (!pred.next.compareAndSet(curr, succ, false, false)) {
                            continue retry;
                        }
                    }
                    curr = succ;
                    if (curr == null) break;
                    succ = curr.next.get(marked);
                }

                if (curr == null || curr.hash >= hash) {
                    return new Window<>(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
            return new Window<>(pred, null);
        }
    }

    @Override
    public boolean add(K key, V value) {
        int hash = hash(key);
        while (true) {
            Window<K, V> window = find(key, hash);
            Node<K, V> pred = window.pred;
            Node<K, V> curr = window.curr;

            if (curr != null && curr.key != null && curr.key.equals(key)) {
                return false; // Already exists
            }

            Node<K, V> node = new Node<>(key, value, hash);
            node.next.set(curr, false);

            if (pred == null) {
                if (table[hash % capacity].compareAndSet(curr, node, false, false)) {
                    return true;
                }
            } else {
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    @Override
    public boolean remove(K key) {
        int hash = hash(key);
        while (true) {
            Window<K, V> window = find(key, hash);
            Node<K, V> pred = window.pred;
            Node<K, V> curr = window.curr;

            if (curr == null || !curr.key.equals(key)) {
                return false;
            }

            Node<K, V> succ = curr.next.getReference();
            if (!curr.next.compareAndSet(succ, succ, false, true)) {
                continue;
            }

            if (pred == null) {
                table[hash % capacity].compareAndSet(curr, succ, false, false);
            } else {
                pred.next.compareAndSet(curr, succ, false, false);
            }
            return true;
        }
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        Window<K, V> window = find(key, hash);
        Node<K, V> curr = window.curr;

        if (curr != null && curr.key != null && curr.key.equals(key)) {
            return curr.value;
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }
}
