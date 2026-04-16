package dp;

import java.util.*;

public class WeightedScheduling {

    public record Job(int start, int finish, int value) {}

    public static int solve(List<Job> jobs) {
        jobs = new ArrayList<>(jobs);
        jobs.sort(Comparator.comparingInt(Job::finish));
        int n = jobs.size();
        int[] p = computeP(jobs);
        int[] dp = new int[n+1];
        for (int j = 1; j <= n; j++) {
            int include = jobs.get(j-1).value() + dp[p[j]];
            int exclude = dp[j-1];
            dp[j] = Math.max(include, exclude);
        }
        return dp[n];
    }

    /** Retorna la lista de trabajos seleccionados en la solución óptima */
    public static List<Job> reconstructJobs(List<Job> jobs) {
        jobs = new ArrayList<>(jobs);
        jobs.sort(Comparator.comparingInt(Job::finish));
        int n = jobs.size();
        int[] p = computeP(jobs);
        int[] dp = new int[n+1];
        for (int j = 1; j <= n; j++) {
            int include = jobs.get(j-1).value() + dp[p[j]];
            int exclude = dp[j-1];
            dp[j] = Math.max(include, exclude);
        }
        // Backtracking
        List<Job> selected = new ArrayList<>();
        int j = n;
        while (j >= 1) {
            int include = jobs.get(j-1).value() + dp[p[j]];
            if (include >= dp[j-1]) {
                selected.add(jobs.get(j-1));
                j = p[j];
            } else {
                j--;
            }
        }
        Collections.reverse(selected);
        return selected;
    }

    /** p[j] = último trabajo compatible con j (termina <= start_j) */
    static int[] computeP(List<Job> jobs) {
        int n = jobs.size();
        int[] finishes = jobs.stream().mapToInt(Job::finish).toArray();
        int[] p = new int[n+1];
        for (int j = 1; j <= n; j++) {
            int s = jobs.get(j-1).start();
            int lo = 0, hi = j-1;
            while (lo < hi) {
                int mid = (lo + hi + 1) >>> 1;
                if (finishes[mid-1] <= s) lo = mid; else hi = mid-1;
            }
            p[j] = lo;
        }
        return p;
    }
}