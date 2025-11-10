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

        // Calcula el consumo de batería de este trabajo
        double batteryCost() {
            return distance * (1.0 + 0.1 * weight);
        }
    }

    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Job> selectedJobs = new ArrayList<>();

    private Map<String, Integer> memoMap; // memoización para maxCostMemo

     public DroneDelivery(String filename) throws Exception {
    Scanner scan = new Scanner(new File(filename));
    while (scan.hasNext()) {
        String id = scan.next();
        int distance = scan.nextInt();  // read distance first
        int weight = scan.nextInt();    // then weight
        int value = scan.nextInt();     // then value
        jobs.add(new Job(id, weight, distance, value)); // Job constructor
    }
    scan.close();
}

    // ==============================
    // 1. RECURSIVO PURO
    // ==============================
    public int maxCostRecursive(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        return maxCostRecursiveHelper(jobs.size() - 1, maxBattery, maxPayload);
    }

    private int maxCostRecursiveHelper(int i, double remainBattery, int remainPayload) {
        if (i < 0) return 0;
        Job job = jobs.get(i);
        double batteryNeeded = job.batteryCost();

        // Opción 1: excluir el trabajo
        int exclude = maxCostRecursiveHelper(i - 1, remainBattery, remainPayload);

        // Opción 2: incluir el trabajo si cabe
        int include = 0;
        if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
            include = job.value + maxCostRecursiveHelper(i - 1, remainBattery - batteryNeeded, remainPayload - job.weight);
        }

        return Math.max(include, exclude);
    }

    // ==============================
    // 2. MEMOIZATION DP
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
    // 3. TABULATION DP (BOTTOM-UP)
    // ==============================
    public int maxCostTab(double maxBattery, int maxPayload) {
        selectedJobs.clear();
        int n = jobs.size();
        int batteryLimit = (int)Math.ceil(maxBattery * 10); // multiplicamos por 10 para índices enteros
        int[][] dp = new int[n + 1][batteryLimit + 1];

        for (int i = 1; i <= n; i++) {
            Job job = jobs.get(i - 1);
            int w = job.weight;
            int b = (int)Math.ceil(job.batteryCost() * 10);
            for (int j = 0; j <= batteryLimit; j++) {
                dp[i][j] = dp[i - 1][j]; // no tomar trabajo
                if (w <= maxPayload && b <= j) {
                    dp[i][j] = Math.max(dp[i][j], job.value + dp[i - 1][j - b]);
                }
            }
        }

        // Reconstruir trabajos seleccionados
        selectedJobs.clear();
        int j = batteryLimit;
        int payload = maxPayload;
        for (int i = n; i > 0; i--) {
            Job job = jobs.get(i - 1);
            int b = (int)Math.ceil(job.batteryCost() * 10);
            if (j >= b && dp[i][j] == job.value + dp[i - 1][j - b] && job.weight <= payload) {
                selectedJobs.add(job);
                j -= b;
                payload -= job.weight;
            }
        }
        Collections.reverse(selectedJobs);

        return dp[n][batteryLimit];
    }

    // ==============================
    // DISPLAY SELECTED JOBS
    // ==============================
    public void displaySelectedJobs() {
        for (Job j : selectedJobs) {
            System.out.println(j.id);
        }
        System.out.println();
    }
}
