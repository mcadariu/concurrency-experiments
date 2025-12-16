package com.mcadariu.concurrency.hashmaps;

public interface ConcurrentHashMap<K, V> {
    boolean add(K key, V value);
    boolean remove(K key);
    V get(K key);
    boolean contains(K key);
}
