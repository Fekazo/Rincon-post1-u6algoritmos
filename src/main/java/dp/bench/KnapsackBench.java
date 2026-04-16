package dp.bench;

import dp.Knapsack;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class KnapsackBench {

    @Param({"100", "500", "1000"}) public int n;
    @Param({"1000", "5000", "10000"}) public int W;

    private int[] weights, values;

    @Setup
    public void setup() {
        Random rng = new Random(42);
        weights = new int[n];
        values  = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = rng.nextInt(W / 4) + 1;
            values[i]  = rng.nextInt(100) + 1;
        }
    }

    @Benchmark
    public int bench01() {
        return Knapsack.solve01(weights, values, W);
    }

    @Benchmark
    public int benchMemOpt() {
        return Knapsack.solveMemOpt(weights, values, W);
    }
}