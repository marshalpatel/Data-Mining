package com.marshalp.aggclustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;


 /*Main Class: Entry point of the program*/

public class ACMain {

	private static boolean isuser;

	public static boolean isUser() {

		return isuser;

	}

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("Enter your choice:");
		System.out.println("1. Age, Profession");
		System.out.println("0. Genre, Rating");
		//Print the out to file.
		System.setOut(new PrintStream("Output.txt"));
		/*Reading input from the user*/
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		try {
			int choice = Integer.parseInt(br.readLine());

			if (choice == 1)
				isuser = true;
			else
				isuser = false;

		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}

		DataAccess dao = new DataAccess();
		ArrayList<DataPoints> uList;

		try {
			uList = dao.getUserData();
		} catch (Exception e) {
			e.printStackTrace();
			uList = null;
			System.exit(0);

		}
		
		/*invoking the clustering routine*/
		
		PerformClustering pc = new PerformClustering();
		pc.CreateDistanceMatrix(uList);

	}

}
