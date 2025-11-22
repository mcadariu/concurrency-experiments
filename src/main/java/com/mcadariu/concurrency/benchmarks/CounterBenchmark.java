package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.primitives.AtomicCounter;
import com.mcadariu.concurrency.primitives.Counter;
import com.mcadariu.concurrency.primitives.ReentrantLockCounter;
import com.mcadariu.concurrency.primitives.SynchronizedCounter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class CounterBenchmark {

    private Counter synchronizedCounter;
    private Counter reentrantLockCounter;
    private Counter atomicCounter;

    @Setup(Level.Iteration)
    public void setup() {
        synchronizedCounter = new SynchronizedCounter();
        reentrantLockCounter = new ReentrantLockCounter();
        atomicCounter = new AtomicCounter();
    }

    // Synchronized counter benchmarks
    @Benchmark
    @Threads(1)
    public void synchronizedCounter_1Thread() {
        synchronizedCounter.increment();
    }

    @Benchmark
    @Threads(4)
    public void synchronizedCounter_4Threads() {
        synchronizedCounter.increment();
    }

    @Benchmark
    @Threads(8)
    public void synchronizedCounter_8Threads() {
        synchronizedCounter.increment();
    }

    @Benchmark
    @Threads(16)
    public void synchronizedCounter_16Threads() {
        synchronizedCounter.increment();
    }

    // ReentrantLock counter benchmarks
    @Benchmark
    @Threads(1)
    public void reentrantLockCounter_1Thread() {
        reentrantLockCounter.increment();
    }

    @Benchmark
    @Threads(4)
    public void reentrantLockCounter_4Threads() {
        reentrantLockCounter.increment();
    }

    @Benchmark
    @Threads(8)
    public void reentrantLockCounter_8Threads() {
        reentrantLockCounter.increment();
    }

    @Benchmark
    @Threads(16)
    public void reentrantLockCounter_16Threads() {
        reentrantLockCounter.increment();
    }

    // Atomic counter benchmarks
    @Benchmark
    @Threads(1)
    public void atomicCounter_1Thread() {
        atomicCounter.increment();
    }

    @Benchmark
    @Threads(4)
    public void atomicCounter_4Threads() {
        atomicCounter.increment();
    }

    @Benchmark
    @Threads(8)
    public void atomicCounter_8Threads() {
        atomicCounter.increment();
    }

    @Benchmark
    @Threads(16)
    public void atomicCounter_16Threads() {
        atomicCounter.increment();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CounterBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
