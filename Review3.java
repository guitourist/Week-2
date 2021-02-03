package javaReview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Review3 {
	
	static String[][] dbResults = new String[0][0];
	static String sql;
	static int month;
	static String monthLabel;
	
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Hello there! Select from the following options...");
		printLine();
		System.out.println("1. November Temperatures");
		System.out.println("2. December Temperatures");
		switch (sc.nextInt()) {
		case 1:
			month = 11;
			runAllSections(month);
			setOutputToFile();
			runAllSections(month);
			break;
		case 2:
			month = 12;
			runAllSections(month);
			setOutputToFile();
			runAllSections(month);
			break;
		default:
			System.out.println("Rerun program and enter 1 or 2...");
			break;
		}
	}
	
	public static void runAllSections(int month) {
		initializeArray(month);
		dayHighLowVariance(dbResults);
		printMaxAndMin(dbResults);
		createGraph(dbResults);
	}
	
	/**
	 * Creates two dimensional array of temperatures from sql file.
	 * @return two dimensional array of temperatures
	 */
	private static String[][] initializeArray(int month) {
		// Your database connection information may be different depending on 
		// your MySQL installation and the dbLogin and dbPassword you choose
		// to use in your database.
		String connectionString = "jdbc:mysql://127.0.0.1:3306/practice";
        String dbLogin = "adrienbaldwin";
        String dbPassword = "0000";
        Connection conn = null;
        monthLabel = (month == 11 ? "November" : "December");
        sql = "SELECT month, day, year, hi, lo FROM temperatures "
        		+ "WHERE month = " + month + " AND year = 2020 ORDER BY month, day, year;";
        
        try 
        {
            conn = DriverManager.getConnection(connectionString, dbLogin, dbPassword);
            if (conn != null) 
            {
                try (Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
            		ResultSet rs = stmt.executeQuery(sql))
                {
                    int numRows;
                    int numCols = 5;
                    rs.last();
                    numRows = rs.getRow();
                    rs.first();
                    dbResults = new String[numRows][numCols];;
                    for (int i = 0; i < numRows; i++)
                    {
                        dbResults[i][0] = rs.getString("month");
                        dbResults[i][1] = rs.getString("day");
                        dbResults[i][2] = rs.getString("year");
                        dbResults[i][3] = rs.getString("hi");
                        dbResults[i][4] = rs.getString("lo");
                        rs.next();
                    }
                } 
                catch (SQLException ex) 
                {
                    System.out.println(ex.getMessage());
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
        return dbResults;
	}
	
	/**
	 * Creates graph showing high and low based on values from decemberTemps array
	 * @param decemberTemps
	 */
	private static void printMaxAndMin(String[][] dbResults) {
		int maxTemp = Integer.parseInt(dbResults[0][4]);
		int minTemp = Integer.parseInt(dbResults[0][3]);
		String maxTempDay = "";
		String minTempDay = "";
		String maxTempMonth = "";
		String minTempMonth = "";
		int current;
		int totalMax = 0;
		int totalMin = 0;
		for (int i = 0; i < dbResults.length; i++) {
			for (int j = 3; j < 5; j++) {
				current = Integer.parseInt(dbResults[i][j]);
				if (current > maxTemp) {
					maxTemp = current; 
					maxTempMonth = dbResults[i][0];
					maxTempDay = dbResults[i][1];
				}
				if (current < minTemp) {
					minTemp = current;
					minTempMonth = dbResults[i][0];
					minTempDay = dbResults[i][1];
				}
				if (j == 3) totalMax += Integer.parseInt(dbResults[i][j]);
				if (j == 4) totalMin += Integer.parseInt(dbResults[i][j]);
			}
		}
		printLine();
		System.out.printf("%s Highest Temperature: %3s/%s: %d Average Hi: %.1f\n", monthLabel, maxTempMonth, maxTempDay, 
				maxTemp, (double) totalMax / (month == 11 ? 30 : 31));
		System.out.printf("%s Lowest Temperature: %4s/%s: %d Average Hi: %.1f\n", monthLabel, minTempMonth, minTempDay, 
				minTemp, (double) totalMin / (month == 11 ? 30 : 31));
		printLine();
	}
	
	// Method to print lines in the console as a separator
	private static void printLine() 
	{
		for (int i = 1; i <= 60; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");
	}
	
	/**
	 * Creates two dimensional array and enters values from csv file
	 * @param decemberTemps
	 */
	private static void createGraph(String[][] dbResults) {
		System.out.println("Graph");
		printLine();
		System.out.printf("%7d%5d%6d%5d%5d%5d%5d%5d%5d%5d%5d\n", 1, 5, 10, 15, 20, 25, 30, 35,
				40, 45, 50);
		System.out.printf("%7c%5c%5c%5c%5c%5c%5c%5c%5c%5c%5c\n", '|', '|', '|', '|', '|', '|',
				'|', '|', '|', '|', '|');
		printLine();
		
		for (int i = 0; i < dbResults.length; i++) {
			System.out.printf("%-3s %s", dbResults[i][1], "Hi");
			System.out.printf("%1s \n", "+".repeat(Integer.parseInt(dbResults[i][3])));
			System.out.printf("%-3s %s%s \n","", "Lo", "-".repeat(Integer.parseInt(dbResults[i][4])));
		}
		
		printLine();
		System.out.printf("%7c%5c%5c%5c%5c%5c%5c%5c%5c%5c%5c\n", '|', '|', '|', '|', '|', '|',
				'|', '|', '|', '|', '|');
		System.out.printf("%7d%5d%6d%5d%5d%5d%5d%5d%5d%5d%5d\n", 1, 5, 10, 15, 20, 25, 30, 35,
				40, 45, 50);
		printLine();
	}
			
	/**
	 * Prints title with explanation of table data
	 * then prints date, high, low, and variance between temperatures
	 * @param decemberTemps
	 */
	private static void dayHighLowVariance(String[][] dbResults) {
		printLine();
		System.out.printf("%s 2020: Temperatures in Utah%n", monthLabel);
		printLine();
		System.out.printf("%-12s %-6s %-6s %-6s%n", "Date", "High", "Low", "Variance");
		printLine();
		for (int i = 0; i < dbResults.length; i++) {
			String date = String.format("%s/%s/%s", dbResults[i][0], dbResults[i][1], dbResults[i][2]);
			String high = String.format("%s", dbResults[i][3]);
			String low = String.format("%s", dbResults[i][4]);
			String variance = String.format("%s", Integer.parseInt(high) - Integer.parseInt(low));
		System.out.printf("%-12s %-6s %-6s %-6s\n", date, high, low, variance);
		}
	}
	
	private static void setOutputToFile() {
		PrintStream o;
		try {
			o = new PrintStream(new File("TemperaturesReportFromDB.txt"));
			System.setOut(o);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}