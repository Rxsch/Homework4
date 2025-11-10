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
        int distance;
        int value;

        Job(String id, int weight, int distance, int value) {
            this.id = id;
            this.weight = weight;
            this.distance = distance;
            this.value = value;
        }

        double batteryCost() {
            return distance * (1.0 + 0.1 * weight);
        }
    }

    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Job> selectedJobs = new ArrayList<>();
    private Map<String, Integer> memoMap;

    public DroneDelivery(String filename) throws Exception {
        Scanner scan = new Scanner(new File(filename));
        while (scan.hasNext()) {
            String id = scan.next();
            int weight = scan.nextInt();
            int distance = scan.nextInt();
            int value = scan.nextInt();
            jobs.add(new Job(id, weight, distance, value));
        }
        scan.close();
    }

    // ==============================
    // 1. Recursive
    // ==============================
    public int maxCostRecursive(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        return maxCostRecursiveHelper(jobs.size() - 1, maxBattery, maxPayload);
    }

    private int maxCostRecursiveHelper(int i, double remainBattery, int remainPayload) {
        if (i < 0) return 0;
        Job job = jobs.get(i);
        double batteryNeeded = job.batteryCost();

        int exclude = maxCostRecursiveHelper(i - 1, remainBattery, remainPayload);

        int include = 0;
        if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
            include = job.value + maxCostRecursiveHelper(i - 1, remainBattery - batteryNeeded, remainPayload - job.weight);
        }

        return Math.max(include, exclude);
    }

    // ==============================
    // 2. Memoization
    // ==============================
    public int maxCostMemo(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        memoMap = new HashMap<>();
        return maxCostMemoHelper(jobs.size() - 1, maxBattery, maxPayload);
    }

    private int maxCostMemoHelper(int i, double remainBattery, int remainPayload) {
        if (i < 0) return 0;

        String key = i + "|" + (int)(remainBattery*10) + "|" + remainPayload;
        if (memoMap.containsKey(key)) return memoMap.get(key);

        Job job = jobs.get(i);
        double batteryNeeded = job.batteryCost();

        int exclude = maxCostMemoHelper(i - 1, remainBattery, remainPayload);
        int include = 0;
        if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
            include = job.value + maxCostMemoHelper(i - 1, remainBattery - batteryNeeded, remainPayload - job.weight);
        }

        int result = Math.max(include, exclude);
        memoMap.put(key, result);
        return result;
    }

    // ==============================
    // 3. Tabulation DP
    // ==============================
    public int maxCostTab(double maxBattery, int maxPayload) {
        int n = jobs.size();
        int batteryLimit = (int)Math.ceil(maxBattery * 10);

        int[][][] dp = new int[n + 1][batteryLimit + 1][maxPayload + 1];

        for (int i = 1; i <= n; i++) {
            Job job = jobs.get(i - 1);
            int b = (int)Math.ceil(job.batteryCost() * 10);
            int w = job.weight;
            for (int j = 0; j <= batteryLimit; j++) {
                for (int k = 0; k <= maxPayload; k++) {
                    dp[i][j][k] = dp[i-1][j][k];
                    if (b <= j && w <= k) {
                        dp[i][j][k] = Math.max(dp[i][j][k], job.value + dp[i-1][j-b][k-w]);
                    }
                }
            }
        }

        // Reconstruct selected jobs
        selectedJobs.clear();
        int j = batteryLimit;
        int k = maxPayload;
        for (int i = n; i > 0; i--) {
            Job job = jobs.get(i-1);
            int b = (int)Math.ceil(job.batteryCost() * 10);
            int w = job.weight;
            if (j >= b && k >= w && dp[i][j][k] == job.value + dp[i-1][j-b][k-w]) {
                selectedJobs.add(job);
                j -= b;
                k -= w;
            }
        }
        Collections.reverse(selectedJobs);

        return dp[n][batteryLimit][maxPayload];
    }

    public void displaySelectedJobs() {
        for (Job j : selectedJobs) {
            System.out.println(j.id);
        }
        System.out.println();
    }
}
