package dp;

public class Knapsack {
    /**
     * Knapsack 0/1 — O(n*W) tiempo, O(n*W) espacio.
     * @return valor máximo seleccionando de los n objetos con capacidad W
     */
    public static int solve01(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++) {
            int wi = weights[i-1], vi = values[i-1];
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (wi <= w)
                    dp[i][w] = Math.max(dp[i][w], vi + dp[i-1][w-wi]);
            }
        }
        return dp[n][W];
    }

    /** Reconstrucción O(n+W): determina qué objetos fueron seleccionados */
    public static boolean[] reconstruct(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++) {
            int wi = weights[i-1], vi = values[i-1];
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (wi <= w)
                    dp[i][w] = Math.max(dp[i][w], vi + dp[i-1][w-wi]);
            }
        }
        boolean[] sel = new boolean[n];
        int w = W;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i-1][w]) {
                sel[i-1] = true;
                w -= weights[i-1];
            }
        }
        return sel;
    }

    /**
     * Unbounded Knapsack — O(n*W) tiempo, O(W) espacio.
     * Cada objeto puede usarse ilimitadas veces.
     * @return valor máximo con capacidad W
     */
    public static int solveUnbounded(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W+1];
        for (int w = 1; w <= W; w++) {
            for (int i = 0; i < n; i++) {
                if (weights[i] <= w)
                    dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
            }
        }
        return dp[W];
    }

    /**
     * Knapsack 0/1 optimizado en espacio — O(n*W) tiempo, O(W) espacio.
     * Usa un único arreglo 1D iterando de derecha a izquierda.
     * @return valor máximo seleccionando de los n objetos con capacidad W
     */
    public static int solveMemOpt(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W+1];
        for (int i = 0; i < n; i++) {
            int wi = weights[i], vi = values[i];
            for (int w = W; w >= wi; w--) {
                dp[w] = Math.max(dp[w], vi + dp[w - wi]);
            }
        }
        return dp[W];
    }
}