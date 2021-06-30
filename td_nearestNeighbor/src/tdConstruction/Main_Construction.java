package tdConstruction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main_Construction {
	// Data reading general
	static int nbLocations = 0;
	static int nbTimesteps = 0;
	static int durationTimestep = 0;
	static double[][][] distanceFct = null;
	static String fileName;
	Scanner file;
	
	static int[] cities;
	
	static int[] citiesHelp;
	static int[] tspPath;
	static int currentTimestep = 0;
	static double totalDuration = 0;
	static double bestResult = 999999;
	static int[] bestResultPath;
	
	
	public static void main(String[]args) {
		
		
		
		
		// Cordeau
		int[] numbersCordeau = {15,20,25,30,35,40};
		int counter = 0;
		String[] stringListOfFiles = new String[6*30];
		for(int j=0;j<numbersCordeau.length;j++) {
			File folder = new File("C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\"+Integer.toString(numbersCordeau[j]));
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
			  if (listOfFiles[i].isFile()) {
			    stringListOfFiles[counter] = Integer.toString(numbersCordeau[j]) +"\\"+ listOfFiles[i].getName();
			    counter++;
			  }
			}
		}
		for (String name :stringListOfFiles) {
			System.out.println("\n"+name);
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\"+name;
			
			
			DataReading dataReading = new DataReading("Cordeau",fileName);
			distanceFct = dataReading.getDistanceFct();
			//System.out.println(Arrays.deepToString(distanceFct));
			nbLocations = dataReading.getnbLocations();
			//System.out.println(nbLocations);
			nbTimesteps = dataReading.getnbTimesteps();
			//System.out.println(nbTimesteps);
			durationTimestep = dataReading.getdurationTimestep();
			//System.out.println(durationTimestep);
						
			// Compare with given results
			int[] solutionCordeau = {0,6,15,14,3,1,11,8 ,2 ,12, 4, 7, 9 ,13, 10, 5, 16};
			int TtotalDuration = 0;
			int TcurrentTimestep = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				TcurrentTimestep = TtotalDuration / durationTimestep;
				TtotalDuration += distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][TcurrentTimestep];
				//System.out.println("from: " + solutionCordeau[k] + " to: "+ solutionCordeau[k + 1] + " in Timestep: " + TcurrentTimestep + " takes " + distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][TcurrentTimestep]);
			}
			//System.out.println("total duration: " + TtotalDuration);
			
			
			/*
			
			// Melgarejo + from
			//fileName = "0609_shortestpath_15_91.txt";
			fileName = "matrix00.txt";
			fileName = "inst-61-6-8-D100_fromF.txt";
					
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
							distanceFct[i][j][s]=file.nextInt();

						}
					}
				}
				file.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			// Data read own
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
			//System.out.println(distanceFct[0][0][0]);
			
			// General TD-TSP
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			

			
			// first fit
			doFirstFit();
			
			// nearest neighbor
			System.out.println("\n\nIterated nearest neighbor algorithm");
			
			Arrays.fill(tspPath, -1);
			int startingCity;
			int step;
			double neighborTime;
			boolean duplicate;
			bestResult = 999999;
			
			
			
			for( int l = 0; l < cities.length; l ++) {
				startingCity = cities[l];
				Arrays.fill(tspPath, -1);
				tspPath[0] = cities[l];
				tspPath[tspPath.length - 1] = cities[l];
				step = 1;
				totalDuration = 0;
				
				while (step < cities.length) {
					neighborTime = 99999;
					currentTimestep = (int) (totalDuration / durationTimestep);
					for (int j = 0; j < cities.length; j++ ) {
						duplicate = false;
						for (int k = 0; k < tspPath.length; k++) {
							if (cities[j] == tspPath[k]) {
								duplicate = true;
							}
						}
						if (distanceFct[startingCity][cities[j]][currentTimestep] < neighborTime && !duplicate) {
							tspPath[step] = cities[j];
							neighborTime = distanceFct[startingCity][cities[j]][currentTimestep];
						}
					}
					totalDuration += distanceFct[startingCity][tspPath[step]][currentTimestep];
					startingCity = tspPath[step];
					step++;
				}
				//System.out.println(Arrays.toString(tspPath)+ "total duration: " + totalDuration + ", current Timestep: " + currentTimestep);
				if(totalDuration < bestResult) {
					bestResult = totalDuration;
					for(int i = 0;i<bestResultPath.length;i++) {
						bestResultPath[i] = tspPath[i];
					}
				}
			}
			System.out.println(Arrays.toString(bestResultPath)+ "total duration: " + bestResult);

			saveInCSV(name,"Iterated nearest neighbor algorithm",bestResult,bestResultPath);
			
			// Nearest insertion algorithm
			System.out.println("\n\nNearest insertion algorithm");	
			Arrays.fill(tspPath, -1);
			double compare = 99999;
			totalDuration = 0;
			currentTimestep = 0;
			// find first insertion

			for(int i = 0; i < cities.length; i++) {
				for(int j = 0; j < cities.length; j++) {
					if(i != j) {
						if(distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep)] < compare) {
							tspPath[0] = cities[i];
							tspPath[1] = cities[j];
							tspPath[2] = cities[i];
							compare = distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep)];
						}
					}
				}
			}
			totalDuration = compare;
			step = 2;		
			// flexible duration calculation
			int pathDuration = 0;
			
			for(int i = 0; i < step;i++) {
				pathDuration += distanceFct[tspPath[i]][tspPath[i+1]][pathDuration/durationTimestep];
			}
			
			// try second insertion
			
			int[] testPath = new int[tspPath.length];
			int[] bestPath = new int[tspPath.length];
			Arrays.fill(testPath, -1);
			Arrays.fill(bestPath, -1);
			int bestDuration = 0;
			
			//Changed to tspPath-1
			while(step < tspPath.length) {
				for(int i = 0; i <= step; i++) {
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
								//for Cordeau
								if (pathDuration/durationTimestep >= nbTimesteps) {
									pathDuration += distanceFct[testPath[m]][testPath[m+1]][nbTimesteps-1];
								}else {
									pathDuration += distanceFct[testPath[m]][testPath[m+1]][pathDuration/durationTimestep];
								}
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
				step++;
			}
			System.out.println(Arrays.toString(bestPath) + " , " + bestDuration);
			saveInCSV(name,"Nearest insertion algorithm",bestDuration,bestPath);
			
		}
		
	}
	
	public static void saveInCSV(String fileName, String heuristic,double bestResult, int[]bestResultPath) {
		try {
			FileWriter writer = new FileWriter("test.csv",true);
			writer.append("\n"+fileName+";"+heuristic+";"+(int)bestResult+";"+Arrays.toString(bestResultPath));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void doFirstFit() {
		System.out.println("Iterated First-Fit algorithm");
		for(int l = 0; l < cities.length; l++) {
			totalDuration = 0;
			for(int k = 0; k < cities.length -1; k++) {
				currentTimestep = (int) (totalDuration / durationTimestep);
				// for Cordeau
				if(currentTimestep > nbTimesteps -1) {
					currentTimestep = nbTimesteps -1;
				}
				totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
			}
			currentTimestep = (int) (totalDuration / durationTimestep);
			// for Cordeau
			if(currentTimestep > nbTimesteps -1) {
				currentTimestep = nbTimesteps -1;
			}
			totalDuration += distanceFct[cities[cities.length -1]][cities[0]][currentTimestep];
			
			if(bestResult > totalDuration) {
				bestResult = totalDuration;
				for(int i = 0;i<bestResultPath.length -1;i++) {
					bestResultPath[i] = cities[i];
				}
				bestResultPath[bestResultPath.length - 1] = bestResultPath[0];
			}
			
			for(int m = 0; m < citiesHelp.length - 1; m++) {
				citiesHelp[m] = cities[m+1];
			}
			citiesHelp[citiesHelp.length - 1] = cities[0];
			
			for(int m = 0; m < cities.length; m++) {
				cities[m] = citiesHelp[m];
			}
		}
		System.out.println(Arrays.toString(bestResultPath) + " , objective value: " + bestResult);
		
		saveInCSV(fileName,"Iterated First-Fit algorithm",bestResult,bestResultPath);
				  
		
	}
}
