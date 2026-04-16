package dp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KnapsackTest {

    // (a) W = 0 → siempre 0
    @Test
    void testCapacityZero() {
        int[] w = {2, 3, 4};
        int[] v = {3, 4, 5};
        assertEquals(0, Knapsack.solve01(w, v, 0));
        assertEquals(0, Knapsack.solveUnbounded(w, v, 0));
        assertEquals(0, Knapsack.solveMemOpt(w, v, 0));
    }

    // (b) Ningún objeto cabe
    @Test
    void testNoItemFits() {
        int[] w = {10, 20};
        int[] v = {5, 10};
        assertEquals(0, Knapsack.solve01(w, v, 5));
        assertEquals(0, Knapsack.solveUnbounded(w, v, 5));
        assertEquals(0, Knapsack.solveMemOpt(w, v, 5));
    }

    // (c) Todos los objetos caben
    @Test
    void testAllItemsFit() {
        int[] w = {1, 2, 3};
        int[] v = {6, 10, 12};
        // suma pesos = 6, W = 10 → todos caben, valor total = 28
        assertEquals(28, Knapsack.solve01(w, v, 10));
        assertEquals(28, Knapsack.solveMemOpt(w, v, 10));
    }

    // (d) Solución óptima NO incluye el objeto de mayor valor
    @Test
    void testOptimalExcludesHighestValue() {
        // Objeto 0: w=5, v=10 | Objeto 1: w=3, v=7 | Objeto 2: w=3, v=7
        // W=6 → no cabe obj0 (w=5) junto a nada útil; mejor: obj1+obj2 = 14
        int[] w = {5, 3, 3};
        int[] v = {10, 7, 7};
        assertEquals(14, Knapsack.solve01(w, v, 6));
        assertEquals(14, Knapsack.solveMemOpt(w, v, 6));
    }

    // (e) solveMemOpt == solve01 para los mismos inputs
    @Test
    void testMemOptMatchesSolve01() {
        int[] w = {2, 3, 4, 5};
        int[] v = {3, 4, 5, 6};
        for (int W = 0; W <= 10; W++) {
            assertEquals(Knapsack.solve01(w, v, W), Knapsack.solveMemOpt(w, v, W),
                "Mismatch at W=" + W);
        }
    }

    // Caso extra: ejemplo clásico
    @Test
    void testClassicExample() {
        int[] w = {1, 3, 4, 5};
        int[] v = {1, 4, 5, 7};
        assertEquals(9, Knapsack.solve01(w, v, 7));
        assertEquals(9, Knapsack.solveMemOpt(w, v, 7));
    }

    // Caso extra: unbounded permite reusar ítems
    @Test
    void testUnboundedReuse() {
        // Objeto: w=2, v=3. W=6 → puede usarse 3 veces → valor=9
        int[] w = {2};
        int[] v = {3};
        assertEquals(9, Knapsack.solveUnbounded(w, v, 6));
        // 0/1 solo puede usarlo una vez → valor=3
        assertEquals(3, Knapsack.solve01(w, v, 6));
    }
}