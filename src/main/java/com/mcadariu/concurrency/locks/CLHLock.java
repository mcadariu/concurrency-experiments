package com.mcadariu.concurrency.locks;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock {
    private static class QNode {
        volatile boolean locked;

        QNode(boolean locked) {
            this.locked = locked;
        }
    }

    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;

    public CLHLock() {
        tail = new AtomicReference<>(new QNode(false));
        myNode = new ThreadLocal<>();
    }

    @Override
    public void lock() {
        QNode node = new QNode(true);
        myNode.set(node);
        QNode pred = tail.getAndSet(node);
        while (pred.locked) {
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock() {
        QNode node = myNode.get();
        node.locked = false;
        myNode.set(null);
    }
}
