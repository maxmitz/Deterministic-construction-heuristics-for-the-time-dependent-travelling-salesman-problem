package td_nearestNeighbor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main_tdNearest {
	public static void main(String[]args) {
		
		
		// Data reading
		String name = "0609_shortestpath_15_91.txt";
		// Has to be changed each time
		int nbLocations = 91;
		int nbTimesteps = -1;
		int duration = 0;
		int[][][] distanceFct = null;
		Scanner file;
		try {
			file = new Scanner(new File(name));
			file.useLocale(Locale.US);
			file.nextInt();
			file.nextInt();			
			nbTimesteps = file.nextInt();
			duration = file.nextInt();
			
			distanceFct = new int[nbLocations][nbLocations][nbTimesteps];
			
			for(int s=0;s<nbTimesteps;s++) {
				for(int i=0;i<nbLocations;i++) {
					for(int j=0;j<nbLocations;j++) {
						distanceFct[i][j][s]=file.nextInt();
						if (distanceFct[i][j][s] == 0) {
							distanceFct[i][j][s] = 9999;
						}
					}
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// General TD-TSP
		
		int[] cities = {5,6,7,8,23,34,45,56};
		int[] citiesHelp = new int[cities.length];
		int[] tspPath = new int[cities.length + 1];
		int currentTimestep = 0;
		int totalDuration = 0;
		
		// first fit
		System.out.println("First-Fit algorithm");
		for( int l = 0; l < cities.length; l++) {
			totalDuration = 0;
			for( int k = 0; k < cities.length -1; k++) {
				System.out.print(cities[k] + " - ");
				currentTimestep = totalDuration / duration;
				totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
			}
			currentTimestep = totalDuration / duration;
			totalDuration += distanceFct[cities[cities.length -1]][cities[0]][currentTimestep];
			System.out.println(cities[cities.length-1] + " - " + cities[0] + ", total duration: " + totalDuration);
			
			for(int m = 0; m < citiesHelp.length - 1; m++) {
				citiesHelp[m] = cities[m+1];
			}
			citiesHelp[citiesHelp.length - 1] = cities[0];
			
			for(int m = 0; m < cities.length; m++) {
				cities[m] = citiesHelp[m];
			}
		}
		
		// nearest neighbor
		System.out.println("\n\nNearest neighbor algorithm");
		
		Arrays.fill(tspPath, -1);
		int i = cities[0];
		tspPath[0] = cities[0];
		tspPath[tspPath.length - 1] = cities[0];
		int step = 1;
		int neighborTime = 99999;
		boolean duplicate;
		
		
		for( int l = 0; l < cities.length; l ++) {
			i = cities[l];
			Arrays.fill(tspPath, -1);
			tspPath[0] = cities[l];
			tspPath[tspPath.length - 1] = cities[l];
			step = 1;
			totalDuration = 0;
			
			while (step < cities.length) {
				neighborTime = 99999;
				currentTimestep = totalDuration / duration;
				for (int j = 0; j < cities.length; j++ ) {
					duplicate = false;
					for (int k = 0; k < tspPath.length; k++) {
						if (cities[j] == tspPath[k]) {
							duplicate = true;
						}
					}
					if (distanceFct[i][j][currentTimestep] < neighborTime && !duplicate) {
						tspPath[step] = cities[j];
					}
				}
				totalDuration += distanceFct[i][tspPath[step]][currentTimestep];
				i = tspPath[step];
				step++;
			}
			for( int k = 0; k < tspPath.length; k++) {
				System.out.print(tspPath[k] + " - ");
			}
			System.out.println("total duration: " + totalDuration + ", current Timestep: " + currentTimestep);

		}
		
		// Nearest insertion algorithm
		
		System.out.println("\n\nNearest insertion algorithm");
		
	}
}
