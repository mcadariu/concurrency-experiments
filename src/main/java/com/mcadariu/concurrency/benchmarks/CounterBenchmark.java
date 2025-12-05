package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.counters.AtomicCounter;
import com.mcadariu.concurrency.counters.Counter;
import com.mcadariu.concurrency.counters.ReentrantLockCounter;
import com.mcadariu.concurrency.counters.SynchronizedCounter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/*
Benchmark                                         Mode  Cnt       Score       Error   Units
CounterBenchmark.atomicCounter_16Threads         thrpt    5   17821.221 ±  8601.979  ops/ms
CounterBenchmark.atomicCounter_1Thread           thrpt    5  141171.971 ±  2367.650  ops/ms
CounterBenchmark.atomicCounter_4Threads          thrpt    5   23361.807 ±  2789.589  ops/ms
CounterBenchmark.atomicCounter_8Threads          thrpt    5   19967.556 ±  4397.355  ops/ms
CounterBenchmark.reentrantLockCounter_16Threads  thrpt    5   49935.145 ±  8996.853  ops/ms
CounterBenchmark.reentrantLockCounter_1Thread    thrpt    5   76992.215 ±  2470.244  ops/ms
CounterBenchmark.reentrantLockCounter_4Threads   thrpt    5   56097.195 ± 11518.139  ops/ms
CounterBenchmark.reentrantLockCounter_8Threads   thrpt    5   53249.415 ±  7463.541  ops/ms
CounterBenchmark.synchronizedCounter_16Threads   thrpt    5   10257.416 ± 41383.995  ops/ms
CounterBenchmark.synchronizedCounter_1Thread     thrpt    5  111651.941 ±  2284.433  ops/ms
CounterBenchmark.synchronizedCounter_4Threads    thrpt    5    4856.839 ±  3401.972  ops/ms
CounterBenchmark.synchronizedCounter_8Threads    thrpt    5   16163.536 ± 54189.480  ops/ms
 */

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
