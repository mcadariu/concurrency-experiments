package com.mcadariu.concurrency.locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements Lock {
    private static class QNode {
        volatile boolean locked;
        volatile QNode next;

        QNode() {
            this.locked = false;
            this.next = null;
        }
    }

    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;

    public MCSLock() {
        tail = new AtomicReference<>(null);
        myNode = new ThreadLocal<>();
    }

    @Override
    public void lock() {
        QNode node = new QNode();
        node.locked = true;
        myNode.set(node);

        QNode pred = tail.getAndSet(node);
        if (pred != null) {
            pred.next = node;
            while (node.locked) {
                Thread.onSpinWait();
            }
        }
    }

    @Override
    public void unlock() {
        QNode node = myNode.get();
        if (node.next == null) {
            if (tail.compareAndSet(node, null)) {
                myNode.set(null);
                return;
            }
            // Someone is about to link to us, wait for them
            while (node.next == null) {
                Thread.onSpinWait();
            }
        }
        // Pass lock to next thread
        node.next.locked = false;
        myNode.set(null);
    }
}
