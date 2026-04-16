package dp;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulingTest {

    // (a) Un solo trabajo
    @Test
    void testSingleJob() {
        List<WeightedScheduling.Job> jobs = List.of(
            new WeightedScheduling.Job(1, 3, 10)
        );
        assertEquals(10, WeightedScheduling.solve(jobs));

        List<WeightedScheduling.Job> sel = WeightedScheduling.reconstructJobs(jobs);
        assertEquals(1, sel.size());
        assertEquals(10, sel.get(0).value());
    }

    // (b) Todos los trabajos son compatibles entre sí
    @Test
    void testAllCompatible() {
        List<WeightedScheduling.Job> jobs = List.of(
            new WeightedScheduling.Job(0, 1, 5),
            new WeightedScheduling.Job(1, 2, 7),
            new WeightedScheduling.Job(2, 3, 3)
        );
        assertEquals(15, WeightedScheduling.solve(jobs));

        List<WeightedScheduling.Job> sel = WeightedScheduling.reconstructJobs(jobs);
        assertEquals(3, sel.size());
        int sum = sel.stream().mapToInt(WeightedScheduling.Job::value).sum();
        assertEquals(15, sum);
    }

    // (c) Greedy EDF sería subóptimo: un trabajo de alto valor solapa varios de bajo valor
    @Test
    void testGreedySuboptimal() {
        // Trabajo A: [0,6] valor=10 — solapa con B, C, D
        // Trabajos B,C,D: [0,2],[2,4],[4,6] valor=4 cada uno → total=12
        // EDF tomaría A (termina primero en empate o simplemente el único largo)
        // Óptimo: B+C+D = 12 > A = 10
        List<WeightedScheduling.Job> jobs = List.of(
            new WeightedScheduling.Job(0, 6, 10),
            new WeightedScheduling.Job(0, 2, 4),
            new WeightedScheduling.Job(2, 4, 4),
            new WeightedScheduling.Job(4, 6, 4)
        );
        assertEquals(12, WeightedScheduling.solve(jobs));

        List<WeightedScheduling.Job> sel = WeightedScheduling.reconstructJobs(jobs);
        int sum = sel.stream().mapToInt(WeightedScheduling.Job::value).sum();
        assertEquals(12, sum);
        // No debe incluir el trabajo de valor 10
        assertTrue(sel.stream().noneMatch(j -> j.value() == 10));
    }

    // (d) reconstructJobs suma exactamente el resultado de solve
    @Test
    void testReconstructMatchesSolve() {
        List<WeightedScheduling.Job> jobs = List.of(
            new WeightedScheduling.Job(0, 3, 3),
            new WeightedScheduling.Job(1, 4, 5),
            new WeightedScheduling.Job(3, 6, 4),
            new WeightedScheduling.Job(4, 7, 2),
            new WeightedScheduling.Job(2, 5, 6),
            new WeightedScheduling.Job(5, 8, 7)
        );
        int optimal = WeightedScheduling.solve(jobs);
        List<WeightedScheduling.Job> sel = WeightedScheduling.reconstructJobs(jobs);
        int sum = sel.stream().mapToInt(WeightedScheduling.Job::value).sum();
        assertEquals(optimal, sum);
    }
}