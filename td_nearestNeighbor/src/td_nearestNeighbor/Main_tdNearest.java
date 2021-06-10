package td_nearestNeighbor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Main_tdNearest {
	public static void main(String[]args) {
		
		String name = "C:\\Users\\m-zim\\eclipse-workspace\\td_nearestNeighbor\\shortestpath_5_75.txt";
		
		// Has to be changed each time
		int nbLocations = 75;
		int nbTimeSteps;
		int duration;
		int[][][] distanceFct;

		
		
		Scanner file;
		try {
			file = new Scanner(new File(name));
			file.useLocale(Locale.US);
			file.nextInt();
			file.nextInt();			
			nbTimeSteps = file.nextInt();
			duration = file.nextInt();
			
			distanceFct = new int[nbLocations][nbLocations][nbTimeSteps];
			
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					for(int s=0;s<nbTimeSteps;s++) {
						System.out.println("i: " + i + " j: " + j + " t: " + s );
						distanceFct[i][j][s]=file.nextInt();
						if (distanceFct[i][j][s] == 0) {
							distanceFct[i][j][s] = 9999;
						}
					}
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
