package com.mcadariu.concurrency.stacks;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EliminationBackoffStack<T> implements Stack<T> {

    private final LockFreeStack<T> stack = new LockFreeStack<>();
    private final Exchanger<T>[] exchangers;
    private final int capacity;
    private final long timeoutNanos;

    @SuppressWarnings("unchecked")
    public EliminationBackoffStack(int capacity, long timeoutMicros) {
        this.capacity = Math.max(1, capacity);
        this.exchangers = (Exchanger<T>[]) new Exchanger[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            exchangers[i] = new Exchanger<>();
        }
        this.timeoutNanos = timeoutMicros * 1000; // Convert to nanoseconds
    }

    public EliminationBackoffStack() {
        this(8, 50); // 8 slots, 50 microsecond timeout
    }

    private int indexForRandomSlot() {
        return ThreadLocalRandom.current().nextInt(capacity);
    }

    @Override
    public void push(T value) throws InterruptedException {
        // Try elimination first - if we can pair with a pop, skip the stack entirely
        int slot = indexForRandomSlot();
        try {
            exchangers[slot].exchange(value, timeoutNanos, TimeUnit.NANOSECONDS);
            // Successfully paired with a pop thread
            return;
        } catch (TimeoutException te) {
            // No pop thread available, fall through to regular push
        }

        // Regular push to the underlying stack
        stack.push(value);
    }

    @Override
    public T pop() throws InterruptedException {
        // Try to pop from stack first
        T value = stack.pop();
        if (value != null) {
            return value;
        }

        // Stack is empty, try to get value via elimination from a push thread
        int slot = indexForRandomSlot();
        try {
            value = exchangers[slot].exchange(null, timeoutNanos, TimeUnit.NANOSECONDS);
            if (value != null) {
                return value;
            }
        } catch (TimeoutException te) {
            // No push thread available
        }

        // Try stack one more time (might have elements now)
        return stack.pop();
    }
}
