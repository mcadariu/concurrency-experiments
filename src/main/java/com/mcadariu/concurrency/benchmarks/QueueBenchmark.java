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
import java.util.concurrent.atomic.AtomicInteger;

/*
QueueBenchmark.boundedQueue_16Threads            thrpt    5  22.825 ± 11.926  ops/us
QueueBenchmark.boundedQueue_1Thread              thrpt    5  45.591 ±  3.994  ops/us
QueueBenchmark.boundedQueue_4Threads             thrpt    5  29.819 ±  4.303  ops/us
QueueBenchmark.boundedQueue_8Threads             thrpt    5  22.017 ± 11.182  ops/us
QueueBenchmark.lockFreeQueue_16Threads           thrpt    5   2.385 ±  0.669  ops/us
QueueBenchmark.lockFreeQueue_1Thread             thrpt    5  50.607 ±  1.823  ops/us
QueueBenchmark.lockFreeQueue_4Threads            thrpt    5   6.187 ±  2.902  ops/us
QueueBenchmark.lockFreeQueue_8Threads            thrpt    5   3.431 ±  1.760  ops/us
QueueBenchmark.unboundedQueue_16Threads          thrpt    5  18.737 ±  6.566  ops/us
QueueBenchmark.unboundedQueue_1Thread            thrpt    5  28.534 ±  4.936  ops/us
QueueBenchmark.unboundedQueue_4Threads           thrpt    5  22.377 ±  1.829  ops/us
QueueBenchmark.unboundedQueue_8Threads           thrpt    5  21.602 ±  3.370  ops/us
 */

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
    private Queue<Integer> optimizedLockFreeQueue;

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
            optimizedLockFreeQueue.enq(1);
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
