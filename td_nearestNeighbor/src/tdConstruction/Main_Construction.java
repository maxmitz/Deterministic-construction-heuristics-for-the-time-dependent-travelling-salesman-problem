package tdConstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main_Construction {
	
	static int nbLocations = 0;
	static int nbTimesteps = 0;
	static int durationTimestep = 0;
	static double[][][] distanceFct = null;
	static String fileName;
	static Scanner file;
	static int[] cities;
	static int[] citiesHelp;
	static int[] tspPath;
	static int currentTimestep = 0;
	static double totalDuration = 0;
	static double bestResult = 999999;
	static int[] bestResultPath;
	static int step;
	static boolean duplicate;
	static double[][] distanceFctTimeIndependent;
	
	static int bestDuration = 999999;
	static String[] stringListOfFiles;
	
	public static void main(String[]args) {

		/*
		// Cordeau
		int[] numbersCordeau = {15,20,25,30,35,40};
		int counter = 0;
		stringListOfFiles = new String[6*30];
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
			nbLocations = dataReading.getnbLocations();
			nbTimesteps = dataReading.getnbTimesteps();
			durationTimestep = dataReading.getdurationTimestep();
				
						*/
			// Compare with given results from Cordeau
			/*
			int[] solutionCordeau = {0,6,15,14,3,1,11,8 ,2 ,12, 4, 7, 9 ,13, 10, 5, 16};
			int TtotalDuration = 0;
			int TcurrentTimestep = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				TcurrentTimestep = TtotalDuration / durationTimestep;
				TtotalDuration += distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][TcurrentTimestep];
				//System.out.println("from: " + solutionCordeau[k] + " to: "+ solutionCordeau[k + 1] + " in Timestep: " + TcurrentTimestep + " takes " + distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][TcurrentTimestep]);
			}
			//System.out.println("total duration: " + TtotalDuration);
			*/
			
		/*
			//System.out.println(distanceFct[0][0][0]);
			
			// General TD-TSP
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
		}
		
		// Melgarejo
		fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Melgarejo\\Matrices\\matrix00.txt";
				
		DataReading dataReading = new DataReading(fileName);
		distanceFct = dataReading.getDistanceFctMelgarejo();
		nbLocations = dataReading.getnbLocations();
		nbTimesteps = dataReading.getnbTimesteps();
		durationTimestep = dataReading.getdurationTimestep();
		
		// General TD-TSP
		cities = new int[nbLocations];
		for(int i=0;i<nbLocations;i++) {
			cities[i] = i;
		}
		citiesHelp = new int[cities.length];
		tspPath = new int[cities.length + 1];
		bestResultPath = new int[cities.length + 1];
		
		//doFirstFit();
		//doNearestNeighbor();
		//doNearestInsertion();
		
		
		// Compare with results from instances
		
		permute(java.util.Arrays.asList(222,190,81,32,9,132,240 ,74 ,127, 150),0);
		*/
		
		// Rifki
		System.out.println("Rifki Benchmark");
		String folderName = "C:\\\\Users\\\\m-zim\\\\Desktop\\\\Masterarbeit\\\\Benchmarks\\\\TDTSPBenchmark_Rifki";
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		stringListOfFiles = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    stringListOfFiles[i] = listOfFiles[i].getName();
		  }
		}
		// !!
		stringListOfFiles = new String[1];
		stringListOfFiles[0] = "inst-11-6-1-D100.txt";
		
		for(String fileName:stringListOfFiles) {
			System.out.println("\n" + fileName);
			DataReading dataReading = new DataReading(folderName + "\\" + fileName);
			distanceFct = dataReading.getDistanceFctRifki();
			nbLocations = dataReading.getnbLocations();
			nbTimesteps = dataReading.getnbTimesteps();
			durationTimestep = dataReading.getdurationTimestep();
			
			// General TD-TSP
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			// test
			
			int[] helperPath  = {5, 0, 6, 3, 7, 2, 4, 9, 10, 1, 8, 5};
			totalDuration = 0;
			currentTimestep = 0;
			for(int m = 0; m < helperPath.length -1; m++) {
				currentTimestep = (int) (totalDuration / durationTimestep);
				totalDuration += distanceFct[helperPath[m]][helperPath[m + 1]][currentTimestep];
			}
			System.out.println(Arrays.toString(helperPath) + " , teeeeeeeeeeeeeest duration: "+ totalDuration);
			// test
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
		}
		
		// Normal TSP
		
		folderName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TSPBenchmarks";
		folder = new File(folderName);
		listOfFiles = folder.listFiles();
		stringListOfFiles = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    stringListOfFiles[i] = listOfFiles[i].getName();
		  }
		}

		for(String fileName:stringListOfFiles) {
			System.out.println("\n" + fileName);
			DataReading dataReading = new DataReading(folderName + "\\" + fileName);
			distanceFctTimeIndependent = dataReading.getDistanceFctTimeIndependent();
			nbLocations = dataReading.getnbLocations();
			nbTimesteps = 1;
			durationTimestep = Integer.MAX_VALUE;
			
			distanceFct = new double[nbLocations][nbLocations][1];
			for(int i = 0; i<nbLocations;i++) {
				for(int j = 0; j<nbLocations;j++) {
					distanceFct[i][j][0] = distanceFctTimeIndependent[i][j];
				}
			}
			
			// General TD-TSP
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			/*
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
			*/
		}
		
		
		/*
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
		bestResult = 999999;
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
	
	public static void doNearestNeighbor() {
		System.out.println("Iterated nearest neighbor algorithm");
		Arrays.fill(tspPath, -1);
		int startingCity;
		double neighborTime;
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

		saveInCSV(fileName,"Iterated nearest neighbor algorithm",bestResult,bestResultPath);
	}
	
	public static void doNearestInsertion() {
		System.out.println("Nearest insertion algorithm");	
		Arrays.fill(tspPath, -1);
		double compare = 99999;
		totalDuration = 0;
		currentTimestep = 0;
		// find first insertion

		for(int i = 0; i < cities.length; i++) 
			for(int j = 0; j < cities.length; j++)
				if(i != j) {
					if(distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep)] < compare) {
						tspPath[0] = cities[i];
						tspPath[1] = cities[j];
						tspPath[2] = cities[i];
						compare = distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimestep)];
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
					
					for(int n = 0; n <= step; n++) 
						if(cities[j] == testPath[n]) 
							duplicate = true;
					
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
		saveInCSV(fileName,"Nearest insertion algorithm",bestDuration,bestPath);
	}
	
	static void doSavingsAlgo() {
		System.out.println("Iterated Savings Algorithm");
		int[] bestPathAllIterations = new int[cities.length +1];
		double bestTimeAllIterations = Double.MAX_VALUE;
		for(int depot : cities) {
			int[] savingsPath = new int[cities.length + cities.length-1];
			int counter = 0;
			savingsPath[0]= depot;
			for(int i = 1;i<savingsPath.length-1;i+=2){
				if(cities[counter] != depot) {
					savingsPath[i] = cities[counter];
					savingsPath[i+1] = depot;
				} else {
					i -= 2;
				}
				counter++;
			}
			int totalDuration = 0;
			int cycleDuration = 0;
			int currentTimestep = 0;
			for(int j = 0; j < savingsPath.length -1; j++) {
				if(savingsPath[j]== depot)
					cycleDuration = 0;
				currentTimestep = cycleDuration / durationTimestep;
				totalDuration += distanceFct[savingsPath[j]][savingsPath[j + 1]][currentTimestep];
				cycleDuration += distanceFct[savingsPath[j]][savingsPath[j + 1]][currentTimestep];
			}
			while(savingsPath.length > cities.length + 1) {
				bestResult = 999999;
				int[] helperCycle;
				int[] helperPath = new int[savingsPath.length -1];
				//find best saving and insert
				for(int i=0;i<helperPath.length;i++){
					if(savingsPath[i] == depot) {
						int j = i + 1;
						counter = 0;
						while(savingsPath[j] != depot) {
							counter++;
							j++;
						}
						helperCycle = new int[counter];
						for(int m= i+1;m<j;m++) {
							helperCycle[m-i-1] = savingsPath[m];
						}
						
						counter = 0;
						// copy array without detected cycle
						Arrays.fill(helperPath, depot);
						
						for(int m = 0;m<savingsPath.length;m++) {
							if(m<i || m > i + helperCycle.length) {
								helperPath[counter] = savingsPath[m];
								counter++;
							}
						}
						
						// Insert at every position + evaluate (not first + not last spot)
						int[] helperHelperPath = new int[helperPath.length];
						for(int m=0;m<helperPath.length;m++) {
							helperHelperPath[m] = helperPath[m];
						}
						for(int indexPosition = 1;indexPosition < helperPath.length-1-helperCycle.length;indexPosition++) {
							for(int m=0;m<helperPath.length;m++) {
								helperPath[m] = helperHelperPath[m];
							}
							for(int m=helperPath.length-1; m > indexPosition && m > helperCycle.length; m--){
								helperPath[m] = helperPath[m-helperCycle.length];
							}
							for(int n=0;n<helperCycle.length;n++) {
								helperPath[indexPosition + n] = helperCycle[n]; 
							}
							
							//Calculate total duration
							totalDuration = 0;
							cycleDuration = 0;
							currentTimestep = 0;
							for(int m = 0; m < helperPath.length -1; m++) {
								if(helperPath[m]==depot)
									cycleDuration = 0;
								currentTimestep = cycleDuration / durationTimestep;
								totalDuration += distanceFct[helperPath[m]][helperPath[m + 1]][currentTimestep];
								cycleDuration += distanceFct[helperPath[m]][helperPath[m + 1]][currentTimestep];
							}
							//System.out.println(Arrays.toString(helperPath) + " total duration: "+ totalDuration);
														
	 						if (bestResult > totalDuration) {
								bestResult = totalDuration;
								bestResultPath = new int[helperPath.length];
								for(int m=0;m<bestResultPath.length;m++) {
									bestResultPath[m] = helperPath[m];
								}
							}

						}
					}
				}
				savingsPath = new int[bestResultPath.length];
				for(int m = 0;m <savingsPath.length;m++) {
					savingsPath[m] = bestResultPath[m];
				}
			}
			System.out.println(Arrays.toString(bestResultPath) + " total duration: " + bestResult);
			if (bestResult < bestTimeAllIterations) {
				bestTimeAllIterations = bestResult;
				for(int m=0;m<bestResultPath.length;m++) {
					bestPathAllIterations[m] = bestResultPath[m];
				}
			}
		// Compare overall
		}
		System.out.println(Arrays.toString(bestPathAllIterations) + " best duration: "+ bestTimeAllIterations);
		saveInCSV(fileName,"Savings algorithm",bestTimeAllIterations,bestPathAllIterations);
		
	}
	
	static void doChristofidesAlgorithm() {
		System.out.println("Christofides Algorithm");
		// transform matrix
		double[][] distanceFctTimeindependent = new double[nbLocations][nbLocations];
		
		for(int i = 0;i<nbLocations;i++) {
			for(int j = 0;j<nbLocations;j++) {
				for(int t = 0;t<nbTimesteps;t++) {
					distanceFctTimeindependent[i][j] += distanceFct[i][j][t];
				}
				distanceFctTimeindependent[i][j] = distanceFctTimeindependent[i][j] / nbTimesteps;
			}
		}
		
		for(int i = 0;i<nbLocations;i++) {
			for(int j = 0;j<nbLocations;j++) {
				if(i != j) {
					distanceFctTimeindependent[i][j] = (distanceFctTimeindependent[i][j] + distanceFctTimeindependent[j][i]) /2;
					distanceFctTimeindependent[j][i] = distanceFctTimeindependent[i][j];
				}
			}
		}
		
		for(int i = 0;i<nbLocations;i++) {
			//System.out.println(Arrays.toString(distanceFctTimeindependent[i]));
		}
		// calculate minimum spanning tree (Kruskal)
		
		int[] mst = new int[cities.length];
		int[] mstIn = new int[cities.length];
		Arrays.fill(mst, -1);
		
		for(int k = 0;k<mst.length -1;k++) {
			double compare = 999999;
			for(int i = 0;i<nbLocations;i++) {
				for(int j = 0;j<nbLocations;j++) {
					if(distanceFctTimeindependent[i][j] < compare && i !=j) {
						boolean duplicate = false;
						for(int m = 0;m<k+1;m++) {
							if(mst[m] == j) {
								duplicate = true;
							}
						}
						
						if(!duplicate){
							compare = distanceFctTimeindependent[i][j];
							if(k == 0)
								mst[k] = i;
							mst[k+1] = j; 
							mstIn[k+1] = i;
						}
					}
				}
			}
			// ??? Add to graph?? or store differently?	
		}
		//System.out.println(Arrays.toString(mst));
		//System.out.println(Arrays.toString(mstIn));
		
		// calculate odd degree vertices (Floyd-Warshall)
		
		//
	}
	
	
	
	
	// Only for Mlegarejo permutation
    static void permute(java.util.List<Integer> arr, int k){
    	int[] solutionMelgarejo = {10,222,190,81,32,9,132,240 ,74 ,127, 150,10};
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            //System.out.println(java.util.Arrays.toString(arr.toArray()));
            for(int m = 1;m < solutionMelgarejo.length-1;m++) {
            	solutionMelgarejo[m] = arr.get(m-1);
            }
    		int TtotalDuration = 0;
    		int TcurrentTimestep = 0;
    		for(int j = 0; j < solutionMelgarejo.length -1; j++) {
    			TcurrentTimestep = TtotalDuration / durationTimestep;
    			TtotalDuration += distanceFct[solutionMelgarejo[j]][solutionMelgarejo[j + 1]][TcurrentTimestep];
    			//System.out.println("from: " + solutionMelgarejo[j] + " to: "+ solutionMelgarejo[j + 1] + " in Timestep: " + TcurrentTimestep + " takes " + distanceFct[solutionMelgarejo[j]][solutionMelgarejo[j + 1]][TcurrentTimestep]);
    		}
    		if(TtotalDuration < bestDuration) {
    			bestDuration = TtotalDuration;
    			System.out.println("New best duration: " + bestDuration);
    			System.out.println(Arrays.toString(solutionMelgarejo));
    		}
        }
        //System.out.println("Final best duration: " + bestDuration);
    }
}
