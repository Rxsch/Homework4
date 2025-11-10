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

    private Map<String, Integer> memoMap; // for memoization

    // Constructor: reads job data from file
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

    // ============================
    // 1. Pure recursive solution
    // ============================
    public int maxCostRecursive(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        return maxCostRecursiveHelper(jobs.size() - 1, maxBattery, maxPayload);
    }

    private int maxCostRecursiveHelper(int i, double remainBattery, int remainPayload) {
        if (i < 0) return 0;

        Job job = jobs.get(i);
        double batteryNeeded = job.batteryCost();

        // Option 1: skip
        int exclude = maxCostRecursiveHelper(i - 1, remainBattery, remainPayload);

        // Option 2: take
        int include = 0;
        if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
            include = job.value + maxCostRecursiveHelper(i - 1, remainBattery - batteryNeeded, remainPayload - job.weight);
        }

        return Math.max(include, exclude);
    }

    // ============================
    // 2. Memoization solution
    // ============================
    public int maxCostMemo(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        memoMap = new HashMap<>();
        return maxCostMemoHelper(jobs.size() - 1, maxBattery, maxPayload);
    }

    private int maxCostMemoHelper(int i, double remainBattery, int remainPayload) {
        if (i < 0) return 0;

        String key = i + "|" + (int)(remainBattery * 10) + "|" + remainPayload;
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

    // ============================
    // 3. Tabulation (bottom-up)
    // ============================
    public int maxCostTab(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        int n = jobs.size();
        int batteryLimit = (int) Math.ceil(maxBattery * 10); // discretize battery
        int[][] dp = new int[n + 1][batteryLimit + 1];

        // Build table
        for (int i = 1; i <= n; i++) {
            Job job = jobs.get(i - 1);
            int weight = job.weight;
            int batteryCost = (int) Math.ceil(job.batteryCost() * 10);

            for (int b = 0; b <= batteryLimit; b++) {
                dp[i][b] = dp[i - 1][b];
                if (weight <= maxPayload && batteryCost <= b) {
                    dp[i][b] = Math.max(dp[i][b], job.value + dp[i - 1][b - batteryCost]);
                }
            }
        }

        // Reconstruct selected jobs
        int b = batteryLimit;
        for (int i = n; i > 0; i--) {
            Job job = jobs.get(i - 1);
            int batteryCost = (int) Math.ceil(job.batteryCost() * 10);
            if (b >= batteryCost && dp[i][b] != dp[i - 1][b]) {
                selectedJobs.add(job);
                b -= batteryCost;
                maxPayload -= job.weight;
            }
        }

        Collections.reverse(selectedJobs);
        return dp[n][batteryLimit];
    }

    // ============================
    // Display selected jobs
    // ============================
    public void displaySelectedJobs() {
        for (Job j : selectedJobs) {
            System.out.println(j.id);
        }
        System.out.println();
    }
}
