/* INSERT YOUR NAME HERE
   Dr. Steinberg
   COP3503 Fall 2025
   Programming Assignment 4
*/

import java.util.*;


public class DroneDeliveryDriver
{
    public static void main(String[] args) throws Exception
	{
		DroneDelivery testcase1 = new DroneDelivery("jobs1.txt");
		DroneDelivery testcase2 = new DroneDelivery("jobs2.txt");
		DroneDelivery testcase3 = new DroneDelivery("jobs3.txt");
		DroneDelivery testcase4 = new DroneDelivery("jobs4.txt");
		DroneDelivery testcase5 = new DroneDelivery("jobs5.txt");
		
		int vals [] = {80, 53, 230, 220, 373};
		
		System.out.println("********Test Case 1********");
		
		
		int testCase1RecursiveResult = testcase1.maxCostRecursive(10.0, 4);
		int testCase1MemoResult = testcase1.maxCostMemo(10.0, 4);
		int testCase1TabulationResult = testcase1.maxCostTab(10.0, 4);
		
		if(testCase1RecursiveResult ==  testCase1MemoResult && testCase1MemoResult == testCase1TabulationResult && testCase1TabulationResult == vals[0])
		{
			System.out.println("Test Case 1 Fully Passed");
		}
		else
		{
			if(testCase1RecursiveResult != vals[0])
				System.out.println("Test Case 1 Recursive Method Returned Incorrect Value");
			
			if(testCase1MemoResult != vals[0])
				System.out.println("Test Case 1 Memoization Method Returned Incorrect Value");
			
			if(testCase1TabulationResult != vals[0])
				System.out.println("Test Case 1 Tabulation Method Returned Incorrect Value");
		}
		
		System.out.println("Jobs Selected for Test Case 1");
		testcase1.displaySelectedJobs();
		

		System.out.println("********Test Case 2********");
		
		
		int testCase2RecursiveResult = testcase2.maxCostRecursive(4.0, 13);
		int testCase2MemoResult = testcase2.maxCostMemo(4.0, 13);
		int testCase2TabulationResult = testcase2.maxCostTab(4.0, 13);
		
		if(testCase2RecursiveResult ==  testCase2MemoResult && testCase2MemoResult == testCase2TabulationResult && testCase2TabulationResult == vals[1])
		{
			System.out.println("Test Case 2 Fully Passed");
		}
		else
		{
			if(testCase2RecursiveResult != vals[1])
				System.out.println("Test Case 2 Recursive Method Returned Incorrect Value");
			
			if(testCase2MemoResult != vals[1])
				System.out.println("Test Case 2 Memoization Method Returned Incorrect Value");
			
			if(testCase2TabulationResult != vals[1])
				System.out.println("Test Case 2 Tabulation Method Returned Incorrect Value");
		}
		
		System.out.println("Jobs Selected for Test Case 2");
		testcase2.displaySelectedJobs();


		System.out.println("********Test Case 3********");
		
		
		int testCase3RecursiveResult = testcase3.maxCostRecursive(12.0, 19);
		int testCase3MemoResult = testcase3.maxCostMemo(12.0, 19);
		int testCase3TabulationResult = testcase3.maxCostTab(12.0, 19);
		
		if(testCase3RecursiveResult ==  testCase3MemoResult && testCase3MemoResult == testCase3TabulationResult && testCase3TabulationResult == vals[2])
		{
			System.out.println("Test Case 3 Fully Passed");
		}
		else
		{
			if(testCase3RecursiveResult != vals[2])
				System.out.println("Test Case 3 Recursive Method Returned Incorrect Value");
			
			if(testCase3MemoResult != vals[2])
				System.out.println("Test Case 3 Memoization Method Returned Incorrect Value");
			
			if(testCase3TabulationResult != vals[2])
				System.out.println("Test Case 3 Tabulation Method Returned Incorrect Value");
		}
		
		System.out.println("Jobs Selected for Test Case 3");
		testcase3.displaySelectedJobs();
		
		System.out.println("********Test Case 4********");
		
		
		int testCase4RecursiveResult = testcase4.maxCostRecursive(20.0, 20);
		int testCase4MemoResult = testcase4.maxCostMemo(20.0, 20);
		int testCase4TabulationResult = testcase4.maxCostTab(20.0, 20);
		
		if(testCase4RecursiveResult ==  testCase4MemoResult && testCase4MemoResult == testCase4TabulationResult && testCase4TabulationResult == vals[3])
		{
			System.out.println("Test Case 4 Fully Passed");
		}
		else
		{
			if(testCase4RecursiveResult != vals[3])
				System.out.println("Test Case 4 Recursive Method Returned Incorrect Value");
			
			if(testCase4MemoResult != vals[3])
				System.out.println("Test Case 4 Memoization Method Returned Incorrect Value");
			
			if(testCase4TabulationResult != vals[3])
				System.out.println("Test Case 4 Tabulation Method Returned Incorrect Value");
		}
		
		System.out.println("Jobs Selected for Test Case 4");
		testcase4.displaySelectedJobs();
		
		System.out.println("********Test Case 5********");
		
		int testCase5RecursiveResult = testcase5.maxCostRecursive(25.0, 20);
		int testCase5MemoResult = testcase5.maxCostMemo(25.0, 20);
		int testCase5TabulationResult = testcase5.maxCostTab(25.0, 20);
		
		if(testCase5RecursiveResult ==  testCase5MemoResult && testCase5MemoResult == testCase5TabulationResult && testCase5TabulationResult == vals[4])
		{
			System.out.println("Test Case 5 Fully Passed");
		}
		else
		{
			if(testCase5RecursiveResult != vals[4])
				System.out.println("Test Case 5 Recursive Method Returned Incorrect Value");
			
			if(testCase5MemoResult != vals[4])
				System.out.println("Test Case 5 Memoization Method Returned Incorrect Value");
			
			if(testCase5TabulationResult != vals[4])
				System.out.println("Test Case 5 Tabulation Method Returned Incorrect Value");
		}
		
		System.out.println("Jobs Selected for Test Case 5");
		testcase5.displaySelectedJobs();
	}
}