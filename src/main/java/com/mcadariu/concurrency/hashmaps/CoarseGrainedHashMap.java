package com.mcadariu.concurrency.hashmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedHashMap<K, V> implements ConcurrentHashMap<K, V> {
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final List<Node<K, V>> table;
    private final Lock lock = new ReentrantLock();
    private final int capacity;

    public CoarseGrainedHashMap(int capacity) {
        this.capacity = capacity;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(null);
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    @Override
    public boolean add(K key, V value) {
        lock.lock();
        try {
            int index = hash(key);
            Node<K, V> node = table.get(index);
            while (node != null) {
                if (node.key.equals(key)) {
                    return false;
                }
                node = node.next;
            }
            Node<K, V> newNode = new Node<>(key, value);
            newNode.next = table.get(index);
            table.set(index, newNode);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(K key) {
        lock.lock();
        try {
            int index = hash(key);
            Node<K, V> pred = null;
            Node<K, V> curr = table.get(index);
            while (curr != null) {
                if (curr.key.equals(key)) {
                    if (pred == null) {
                        table.set(index, curr.next);
                    } else {
                        pred.next = curr.next;
                    }
                    return true;
                }
                pred = curr;
                curr = curr.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            int index = hash(key);
            Node<K, V> node = table.get(index);
            while (node != null) {
                if (node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }
}
