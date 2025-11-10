/* Daniel Rangosch
   Dr. Steinberg
   COP3503 Fall 2025
   Programming Assignment 4
*/

import java.io.*;
import java.util.*;

public class DroneDelivery {

    class Job {
        String id;
        int weight;
        int time;
        int cost;
        Job(String id, int weight, int time, int cost) {
            this.id = id;
            this.weight = weight;
            this.time = time;
            this.cost = cost;
        }
    }

    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Job> selectedJobs = new ArrayList<>();

    private Integer[][] memo;     // for memoization DP
    private int[][] dp;          // for tabulation DP

    // Constructor: reads job data from file
    public DroneDelivery(String filename) throws Exception {
        Scanner scan = new Scanner(new File(filename));
        while (scan.hasNext()) {
            String id = scan.next();
            int weight = scan.nextInt();
            int time = scan.nextInt();
            int cost = scan.nextInt();
            jobs.add(new Job(id, weight, time, cost));
        }
        scan.close();
    }

    // ====================================================================
    // ✅ 1. PURE RECURSIVE SOLUTION (NO DP)
    // ====================================================================
    public int maxCostRecursive(double maxWeight, int maxTime) {
        selectedJobs.clear();
        return maxCostRecursiveHelper(jobs.size() - 1, maxWeight, maxTime);
    }

    private int maxCostRecursiveHelper(int i, double remainWeight, int remainTime) {
        if (i < 0 || remainWeight <= 0 || remainTime <= 0) return 0;

        Job job = jobs.get(i);

        // Option 1: skip job
        int exclude = maxCostRecursiveHelper(i - 1, remainWeight, remainTime);

        // Option 2: take job (if possible)
        int include = 0;
        if (job.weight <= remainWeight && job.time <= remainTime) {
            include = job.cost + maxCostRecursiveHelper(i - 1, remainWeight - job.weight, remainTime - job.time);
        }

        return Math.max(include, exclude);
    }

    // ====================================================================
    // ✅ 2. MEMOIZATION DP SOLUTION
    // ====================================================================
    public int maxCostMemo(double maxWeight, int maxTime) {
        selectedJobs.clear();
        memo = new Integer[jobs.size()][maxTime + 1];
        return maxCostMemoHelper(jobs.size() - 1, maxWeight, maxTime);
    }

    private int maxCostMemoHelper(int i, double remainWeight, int remainTime) {
        if (i < 0 || remainWeight <= 0 || remainTime <= 0) return 0;

        if (memo[i][remainTime] != null && remainWeight % 1 == 0) {
            return memo[i][remainTime];
        }

        Job job = jobs.get(i);

        int exclude = maxCostMemoHelper(i - 1, remainWeight, remainTime);

        int include = 0;
        if (job.weight <= remainWeight && job.time <= remainTime) {
            include = job.cost + maxCostMemoHelper(i - 1, remainWeight - job.weight, remainTime - job.time);
        }

        int result = Math.max(include, exclude);

        if (remainWeight % 1 == 0)
            memo[i][remainTime] = result;

        return result;
    }

    // ====================================================================
    // ✅ 3. TABULATION DP (BOTTOM-UP) — also stores selected jobs
    // ====================================================================
    public int maxCostTab(double maxWeight, int maxTime) {
        selectedJobs.clear();
        int W = (int) maxWeight;
        dp = new int[jobs.size() + 1][maxTime + 1];

        for (int i = 1; i <= jobs.size(); i++) {
            Job job = jobs.get(i - 1);
            for (int t = 0; t <= maxTime; t++) {
                dp[i][t] = dp[i - 1][t];
                if (job.weight <= W && job.time <= t) {
                    dp[i][t] = Math.max(dp[i][t], job.cost + dp[i - 1][t - job.time]);
                }
            }
        }

        reconstructSelection(W, maxTime);
        return dp[jobs.size()][maxTime];
    }

    // backtrack to find the selected jobs
    private void reconstructSelection(int maxWeight, int maxTime) {
        int t = maxTime;
        int w = maxWeight;

        for (int i = jobs.size(); i > 0; i--) {
            if (dp[i][t] != dp[i - 1][t]) {
                Job job = jobs.get(i - 1);
                selectedJobs.add(job);
                t -= job.time;
                w -= job.weight;
            }
        }
    }

    // ====================================================================
    // ✅ DISPLAY SELECTED JOBS (used by driver)
    // ====================================================================
    public void displaySelectedJobs() {
        Collections.reverse(selectedJobs);
        for (Job j : selectedJobs) {
            System.out.println(j.id + " Weight:" + j.weight + " Time:" + j.time + " Cost:" + j.cost);
        }
        System.out.println();
    }

}
