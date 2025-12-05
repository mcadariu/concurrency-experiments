package com.mcadariu.concurrency.benchmarks;

import com.mcadariu.concurrency.locks.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/*
Benchmark                              Mode  Cnt    Score    Error   Units
LockBenchmarks.backoffLock_16Threads        thrpt    5  111.154 ±  6.249  ops/us
LockBenchmarks.backoffLock_1Thread          thrpt    5  118.698 ±  2.970  ops/us
LockBenchmarks.backoffLock_4Threads         thrpt    5  115.393 ±  4.074  ops/us
LockBenchmarks.backoffLock_8Threads         thrpt    5  116.485 ±  3.370  ops/us
LockBenchmarks.casLock_16Threads            thrpt    5    1.557 ±  0.622  ops/us
LockBenchmarks.casLock_1Thread              thrpt    5  118.568 ±  2.069  ops/us
LockBenchmarks.casLock_4Threads             thrpt    5    6.529 ±  3.958  ops/us
LockBenchmarks.casLock_8Threads             thrpt    5    2.389 ±  0.512  ops/us
LockBenchmarks.clhLock_16Threads            thrpt    5   ≈ 10⁻³           ops/us
LockBenchmarks.clhLock_1Thread              thrpt    5  136.537 ±  5.921  ops/us
LockBenchmarks.clhLock_4Threads             thrpt    5    5.456 ±  2.152  ops/us
LockBenchmarks.clhLock_8Threads             thrpt    5    3.170 ±  2.942  ops/us
LockBenchmarks.javaReentrantLock_16Threads  thrpt    5   53.089 ±  2.918  ops/us
LockBenchmarks.javaReentrantLock_1Thread    thrpt    5   75.061 ±  7.957  ops/us
LockBenchmarks.javaReentrantLock_4Threads   thrpt    5   54.705 ±  0.835  ops/us
LockBenchmarks.javaReentrantLock_8Threads   thrpt    5   52.982 ±  6.509  ops/us
LockBenchmarks.mcsLock_16Threads            thrpt    5    0.001 ±  0.001  ops/us
LockBenchmarks.mcsLock_1Thread              thrpt    5   77.894 ±  3.604  ops/us
LockBenchmarks.mcsLock_4Threads             thrpt    5    5.363 ±  1.017  ops/us
LockBenchmarks.mcsLock_8Threads             thrpt    5    2.835 ±  0.793  ops/us
LockBenchmarks.ttasLock_16Threads           thrpt    5    1.576 ±  1.081  ops/us
LockBenchmarks.ttasLock_1Thread             thrpt    5  119.168 ±  4.192  ops/us
LockBenchmarks.ttasLock_4Threads            thrpt    5    3.551 ±  0.886  ops/us
LockBenchmarks.ttasLock_8Threads            thrpt    5    1.889 ±  0.940  ops/us
 */

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class LockBenchmarks {

    private Lock casLock;
    private Lock ttasLock;
    private Lock backoffLock;
    private Lock clhLock;
    private Lock mcsLock;
    private Lock javaReentrantLock;

    private int counter;

    @Setup(Level.Iteration)
    public void setup() {
        casLock = new CASLock();
        ttasLock = new TTASLock();
        backoffLock = new BackoffLock();
        clhLock = new CLHLock();
        mcsLock = new MCSLock();
        javaReentrantLock = new JavaReentrantLock();
        counter = 0;
    }

    @Benchmark
    @Threads(1)
    public void casLock_1Thread() throws InterruptedException {
        casLock.lock();
        try {
            counter++;
        } finally {
            casLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void casLock_4Threads() throws InterruptedException {
        casLock.lock();
        try {
            counter++;
        } finally {
            casLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void casLock_8Threads() throws InterruptedException {
        casLock.lock();
        try {
            counter++;
        } finally {
            casLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void casLock_16Threads() throws InterruptedException {
        casLock.lock();
        try {
            counter++;
        } finally {
            casLock.unlock();
        }
    }

    @Benchmark
    @Threads(1)
    public void ttasLock_1Thread() throws InterruptedException {
        ttasLock.lock();
        try {
            counter++;
        } finally {
            ttasLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void ttasLock_4Threads() throws InterruptedException {
        ttasLock.lock();
        try {
            counter++;
        } finally {
            ttasLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void ttasLock_8Threads() throws InterruptedException {
        ttasLock.lock();
        try {
            counter++;
        } finally {
            ttasLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void ttasLock_16Threads() throws InterruptedException {
        ttasLock.lock();
        try {
            counter++;
        } finally {
            ttasLock.unlock();
        }
    }

    @Benchmark
    @Threads(1)
    public void backoffLock_1Thread() throws InterruptedException {
        backoffLock.lock();
        try {
            counter++;
        } finally {
            backoffLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void backoffLock_4Threads() throws InterruptedException {
        backoffLock.lock();
        try {
            counter++;
        } finally {
            backoffLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void backoffLock_8Threads() throws InterruptedException {
        backoffLock.lock();
        try {
            counter++;
        } finally {
            backoffLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void backoffLock_16Threads() throws InterruptedException {
        backoffLock.lock();
        try {
            counter++;
        } finally {
            backoffLock.unlock();
        }
    }

    @Benchmark
    @Threads(1)
    public void clhLock_1Thread() throws InterruptedException {
        clhLock.lock();
        try {
            counter++;
        } finally {
            clhLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void clhLock_4Threads() throws InterruptedException {
        clhLock.lock();
        try {
            counter++;
        } finally {
            clhLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void clhLock_8Threads() throws InterruptedException {
        clhLock.lock();
        try {
            counter++;
        } finally {
            clhLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void clhLock_16Threads() throws InterruptedException {
        clhLock.lock();
        try {
            counter++;
        } finally {
            clhLock.unlock();
        }
    }

    @Benchmark
    @Threads(1)
    public void mcsLock_1Thread() throws InterruptedException {
        mcsLock.lock();
        try {
            counter++;
        } finally {
            mcsLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void mcsLock_4Threads() throws InterruptedException {
        mcsLock.lock();
        try {
            counter++;
        } finally {
            mcsLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void mcsLock_8Threads() throws InterruptedException {
        mcsLock.lock();
        try {
            counter++;
        } finally {
            mcsLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void mcsLock_16Threads() throws InterruptedException {
        mcsLock.lock();
        try {
            counter++;
        } finally {
            mcsLock.unlock();
        }
    }

    @Benchmark
    @Threads(1)
    public void javaReentrantLock_1Thread() throws InterruptedException {
        javaReentrantLock.lock();
        try {
            counter++;
        } finally {
            javaReentrantLock.unlock();
        }
    }

    @Benchmark
    @Threads(4)
    public void javaReentrantLock_4Threads() throws InterruptedException {
        javaReentrantLock.lock();
        try {
            counter++;
        } finally {
            javaReentrantLock.unlock();
        }
    }

    @Benchmark
    @Threads(8)
    public void javaReentrantLock_8Threads() throws InterruptedException {
        javaReentrantLock.lock();
        try {
            counter++;
        } finally {
            javaReentrantLock.unlock();
        }
    }

    @Benchmark
    @Threads(16)
    public void javaReentrantLock_16Threads() throws InterruptedException {
        javaReentrantLock.lock();
        try {
            counter++;
        } finally {
            javaReentrantLock.unlock();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LockBenchmarks.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
