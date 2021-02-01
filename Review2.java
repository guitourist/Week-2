package javaReview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Review2 {
	
	static int day;
	static int high;
	static int low;
	static int variance;
	static int current;
	static int maxTemp;
	static int maxTempDay;
	static int minTemp;	
	static int minTempDay;
	static int calculateMax;
	static int calculateMin;

	public static void main(String[] args) {
		
		//Change location of output stream
		 PrintStream o;
		try {
			o = new PrintStream(new File("TemperaturesReport.txt"));
			System.setOut(o);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Create array
		int[][] decemberTemps = decemberTempsArray();
		
		//Print title, day, high, low and variance
		dayHighLowVariance(decemberTemps);
		
		//Print highest and lowest temperature in the month
		printMaxAndMin(decemberTemps);
		
		//Print graph of temperatures with + being max and - being min
		createGraph(decemberTemps);
		}

	/**
	 * Creates two dimensional array and enters values from csv file
	 * @param decemberTemps
	 */
	private static void createGraph(int[][] decemberTemps) {
		System.out.println("Graph");
		System.out.println("--------------------------------------------------------------");
		System.out.printf("%7d%5d%6d%5d%5d%5d%5d%5d%5d%5d%5d\n", 1, 5, 10, 15, 20, 25, 30, 35,
				40, 45, 50);
		System.out.printf("%7c%5c%5c%5c%5c%5c%5c%5c%5c%5c%5c\n", '|', '|', '|', '|', '|', '|',
				'|', '|', '|', '|', '|');
		System.out.println("--------------------------------------------------------------");
		
		for (int i = 0; i < 31; i++) {
			System.out.printf("%-3d %s", decemberTemps[i][0], "Hi");
			System.out.printf("%1s \n", "+".repeat(decemberTemps[i][1]));
			System.out.printf("%-3s %s%s \n","", "Lo", "-".repeat(decemberTemps[i][2]));
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.printf("%7c%5c%5c%5c%5c%5c%5c%5c%5c%5c%5c\n", '|', '|', '|', '|', '|', '|',
				'|', '|', '|', '|', '|');
		System.out.printf("%7d%5d%6d%5d%5d%5d%5d%5d%5d%5d%5d\n", 1, 5, 10, 15, 20, 25, 30, 35,
				40, 45, 50);
		System.out.println("--------------------------------------------------------------");
	}

	/**
	 * Creates graph showing high and low based on values from decemberTemps array
	 * @param decemberTemps
	 */
	private static void printMaxAndMin(int[][] decemberTemps) {
		maxTemp = decemberTemps[0][1];
		minTemp = decemberTemps[0][1];
		for (int i = 0; i < 31; i++) {
			for (int j = 0; j < 2; j++) {
				current = decemberTemps[i][j + 1];
				if (current > maxTemp) {
					maxTemp = current; 
					maxTempDay = decemberTemps[i][0];
				}
				if (current < minTemp) {
					minTemp = current;
					minTempDay = decemberTemps[i][0];
				}
				if (j == 0) calculateMax += decemberTemps[i][j + 1];
				if (j == 1) calculateMin += decemberTemps[i][j + 1];
			}
		}
		System.out.println("--------------------------------------------------------------");
		System.out.printf("December Highest Temperature: %4s: %d Average Hi: %.1f\n", "12/" + maxTempDay, 
				maxTemp, (double) calculateMax / 31);
		System.out.printf("December Lowest Temperature: %6s: %d Average Hi: %.1f\n", "12/" + minTempDay, 
				minTemp, (double) calculateMin / 31);
		System.out.println("--------------------------------------------------------------");
	}

	/**
	 * Prints title with explanation of table data
	 * then prints date, high, low, and variance between temperatures
	 * @param decemberTemps
	 */
	private static void dayHighLowVariance(int[][] decemberTemps) {
		System.out.println("--------------------------------------------------------------");
		System.out.println("December 2020: Temperatures in Utah");
		System.out.println("--------------------------------------------------------------");
		System.out.printf("%-6s %-6s %-6s %-6s", "Day", "High", "Low", "Variance\n");
		System.out.println("--------------------------------------------------------------");
		for (int i = 0; i < 31; i++) {
				day = decemberTemps[i][0];
				high = decemberTemps[i][1];
				low = decemberTemps[i][2];
				variance = high - low;
			System.out.printf("%-6d %-6d %-6d %-6d\n", day, high, low, variance);
			}
	}

	/**
	 * Creates two dimensional array of temperatures from given file.
	 * @return two dimensional array of temperatures
	 */
	private static int[][] decemberTempsArray() {
		String filepath = "resources\\SLCDecember2020Temperatures.csv";
		
		int[][] decemberTemps = new int[31][3];
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			for (int i = 0; i < 31; i++) {
				String line = reader.readLine();
				String[] parsed = line.split(",");
				for (int j = 0; j < 3; j++) {
					decemberTemps[i][j] = Integer.parseInt(parsed[j]);
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return decemberTemps;
	}
}
