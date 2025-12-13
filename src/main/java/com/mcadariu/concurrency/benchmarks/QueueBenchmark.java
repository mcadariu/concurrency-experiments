package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.queues.BoundedQueue;
import com.mcadariu.concurrency.queues.LockFreeQueue;
import com.mcadariu.concurrency.queues.Queue;
import com.mcadariu.concurrency.queues.UnboundedQueue;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class QueueBenchmark {

    private Queue<Integer> boundedQueue;
    private Queue<Integer> unboundedQueue;
    private Queue<Integer> lockFreeQueue;

    private AtomicInteger dummy;

    @Setup(Level.Iteration)
    public void setup() throws InterruptedException {
        boundedQueue = new BoundedQueue<>(10000);
        unboundedQueue = new UnboundedQueue<>();
        lockFreeQueue = new LockFreeQueue<>();
        dummy = new AtomicInteger(0);

        for (int i = 0; i < 5000; i++) {
            boundedQueue.enq(1);
            unboundedQueue.enq(1);
            lockFreeQueue.enq(1);
        }
    }

    private void exercise(Queue<Integer> queue) throws InterruptedException {
        queue.enq(1);
        Integer v = queue.deq();
        if (v != null) dummy.addAndGet(v);
    }

    @Benchmark
    @Threads(1)
    public void boundedQueue_1Thread() throws InterruptedException {
        exercise(boundedQueue);
    }

    @Benchmark
    @Threads(4)
    public void boundedQueue_4Threads() throws InterruptedException {
        exercise(boundedQueue);
    }

    @Benchmark
    @Threads(8)
    public void boundedQueue_8Threads() throws InterruptedException {
        exercise(boundedQueue);
    }

    @Benchmark
    @Threads(16)
    public void boundedQueue_16Threads() throws InterruptedException {
        exercise(boundedQueue);
    }

    @Benchmark
    @Threads(1)
    public void unboundedQueue_1Thread() throws InterruptedException {
        exercise(unboundedQueue);
    }

    @Benchmark
    @Threads(4)
    public void unboundedQueue_4Threads() throws InterruptedException {
        exercise(unboundedQueue);
    }

    @Benchmark
    @Threads(8)
    public void unboundedQueue_8Threads() throws InterruptedException {
        exercise(unboundedQueue);
    }

    @Benchmark
    @Threads(16)
    public void unboundedQueue_16Threads() throws InterruptedException {
        exercise(unboundedQueue);
    }

    @Benchmark
    @Threads(1)
    public void lockFreeQueue_1Thread() throws InterruptedException {
        exercise(lockFreeQueue);
    }

    @Benchmark
    @Threads(4)
    public void lockFreeQueue_4Threads() throws InterruptedException {
        exercise(lockFreeQueue);
    }

    @Benchmark
    @Threads(8)
    public void lockFreeQueue_8Threads() throws InterruptedException {
        exercise(lockFreeQueue);
    }

    @Benchmark
    @Threads(16)
    public void lockFreeQueue_16Threads() throws InterruptedException {
        exercise(lockFreeQueue);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(QueueBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
