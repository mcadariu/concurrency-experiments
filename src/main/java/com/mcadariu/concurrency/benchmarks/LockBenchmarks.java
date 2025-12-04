package com.mcadariu.concurrency.benchmarks;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LockBenchmarks {

    public static class CASLock {
        private final AtomicBoolean state = new AtomicBoolean(false);

        public void lock() {
            while (!state.compareAndSet(false, true)) {
            }
        }

        public void unlock() {
            state.set(false);
        }
    }

    public static class TTASLock {
        private final AtomicBoolean state = new AtomicBoolean(false);

        public void lock() {
            for (;;) {
                while (state.get()) {
                }
                if (state.compareAndSet(false, true)) {
                    return;
                }
            }
        }

        public void unlock() {
            state.set(false);
        }
    }

    @State(Scope.Group)
    public static class CasState {
        final CASLock lock = new CASLock();
        int counter = 0;
    }

    @State(Scope.Group)
    public static class TtasState {
        final TTASLock lock = new TTASLock();
        int counter = 0;
    }

    @Group("cas")
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void cas_lock(CasState s) {
        s.lock.lock();
        try {
            s.counter++;
        } finally {
            s.lock.unlock();
        }
    }

    @Group("ttas")
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void ttas_lock(TtasState s) {
        s.lock.lock();
        try {
            s.counter++;
        } finally {
            s.lock.unlock();
        }
    }

    public static class BackoffLock {
        private final AtomicBoolean state = new AtomicBoolean(false);

        public void lock() throws InterruptedException {
            int delay = 1;
            while (true) {
                while (state.get()) {
                }
                if (state.compareAndSet(false, true)) return;
                TimeUnit.NANOSECONDS.sleep(delay);
                delay = Math.min(delay * 2, 1_000_000);
            }
        }

        public void unlock() {
            state.set(false);
        }
    }

    public static class CLHLock {
        private static class QNode { volatile boolean locked = true; }
        private final ThreadLocal<QNode> myNode = ThreadLocal.withInitial(QNode::new);
        private final ThreadLocal<QNode> myPred = new ThreadLocal<>();
        private final AtomicReference<QNode> tail = new AtomicReference<>(new QNode());

        public void lock() {
            QNode node = myNode.get();
            node.locked = true;
            QNode pred = tail.getAndSet(node);
            myPred.set(pred);
            while (pred.locked) {
            }
        }

        public void unlock() {
            QNode node = myNode.get();
            node.locked = false;
            myNode.set(new QNode());
        }
    }

    public static class MCSLock {
        private static class QNode { volatile boolean locked; volatile QNode next; }
        private final AtomicReference<QNode> tail = new AtomicReference<>(null);
        private final ThreadLocal<QNode> myNode = ThreadLocal.withInitial(QNode::new);

        public void lock() {
            QNode node = myNode.get();
            node.locked = true;
            node.next = null;
            QNode pred = tail.getAndSet(node);
            if (pred != null) {
                pred.next = node;
                while (node.locked) {
                }
            }
        }

        public void unlock() {
            QNode node = myNode.get();
            if (node.next == null) {
                if (tail.compareAndSet(node, null)) return;
                while (node.next == null) {}
            }
            node.next.locked = false;
            node.next = null;
        }
    }

    @State(Scope.Group)
    public static class BackoffState {
        final BackoffLock lock = new BackoffLock();
        int counter = 0;
    }

    @Group("backoff")
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void backoff_lock(BackoffState s) throws Exception {
        s.lock.lock();
        try {
            s.counter++;
        } finally {
            s.lock.unlock();
        }
    }

    @State(Scope.Group)
    public static class CLHState {
        final CLHLock lock = new CLHLock();
        int counter = 0;
    }

    @Group("clh")
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void clh_lock(CLHState s) {
        s.lock.lock();
        try {
            s.counter++;
        } finally {
            s.lock.unlock();
        }
    }

    @State(Scope.Group)
    public static class MCSState {
        final MCSLock lock = new MCSLock();
        int counter = 0;
    }

    @Group("mcs")
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void mcs_lock(MCSState s) {
        s.lock.lock();
        try {
            s.counter++;
        } finally {
            s.lock.unlock();
        }
    }
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
                .include(LockBenchmarks.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .forks(1)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
