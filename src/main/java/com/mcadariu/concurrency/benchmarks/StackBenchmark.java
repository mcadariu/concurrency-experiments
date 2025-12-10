package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.stacks.EliminationBackoffStack;
import com.mcadariu.concurrency.stacks.LockFreeStack;
import com.mcadariu.concurrency.stacks.Stack;
import com.mcadariu.concurrency.stacks.SynchronizedStack;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/*
Benchmark                                    Mode  Cnt   Score   Error   Units
StackBenchmark.eliminationStack_16Threads   thrpt    5   0.803 ± 0.023  ops/us
StackBenchmark.eliminationStack_1Thread     thrpt    5   0.029 ± 0.001  ops/us
StackBenchmark.eliminationStack_4Threads    thrpt    5   0.129 ± 0.002  ops/us
StackBenchmark.eliminationStack_8Threads    thrpt    5   0.560 ± 0.168  ops/us
StackBenchmark.lockFreeStack_16Threads      thrpt    5   3.473 ± 1.649  ops/us
StackBenchmark.lockFreeStack_1Thread        thrpt    5  96.195 ± 2.436  ops/us
StackBenchmark.lockFreeStack_4Threads       thrpt    5   8.323 ± 2.654  ops/us
StackBenchmark.lockFreeStack_8Threads       thrpt    5   3.559 ± 1.086  ops/us
StackBenchmark.synchronizedStack_16Threads  thrpt    5  17.478 ± 1.577  ops/us
StackBenchmark.synchronizedStack_1Thread    thrpt    5  27.198 ± 0.340  ops/us
StackBenchmark.synchronizedStack_4Threads   thrpt    5  17.277 ± 1.999  ops/us
StackBenchmark.synchronizedStack_8Threads   thrpt    5  17.514 ± 1.393  ops/us
 */

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class StackBenchmark {

    private Stack<Integer> lockFreeStack;
    private Stack<Integer> eliminationStack;
    private Stack<Integer> synchronizedStack;

    private AtomicInteger dummy;

    @Setup(Level.Iteration)
    public void setup() {
        lockFreeStack = new LockFreeStack<>();
        eliminationStack = new EliminationBackoffStack<>();
        synchronizedStack = new SynchronizedStack<>();
        dummy = new AtomicInteger(0);
    }

    private void exercise(Stack<Integer> stack) throws InterruptedException {
        if (ThreadLocalRandom.current().nextBoolean()) {
            stack.push(1);
        } else {
            Integer v = stack.pop();
            if (v != null) dummy.addAndGet(v);
        }
    }

    @Benchmark
    @Threads(1)
    public void lockFreeStack_1Thread() throws InterruptedException {
        exercise(lockFreeStack);
    }

    @Benchmark
    @Threads(4)
    public void lockFreeStack_4Threads() throws InterruptedException {
        exercise(lockFreeStack);
    }

    @Benchmark
    @Threads(8)
    public void lockFreeStack_8Threads() throws InterruptedException {
        exercise(lockFreeStack);
    }

    @Benchmark
    @Threads(16)
    public void lockFreeStack_16Threads() throws InterruptedException {
        exercise(lockFreeStack);
    }

    @Benchmark
    @Threads(1)
    public void eliminationStack_1Thread() throws InterruptedException {
        exercise(eliminationStack);
    }

    @Benchmark
    @Threads(4)
    public void eliminationStack_4Threads() throws InterruptedException {
        exercise(eliminationStack);
    }

    @Benchmark
    @Threads(8)
    public void eliminationStack_8Threads() throws InterruptedException {
        exercise(eliminationStack);
    }

    @Benchmark
    @Threads(16)
    public void eliminationStack_16Threads() throws InterruptedException {
        exercise(eliminationStack);
    }

    @Benchmark
    @Threads(1)
    public void synchronizedStack_1Thread() throws InterruptedException {
        exercise(synchronizedStack);
    }

    @Benchmark
    @Threads(4)
    public void synchronizedStack_4Threads() throws InterruptedException {
        exercise(synchronizedStack);
    }

    @Benchmark
    @Threads(8)
    public void synchronizedStack_8Threads() throws InterruptedException {
        exercise(synchronizedStack);
    }

    @Benchmark
    @Threads(16)
    public void synchronizedStack_16Threads() throws InterruptedException {
        exercise(synchronizedStack);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StackBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}