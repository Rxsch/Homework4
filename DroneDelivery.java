/* Daniel Rangosch
   Dr. Steinberg
   COP3503 Fall 2025
   Programming Assignment 4
*/

import java.io.*;
import java.util.*;

public class DroneDelivery {

	// Represents a delivery job
	class Job {
		String id;
		int weight;
		int distance;
		int value;

		// Create a new job
		Job(String id, int weight, int distance, int value) {
			this.id = id;
			this.weight = weight;
			this.distance = distance;
			this.value = value;
		}

		// Compute battery cost for job
		double batteryCost() {
			return distance * (1.0 + 0.1 * weight);
		}
	}

	// Store all jobs
	private ArrayList<Job> jobs = new ArrayList<>();
	// Store selected jobs
	private ArrayList<Job> selectedJobs = new ArrayList<>();
	// Memoization map for DP
	private Map<String, Integer> memoMap;

	// Load jobs from file
	public DroneDelivery(String filename) throws Exception {
		Scanner scan = new Scanner(new File(filename));
		while (scan.hasNext()) {
			String id = scan.next();
			int distance = scan.nextInt();  // distance first
			int weight = scan.nextInt();    // then weight
			int value = scan.nextInt();     // then value
			jobs.add(new Job(id, weight, distance, value));
		}
		scan.close();
	}

	// RECURSION

	// Compute maximum value recursively
	public int maxCostRecursive(double maxBattery, int maxPayload) {
		selectedJobs.clear();
		int n = jobs.size();
		int maxValue = maxCostRecursiveHelper(n - 1, maxBattery, maxPayload);
		reconstructSelectedJobsRecursive(maxBattery, maxPayload);
		return maxValue;
	}

	// Recursive helper method
	private int maxCostRecursiveHelper(int i, double remainBattery, int remainPayload) {
		if (i < 0) return 0;
		Job job = jobs.get(i);
		double batteryNeeded = job.batteryCost();

		// Option 1: skip the job
		int exclude = maxCostRecursiveHelper(i - 1, remainBattery, remainPayload);

		// Option 2: include the job if possible
		int include = 0;
		if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
			include = job.value + maxCostRecursiveHelper(
			              i - 1, remainBattery - batteryNeeded, remainPayload - job.weight
			          );
		}

		return Math.max(include, exclude);
	}

	// Reconstruct selected jobs for recursion
	private void reconstructSelectedJobsRecursive(double maxBattery, int maxPayload) {
		selectedJobs.clear();
		int i = jobs.size() - 1;
		double remainBattery = maxBattery;
		int remainPayload = maxPayload;

		while (i >= 0) {
			Job job = jobs.get(i);
			double batteryNeeded = job.batteryCost();
			int exclude = maxCostRecursiveHelper(i - 1, remainBattery, remainPayload);
			int include = 0;
			if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
				include = job.value + maxCostRecursiveHelper(
				              i - 1, remainBattery - batteryNeeded, remainPayload - job.weight
				          );
			}
			if (include > exclude) {
				selectedJobs.add(job);
				remainBattery -= batteryNeeded;
				remainPayload -= job.weight;
			}
			i--;
		}
		Collections.reverse(selectedJobs);
	}

	// MEMOIZATION DP

	// Compute maximum value using memoization
	public int maxCostMemo(double maxBattery, int maxPayload) {
		selectedJobs.clear();
		memoMap = new HashMap<>();
		int n = jobs.size();
		int maxValue = maxCostMemoHelper(n - 1, maxBattery, maxPayload);
		reconstructSelectedJobsMemo(maxBattery, maxPayload);
		return maxValue;
	}

	// Memoization helper method
	private int maxCostMemoHelper(int i, double remainBattery, int remainPayload) {
		if (i < 0) return 0;

		// Create unique key for memo map
		String key = i + "|" + (int)(remainBattery*10) + "|" + remainPayload;
		if (memoMap.containsKey(key)) return memoMap.get(key);

		Job job = jobs.get(i);
		double batteryNeeded = job.batteryCost();

		// Option 1: skip job
		int exclude = maxCostMemoHelper(i - 1, remainBattery, remainPayload);

		// Option 2: try including this job
		int include = 0;
		if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
			include = job.value + maxCostMemoHelper(
			              i - 1, remainBattery - batteryNeeded, remainPayload - job.weight
			          );
		}

		int result = Math.max(include, exclude);
		memoMap.put(key, result);
		return result;
	}

	// Reconstruct selected jobs for memoization
	private void reconstructSelectedJobsMemo(double maxBattery, int maxPayload) {
		selectedJobs.clear();
		int i = jobs.size() - 1;
		double remainBattery = maxBattery;
		int remainPayload = maxPayload;

		while (i >= 0) {
			Job job = jobs.get(i);
			double batteryNeeded = job.batteryCost();
			int exclude = maxCostMemoHelper(i - 1, remainBattery, remainPayload);
			int include = 0;
			if (job.weight <= remainPayload && batteryNeeded <= remainBattery) {
				include = job.value + maxCostMemoHelper(
				              i - 1, remainBattery - batteryNeeded, remainPayload - job.weight
				          );
			}
			if (include > exclude) {
				selectedJobs.add(job);
				remainBattery -= batteryNeeded;
				remainPayload -= job.weight;
			}
			i--;
		}
		Collections.reverse(selectedJobs);
	}

	// TABULATION DP (BOTTOM-UP)

	// Compute maximum value using bottom-up DP
	public int maxCostTab(double maxBattery, int maxPayload) {
		selectedJobs.clear();
		int n = jobs.size();
		int batteryLimit = (int)Math.ceil(maxBattery * 10);

		// DP table for job selection
		int[][] dp = new int[n + 1][batteryLimit + 1];

		// Fill DP table
		for (int i = 1; i <= n; i++) {
			Job job = jobs.get(i - 1);
			int w = job.weight;
			int b = (int)Math.ceil(job.batteryCost() * 10);
			for (int j = 0; j <= batteryLimit; j++) {
				// Skip current job
				dp[i][j] = dp[i - 1][j];
				// Try including this job
				if (w <= maxPayload && b <= j) {
					dp[i][j] = Math.max(dp[i][j], job.value + dp[i - 1][j - b]);
				}
			}
		}

		// Reconstruct selected jobs
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

	// Print all selected job IDs
	public void displaySelectedJobs() {
		for (Job j : selectedJobs) {
			System.out.println(j.id);
		}
		System.out.println();
	}
}
