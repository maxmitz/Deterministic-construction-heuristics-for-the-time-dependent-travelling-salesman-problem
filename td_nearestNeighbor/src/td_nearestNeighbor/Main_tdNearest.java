package td_nearestNeighbor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main_tdNearest {
	public static void main(String[]args) {
		
		// Data reading general
		int nbLocations = 0;
		int nbTimesteps = 0;
		int durationTimestep = 0;
		int[][][] distanceFct = null;
		String fileName;
		Scanner file;
		
		// Data read own + Melgarejo
		int missing = 0;
		//fileName = "0609_shortestpath_15_91.txt";
		fileName = "matrix00.txt";
		
		try {
			file = new Scanner(new File(fileName));
			file.useLocale(Locale.US);
			nbLocations = file.nextInt();		
			nbTimesteps = file.nextInt();
			durationTimestep = file.nextInt();
			distanceFct = new int[nbLocations][nbLocations][nbTimesteps];
			
			
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					for(int s=0;s<nbTimesteps;s++) {
						try {
							distanceFct[i][j][s]=file.nextInt();
							while(distanceFct[i][j][s] == 0){
								distanceFct[i][j][s]=file.nextInt();
							}
						} catch(Exception e) {
							missing ++;
						}
						
						/*
						if (distanceFct[i][j][s] == 0) {
							distanceFct[i][j][s] = 9999;
						}*/
					}
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Missing values: " + missing);
		/*
		try {
			file = new Scanner(new File(fileName));
			file.useLocale(Locale.US);
			nbLocations = file.nextInt();		
			nbTimesteps = file.nextInt();
			durationTimestep = file.nextInt();
			distanceFct = new int[nbLocations][nbLocations][nbTimesteps];
			
			for(int s=0;s<nbTimesteps;s++) {
				for(int i=0;i<nbLocations;i++) {
					for(int j=0;j<nbLocations;j++) {
						distanceFct[i][j][s]=file.nextInt();
						
						while(distanceFct[i][j][s] == 0){
							distanceFct[i][j][s]=file.nextInt();
						}
						if (distanceFct[i][j][s] == 0) {
							distanceFct[i][j][s] = 9999;

					}
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		System.out.println(distanceFct[0][0][0]);
		System.out.println(distanceFct[0][0][1]);
		System.out.println(distanceFct[0][2][0]);
		System.out.println(distanceFct[0][1][0]);
		System.out.println(distanceFct[1][0][0]);
		System.out.println(distanceFct[254][254][129]);
		
		// General TD-TSP
		int[] cities = {5,6,7,8,23,34,45,56,88};
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
				currentTimestep = totalDuration / durationTimestep;
				totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
			}
			currentTimestep = totalDuration / durationTimestep;
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
		int neighborTime;
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
				currentTimestep = totalDuration / durationTimestep;
				for (int j = 0; j < cities.length; j++ ) {
					duplicate = false;
					for (int k = 0; k < tspPath.length; k++) {
						if (cities[j] == tspPath[k]) {
							duplicate = true;
						}
					}
					if (distanceFct[i][cities[j]][currentTimestep] < neighborTime && !duplicate) {
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
		Arrays.fill(tspPath, -1);
		int compare = 99999;
		totalDuration = 0;
		currentTimestep = 0;
		// find first insertion
		/*
		for(i = 0; i < cities.length; i++) {
			for(int j = 0; j < cities.length; j++) {
				if(i != j) {
					if(distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep)] < compare) {
						tspPath[0] = cities[i];
						tspPath[1] = cities[j];
						tspPath[2] = cities[i];
						compare = distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep];
					}
				}
			}
		}
		totalDuration = compare;
		step = 2;
		System.out.println(Arrays.toString(tspPath) + " , " + compare);
		
		// flexible duration calculation
		int pathDuration = 0;
		
		for(i = 0; i < step;i++) {
			pathDuration += distanceFct[tspPath[i]][tspPath[i+1]][pathDuration/durationTimestep];
		}
		
		// try second insertion
		
		int[] testPath = new int[tspPath.length];
		int[] bestPath = new int[tspPath.length];
		Arrays.fill(testPath, -1);
		Arrays.fill(bestPath, -1);
		int bestDuration = 0;
		
		while(step < tspPath.length) {
			System.out.println(step +1  + " insertion");
			for(i = 0; i <= step; i++) {
				compare = 99999;
				for(int j = 0; j < cities.length; j++) {
					for(int k = 0; k <= step; k++) {
						testPath[k] = tspPath[k];
					}
					// Check for duplicate
					
					duplicate = false;
					
					for(int n = 0; n <= step; n++) {
						if(cities[j] == testPath[n]) {
							duplicate = true;
						}
					}
					
					// !!! Do i miss one permutation? two positions for 0 ..
					if(!duplicate){
						if(i == 0) {
							// insert at start and step + 1 and see if it is shorter than compare
							testPath[0] = cities[j];
							testPath[step+1] = cities[j];
						} else {
							for(int l = step; l >= i; l--) {
								testPath[l+1] = testPath[l];
							}
							testPath[i] = cities[j];
							testPath[step+1] = testPath[0];
						}
						pathDuration = 0;
						for(int m = 0; m < step;m++) {
							pathDuration += distanceFct[testPath[m]][testPath[m+1]][pathDuration/durationTimestep];
						}
						if(pathDuration < compare) {
							compare = pathDuration;
							bestDuration = pathDuration;
							for(int k = 0; k <= step + 1; k++) {
								bestPath[k] = testPath[k];
							}
							//System.out.println(Arrays.toString(bestPath) + " , " + bestDuration + ", duplicate: " + duplicate);

						}	
					}
				}
				
			}
			for (int o = 0; o < tspPath.length;o++) {
				tspPath[o] = bestPath[o];
			}
			pathDuration = 0;
			for(int m = 0; m < step;m++) {
				pathDuration += distanceFct[bestPath[m]][bestPath[m+1]][pathDuration/durationTimestep];
			}
			
			System.out.println("Best Path: " + Arrays.toString(bestPath) + " , " + bestDuration);
			step++;
		}
		*/
		
		// Christofides' algorithm
		System.out.println("\n\nChristofides' algorithm");	
		Arrays.fill(tspPath, -1);
		currentTimestep = 0;
		
		// Start minimum-cost arborescence
		step = 1;
		int[] predecessor = new int[cities.length];
		int[] treeElements = new int[cities.length];
		int[] timeTree = new int[cities.length];
		int[] matchingTreeElements;
		int[] matchingPredecessor;
		
		
		// change to < cities.length
		for(int l = 0; l < 1; l++) {
			
			System.out.println("Minimum-cost arborescence");
			Arrays.fill(predecessor, -1);
			Arrays.fill(treeElements, -1);
			treeElements[0] = cities[l];
			Arrays.fill(timeTree, 0);
			
			for(step = 1; step < treeElements.length; step++) {
				compare = 99999;
				for(i = 0; i < step; i++) {
					currentTimestep = timeTree[i] / durationTimestep;
					for(int j = 0; j < cities.length; j++) {
						
						duplicate = false;
						for (int k = 0; k < step; k++) {
							if(treeElements[k] == cities[j]) {
								duplicate = true;
							}
						}
						if (distanceFct[treeElements[i]][cities[j]][currentTimestep] < compare && !duplicate) {
							compare = distanceFct[treeElements[i]][cities[j]][currentTimestep];
							treeElements[step] = cities[j];
							predecessor[step] = treeElements[i];
							timeTree[step] = distanceFct[treeElements[i]][cities[j]][currentTimestep] + timeTree[i];						
						}
					}
				}
				totalDuration = 0;
				for (int k = 1; k <= step;k++) {
					totalDuration += distanceFct[predecessor[k]][treeElements[k]][currentTimestep];
				}
			}
			
			System.out.println(Arrays.toString(treeElements) + " , total Duration: " + totalDuration);
			System.out.println(Arrays.toString(predecessor));
			//System.out.println(Arrays.toString(timeTree));
			
			
			// Add Edges for nbInEdges != nbOutEdges
			System.out.println("Find matching");
			
			int nbAdditionalEdges = 1;
			
			for(int k = 0; k < cities.length; k++) {
				int helpCounter = 0;
				for(int m = 0; m < treeElements.length; m++) {
					if (cities[k] == predecessor[m]) {
						helpCounter++;
					}
				}
				if(helpCounter > 1) {
					nbAdditionalEdges += helpCounter - 1;
				}
			}
			System.out.println("Needed additional edges: " + nbAdditionalEdges);
			
			matchingTreeElements = new int[cities.length + nbAdditionalEdges];
			Arrays.fill(matchingTreeElements, -1);
			matchingPredecessor = new int[cities.length + nbAdditionalEdges];
			Arrays.fill(matchingPredecessor, -1);
			for(int k = 0; k < cities.length; k++) {
				matchingTreeElements[k] = treeElements[k];
				matchingPredecessor[k] = predecessor[k];
			}
			System.out.println(Arrays.toString(matchingTreeElements));
			System.out.println(Arrays.toString(matchingPredecessor));
			
			
		}
	}
}
