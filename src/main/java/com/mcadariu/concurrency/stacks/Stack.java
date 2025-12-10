package com.mcadariu.concurrency.stacks;

public interface Stack<T> {
    void push(T value) throws InterruptedException;
    T pop() throws InterruptedException;
}
