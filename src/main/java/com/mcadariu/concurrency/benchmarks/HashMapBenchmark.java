package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.hashmaps.CoarseGrainedHashMap;
import com.mcadariu.concurrency.hashmaps.ConcurrentHashMap;
import com.mcadariu.concurrency.hashmaps.LockFreeHashMap;
import com.mcadariu.concurrency.hashmaps.StripedHashMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/*
HashMapBenchmark.coarseGrainedMap_16Threads  thrpt    5  10.260 ±  0.519  ops/us
HashMapBenchmark.coarseGrainedMap_1Thread    thrpt    5  17.203 ±  1.452  ops/us
HashMapBenchmark.coarseGrainedMap_4Threads   thrpt    5  11.405 ±  0.181  ops/us
HashMapBenchmark.coarseGrainedMap_8Threads   thrpt    5  10.321 ±  2.101  ops/us
HashMapBenchmark.lockFreeMap_16Threads       thrpt    5  45.861 ± 19.027  ops/us
HashMapBenchmark.lockFreeMap_1Thread         thrpt    5  29.716 ±  7.842  ops/us
HashMapBenchmark.lockFreeMap_4Threads        thrpt    5  45.400 ± 30.424  ops/us
HashMapBenchmark.lockFreeMap_8Threads        thrpt    5  54.254 ± 24.810  ops/us
HashMapBenchmark.stripedMap_16Threads        thrpt    5  13.687 ±  0.758  ops/us
HashMapBenchmark.stripedMap_1Thread          thrpt    5  18.654 ±  0.600  ops/us
HashMapBenchmark.stripedMap_4Threads         thrpt    5  27.144 ±  3.910  ops/us
HashMapBenchmark.stripedMap_8Threads         thrpt    5  15.682 ±  2.929  ops/us
 */

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class HashMapBenchmark {

    private ConcurrentHashMap<Integer, Integer> coarseGrainedMap;
    private ConcurrentHashMap<Integer, Integer> stripedMap;
    private ConcurrentHashMap<Integer, Integer> lockFreeMap;

    private AtomicInteger dummy;
    private static final int CAPACITY = 1024;
    private static final int KEY_RANGE = 10000;

    @Setup(Level.Iteration)
    public void setup() {
        coarseGrainedMap = new CoarseGrainedHashMap<>(CAPACITY);
        stripedMap = new StripedHashMap<>(CAPACITY, 16);
        lockFreeMap = new LockFreeHashMap<>(CAPACITY);
        dummy = new AtomicInteger(0);

        for (int i = 0; i < KEY_RANGE / 2; i++) {
            int key = ThreadLocalRandom.current().nextInt(KEY_RANGE);
            coarseGrainedMap.add(key, key);
            stripedMap.add(key, key);
            lockFreeMap.add(key, key);
        }
    }

    private void exercise(ConcurrentHashMap<Integer, Integer> map) {
        int key = ThreadLocalRandom.current().nextInt(KEY_RANGE);
        int op = ThreadLocalRandom.current().nextInt(100);

        if (op < 10) {
            map.remove(key);
        } else if (op < 30) {
            map.add(key, key);
        } else {
            Integer v = map.get(key);
            if (v != null) dummy.addAndGet(v);
        }
    }

    @Benchmark
    @Threads(1)
    public void coarseGrainedMap_1Thread() {
        exercise(coarseGrainedMap);
    }

    @Benchmark
    @Threads(4)
    public void coarseGrainedMap_4Threads() {
        exercise(coarseGrainedMap);
    }

    @Benchmark
    @Threads(8)
    public void coarseGrainedMap_8Threads() {
        exercise(coarseGrainedMap);
    }

    @Benchmark
    @Threads(16)
    public void coarseGrainedMap_16Threads() {
        exercise(coarseGrainedMap);
    }

    @Benchmark
    @Threads(1)
    public void stripedMap_1Thread() {
        exercise(stripedMap);
    }

    @Benchmark
    @Threads(4)
    public void stripedMap_4Threads() {
        exercise(stripedMap);
    }

    @Benchmark
    @Threads(8)
    public void stripedMap_8Threads() {
        exercise(stripedMap);
    }

    @Benchmark
    @Threads(16)
    public void stripedMap_16Threads() {
        exercise(stripedMap);
    }

    @Benchmark
    @Threads(1)
    public void lockFreeMap_1Thread() {
        exercise(lockFreeMap);
    }

    @Benchmark
    @Threads(4)
    public void lockFreeMap_4Threads() {
        exercise(lockFreeMap);
    }

    @Benchmark
    @Threads(8)
    public void lockFreeMap_8Threads() {
        exercise(lockFreeMap);
    }

    @Benchmark
    @Threads(16)
    public void lockFreeMap_16Threads() {
        exercise(lockFreeMap);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HashMapBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
