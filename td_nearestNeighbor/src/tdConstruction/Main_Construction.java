package tdConstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main_Construction {
	
	static int nbLocations = 0;
	static int nbTimeSteps = 0;
	static int durationTimeStep = 0;
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
	
	static double bestDuration = 999999;
	static String[] stringListOfFiles;
	static int counter;
	static boolean isCordeau = false;
	static int[] serviceTime;
	public static double[][] distanceFctTimeindependent;
	
	static List<Integer> mst;
	static List<Integer> mstIn;
	
	//from FIFO
	private static FIFOTimeStep[][][] FIFODistanceFct;

	
	public static void main(String[]args) throws FileNotFoundException {

/*
		// Cordeau
		isCordeau = true;
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
		
		stringListOfFiles = new String[1];
		stringListOfFiles[0] = "15\\15ANodi_1";
		for (String name :stringListOfFiles) {
			System.out.println("\n"+name);
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\"+name;
			
			
			DataReading dataReading = new DataReading(fileName);
			distanceFct = dataReading.getDistanceFctCordeau();
			nbLocations = dataReading.getnbLocations();
			nbTimeSteps = dataReading.getnbTimeSteps();
			durationTimeStep = dataReading.getdurationTimeStep();
			setFIFODistanceFct();	
						
			// Compare with given results from Cordeau
			//15ANodi_1
			int[] solutionCordeau = {0, 6 ,15 ,14, 3 ,1 ,11, 8, 2, 12, 4 ,7, 9, 13 ,10, 5, 0};
			//15CNodi_1
			//int[] solutionCordeau = {0, 7, 2, 14, 15, 13, 3, 12, 9, 8, 1, 5, 11, 6, 10, 4, 0};
			double tTotalDuration = 0;
			int tCurrentTimestep = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				tCurrentTimestep = (int) (tTotalDuration / durationTimeStep);
				tTotalDuration += distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][tCurrentTimestep];
				//System.out.println("from: " + solutionCordeau[k] + " to: "+ solutionCordeau[k + 1] + " in Timestep: " + tCurrentTimestep + " takes " + distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][tCurrentTimestep] + " total " + tTotalDuration );
			}
			System.out.println("total duration without FIFO: " + tTotalDuration);
			
			tTotalDuration = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				tTotalDuration += getFIFOTravellingTime(solutionCordeau[k],solutionCordeau[k + 1],tTotalDuration);
			}
			System.out.println("total duration with FIFO: " + tTotalDuration);
			// General TD-TSP
			cities = new int[nbLocations-1];
			for(int i=0;i<nbLocations-1;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			//doChristofidesAlgorithm();
		}
		isCordeau = false;
		*/
		
		
		// Melgarejo
		fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Melgarejo\\Matrices\\matrix00.txt";				
		DataReading dataReading = new DataReading(fileName);
		distanceFct = dataReading.getDistanceFctMelgarejo();
		nbLocations = dataReading.getnbLocations();
		nbTimeSteps = dataReading.getnbTimeSteps();
		durationTimeStep = dataReading.getdurationTimeStep();
		
		// Get instances
	
		int[] numbersMelgarejo = {10,20,30,50,100};
		counter = 0;
		stringListOfFiles = new String[220];
		String folderName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Melgarejo\\Instances\\Instances_noTW\\";
		
		for(int j=0;j<numbersMelgarejo.length;j++) {
			File folder = new File(folderName+Integer.toString(numbersMelgarejo[j]));
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
			  if (listOfFiles[i].isFile()) {
			    stringListOfFiles[counter] = Integer.toString(numbersMelgarejo[j]) +"\\"+ listOfFiles[i].getName();
			    counter++;
			  }
			}
		}
		counter=0;
		
		stringListOfFiles = new String[1];
		stringListOfFiles[0] = "10\\inst_10_1.txt";
		
		for(String name : stringListOfFiles) {
			System.out.println(name);
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Melgarejo\\Matrices\\matrix00.txt";				
			dataReading = new DataReading(fileName);
			distanceFct = dataReading.getDistanceFctMelgarejo();
			file = new Scanner(new File(folderName+name));
			file.useLocale(Locale.US);
			cities = new int[file.nextInt()];
			serviceTime = new int[cities.length];
			for(int i = 0;i<cities.length;i++) {
				file.nextLine();
				cities[i] = file.nextInt();
				serviceTime[i] = file.nextInt();
			}
			file.close();
			
			
			
			// include FIFO
			setFIFODistanceFct();			
			//System.out.println(distanceFct[0][1][10] + "  " + getFIFOTravellingTime(0, 1,3955));
			
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();

			// Compare with results from instances
			counter = 0;
			//permute(java.util.Arrays.asList(190,81,32,9,132,240 ,74 ,127, 150),0);
			
			// best solution from permutation for 10_1
			//int[] solution = {222, 9, 132, 127, 150, 190, 32, 81, 74, 240, 222};
			
			int[] solution = {0,4,5,8,9,1,3,7,2,6,0};
			
			double tTotalDuration = 0;
			int tCurrentTimestep = 0;
			for(int k = 0; k < solution.length -1; k++) {
				tCurrentTimestep = (int) (tTotalDuration / durationTimeStep);
				tTotalDuration += distanceFct[cities[solution[k]]][cities[solution[k + 1]]][tCurrentTimestep] + serviceTime[solution[k]];
			}
			//System.out.println("total duration without FIFO: " + tTotalDuration);

			tTotalDuration = 0;
			for(int k = 0; k < solution.length -1; k++) {
				tTotalDuration += getFIFOTravellingTime(solution[k],solution[k + 1],tTotalDuration);
			}
			//System.out.println("total duration with FIFO: " + tTotalDuration);

		}
		
		
		/*
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
			nbTimeSteps = dataReading.getnbTimeSteps();
			durationTimeStep = dataReading.getdurationTimeStep();
			
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
			doSavingsAlgo();
			doChristofidesAlgorithm();
		}
		*/
		// Normal TSP
		/*
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
			nbTimeSteps = 1;
			durationTimeStep = Integer.MAX_VALUE;
			
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
			
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
			
		}
		*/
	}
	
	public static void saveInCSV(String fileName, String heuristic,double bestResult, int[]bestResultPath, int time) {
		try {
			FileWriter writer = new FileWriter("test.csv",true);
			writer.append("\n"+fileName+";"+heuristic+";"+(int)bestResult+";"+Arrays.toString(bestResultPath)+";"+ time);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void doFirstFit() {
		LocalTime timeStart = LocalTime.now();
		bestResult = 999999;
		int[] originalCities = new int[cities.length];
		for(int m = 0; m < cities.length; m++) {
			originalCities[m] = cities[m];
		}
		
		
		System.out.println("First-Fit algorithm");
		//change to l<cities.length to treat every vertex as possible depot
		for(int l = 0; l < 1; l++) {
			totalDuration = 0;
			for(int k = 0; k < cities.length -1; k++) {
				currentTimestep = (int) (totalDuration / durationTimeStep);
				// for Cordeau
				if(currentTimestep > nbTimeSteps -1) {
					currentTimestep = nbTimeSteps -1;
				}
				// totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
				totalDuration += getFIFOTravellingTime(k,k+1,totalDuration);
			}
			currentTimestep = (int) (totalDuration / durationTimeStep);
			// for Cordeau
			if(currentTimestep > nbTimeSteps -1) {
				currentTimestep = nbTimeSteps -1;
			}
			totalDuration += getFIFOTravellingTime(cities.length -1,0,totalDuration);

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
		
		//Get original cities
		
		for(int m = 0; m < cities.length; m++) {
			cities[m] = originalCities[m];
		}
		
		
		System.out.println(Arrays.toString(bestResultPath) + " objective value: " + bestResult);
		
		LocalTime timeEnd = LocalTime.now();
		
		
		//Mikro
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;	
		saveInCSV(fileName,"First-Fit algorithm",bestResult,bestResultPath,computingTime);
				  
		
	}
	
	public static void doNearestNeighbor() {
		LocalTime timeStart = LocalTime.now();
		System.out.println("Nearest neighbor algorithm");
		double neighborTime;
		bestResult = 999999;
		int[] tspPathSolution = new int[tspPath.length];
		tspPath[0] = 0;
		tspPath[tspPath.length - 1] = 0;
		tspPathSolution[0] = cities[0];
		tspPathSolution[tspPath.length - 1] = cities[0];
		
		totalDuration = 0;
		List<Integer> remainingCities = new LinkedList<>();
		for(int i = 1;i<cities.length;i++) {
			remainingCities.add(i);
		}
		
		for(int i = 1;i<cities.length;i++) {
			neighborTime = 99999;
			List<Integer> nextCity = new LinkedList<>();
			for(int j : remainingCities) {
				if (getFIFOTravellingTime(0,j,totalDuration) < neighborTime){;
					tspPath[i] = j;
					tspPathSolution[i] = cities[j];
					neighborTime = getFIFOTravellingTime(0,j,totalDuration);
				}
			}
			nextCity.add(tspPath[i]);
			remainingCities.removeAll(nextCity);
			totalDuration += getFIFOTravellingTime(tspPath[i-1],tspPath[i],totalDuration);
		}
		totalDuration += getFIFOTravellingTime(tspPath[tspPath.length-2],tspPath[tspPath.length-1],totalDuration);

		System.out.println(Arrays.toString(tspPathSolution)+ " objective value: " + totalDuration);

		LocalTime timeEnd = LocalTime.now();
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos() /1000;
		saveInCSV(fileName,"Nearest neighbor algorithm",totalDuration,tspPathSolution,computingTime);
	}
	
	public static void doNearestInsertion() {
		System.out.println("Nearest insertion algorithm");
		LocalTime timeStart = LocalTime.now();
		int[] tspPathSolution = new int[tspPath.length];
		double compare = 99999;
		totalDuration = 0;
		currentTimestep = 0;
		for(int j = 0; j < cities.length; j++)
				if(0 != j) {
					if(getFIFOTravellingTime(0,j,totalDuration) + getFIFOTravellingTime(j,0,totalDuration) < compare) {	
						tspPath[0] = 0;
						tspPath[1] = j;
						tspPath[2] = 0;
						tspPathSolution[0] = cities[0];
						tspPathSolution[1] = cities[j];
						tspPathSolution[2] = cities[0];
						compare = getFIFOTravellingTime(0,j,totalDuration) + getFIFOTravellingTime(j,0,totalDuration);
					}
				}
		
		totalDuration = compare;
		step = 2;		
		double pathDuration = 0;
		for(int i = 0; i < step;i++) {
			pathDuration += getFIFOTravellingTime(tspPath[i],tspPath[i+1],pathDuration);
		}
		
		// following insertions
		int[] testPath = new int[tspPath.length];
		int[] bestPath = new int[tspPath.length];
		double bestDuration = 0;
		
		while(step < tspPath.length) {
			//change to i = 0 to treat every vertex as possible depot
			for(int i = 1; i <= step; i++) {
				compare = 99999;
				for(int j = 0; j < cities.length; j++) {
					for(int k = 0; k <= step; k++) {
						testPath[k] = tspPath[k];
					}
					// Check for duplicate
					duplicate = false;
					for(int n = 0; n <= step; n++) 
						if(j == testPath[n]) 
							duplicate = true;
					
					if(!duplicate){
						for(int l = step; l >= i; l--) {
							testPath[l+1] = testPath[l];
						}
						testPath[i] = j;
						testPath[step+1] = testPath[0];
						pathDuration = 0;
						for(int m = 0; m <= step;m++) {
							// for Melgarejo
							if (pathDuration/durationTimeStep >= nbTimeSteps) {
								pathDuration += getFIFOTravellingTime(testPath[m],testPath[m+1],nbTimeSteps-1);
							} else {
								pathDuration += getFIFOTravellingTime(testPath[m],testPath[m+1],pathDuration);
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
				// for Melgarejo
				if (pathDuration/durationTimeStep >= nbTimeSteps) {
					pathDuration += getFIFOTravellingTime(bestPath[m],bestPath[m+1],nbTimeSteps-1);
				} else {
					pathDuration += getFIFOTravellingTime(bestPath[m],bestPath[m+1],pathDuration);

				}
			}
			step++;
		}
		for(int i = 0;i<tspPath.length;i++)
			tspPathSolution[i] = cities[bestPath[i]];
		System.out.println(Arrays.toString(tspPathSolution) + " objective value: " + bestDuration);
		LocalTime timeEnd = LocalTime.now();
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;
		saveInCSV(fileName,"Nearest insertion algorithm",bestDuration,tspPathSolution,computingTime);
	}
	
	static void doSavingsAlgo() {
		LocalTime timeStart = LocalTime.now();
		System.out.println("Savings Algorithm");
		int[] savingsPath = new int[cities.length + cities.length-1];
		int counter = 1;
		savingsPath[0]= 0;
		for(int i = 1;i<savingsPath.length-1;i+=2){
			savingsPath[i] = counter;
			savingsPath[i+1] = 0;
			counter++;
		}
		double totalDuration = 0;
		double cycleDuration = 0;
		for(int j = 0; j < savingsPath.length -1; j++) {
			if(savingsPath[j]== 0)
				cycleDuration = 0;
			totalDuration += getFIFOTravellingTime(savingsPath[j],savingsPath[j + 1],cycleDuration);
			cycleDuration += getFIFOTravellingTime(savingsPath[j],savingsPath[j + 1],cycleDuration);
		}
		while(savingsPath.length > cities.length + 1) {
			bestDuration = 999999;
			int[] helperCycle;
			int[] helperPath = new int[savingsPath.length -1];
			for(int i=0;i<helperPath.length;i++){
				if(savingsPath[i] == 0) {
					int j = i + 1;
					counter = 0;
					while(savingsPath[j] != 0) {
						counter++;
						j++;
					}
					helperCycle = new int[counter];
					for(int m= i+1;m<j;m++) {
						helperCycle[m-i-1] = savingsPath[m];
					}
					
					counter = 0;
					// copy array without detected cycle
					Arrays.fill(helperPath, 0);
					
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
						for(int m = 0; m < helperPath.length -1; m++) {
							if(helperPath[m]==0)
								cycleDuration = 0;
							totalDuration += getFIFOTravellingTime(helperPath[m],helperPath[m + 1],cycleDuration);
							cycleDuration += getFIFOTravellingTime(helperPath[m],helperPath[m + 1],cycleDuration);

						}
						
 						if (totalDuration < bestDuration) {
							bestDuration = totalDuration;
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
		
		for(int i = 0;i< bestResultPath.length;i++) {
			bestResultPath[i] = cities[bestResultPath[i]];
		}
		System.out.println(Arrays.toString(bestResultPath) + " objective value: "+ bestDuration);
		LocalTime timeEnd = LocalTime.now();
		
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;
		saveInCSV(fileName,"Savings algorithm",bestDuration,bestResultPath,computingTime);
		
	}
	
	static void doChristofidesAlgorithm() {
		System.out.println("Christofides Algorithm");
		LocalTime timeStart = LocalTime.now();

		// transform matrix
		distanceFctTimeindependent = new double[nbLocations][nbLocations];
		
		// Get average
		/*
		for(int i = 0;i<nbLocations;i++) {
			for(int j = 0;j<nbLocations;j++) {
				for(int t = 0;t<nbTimeSteps;t++) {
					distanceFctTimeindependent[i][j] += distanceFct[i][j][t];
				}
				distanceFctTimeindependent[i][j] = distanceFctTimeindependent[i][j] / nbTimeSteps;
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
		*/
		// Get median for distanceFct
		
		for(int i = 0;i<nbLocations;i++) {
			for(int j = 0;j<nbLocations;j++) {
				double[] listMedian = new double[nbTimeSteps*2];
				for(int t = 0;t<nbTimeSteps;t++) {
					listMedian[t] = distanceFct[i][j][t];
					listMedian[t+nbTimeSteps] = distanceFct[j][i][t];
				}
				Arrays.sort(listMedian);
				distanceFctTimeindependent[i][j] = (listMedian[nbTimeSteps] + listMedian[nbTimeSteps+1])/2;
			}
		}
		
		// calculate minimum spanning tree (Kruskal)
		mst = new LinkedList<>();
		mstIn = new LinkedList<>();
		
		for(int k = 0;k<cities.length -1;k++) {
			double compare = 999999;
			int bestI = -2;
			int bestJ = -2;
			for(int i = 0;i<cities.length;i++) {
				for(int j = 0;j<cities.length;j++) {
					if(distanceFctTimeindependent[cities[i]][cities[j]] < compare && i !=j) {
						
						Graph graph = new Graph(cities.length);
					    for(int m = 0; m<mst.size();m++) {
					    	graph.addEdge(mstIn.get(m), mst.get(m));
					    }
					    graph.addEdge(i, j);
						
						if(!graph.isCyclic()){
							compare = distanceFctTimeindependent[cities[i]][cities[j]];
							bestI = i;
							bestJ = j;

						}
					}
				}
			}
			mst.add(bestJ); 
			mstIn.add(bestI);
		}
		int[] perfectMatching = new int[counter];
		
		// !!!Needs to be done!!!
		doPerfectMatchingPermutation();		
		
		// Find Vertices with more than 2 edges
		
		List<Integer> needShortcut = new LinkedList<>();
		counter = 0;
		for(int i = 0;i<=mst.size();i++) {
			int helpCounter = 0;
			for(int j = 0;j<mst.size();j++) {
				if(i == mst.get(j)) 
					helpCounter++;
				if(i == mstIn.get(j)) 
					helpCounter++;
			}
			if(helpCounter > 2) {
				counter++;
				needShortcut.add(i);
			}
		}		
		
		while(!needShortcut.isEmpty()) {
			double compare = 999999;
			int bestI = -1;
			int bestJ = -1;
			int bestV = -1;
			for(int vertex: needShortcut) {
				List<Integer> connectedV = new LinkedList<>();
				// get all the connected vertices
				for(int i = 0;i<mst.size();i++) {
					if(mst.get(i) == vertex)
						connectedV.add(mstIn.get(i));
					if(mstIn.get(i) == vertex)
						connectedV.add(mst.get(i));
				}				
				// try shortcut between all of them and save the best saving so far and bestI bestJ deleteI deleteJ
				
				for(int i = 0;i<connectedV.size();i++) {
					for(int j = i+1;j<connectedV.size();j++) {
						if(distanceFctTimeindependent[cities[i]][cities[j]] - distanceFctTimeindependent[cities[vertex]][cities[j]] - distanceFctTimeindependent[cities[i]][vertex] < compare) {
							compare = distanceFctTimeindependent[cities[i]][cities[j]] - distanceFctTimeindependent[cities[vertex]][cities[j]] - distanceFctTimeindependent[cities[i]][vertex];
							bestV = vertex;
							bestI = i;
							bestJ = j;
						}
					}
				}
			}
			List<Integer> shortcutV = new LinkedList<>();
			for(int i = 0;i<mst.size();i++) {
				if(mst.get(i) == bestV)
					shortcutV.add(mstIn.get(i));
				if(mstIn.get(i) == bestV)
					shortcutV.add(mst.get(i));
			}
			
			// remove the two unnecessary arcs
			int[] removeList = new int[2];
			counter = 0;
			for(int i = 0; i < mst.size();i++) {
				if((mst.get(i) == shortcutV.get(bestI) && mstIn.get(i) == bestV) 
						||(mst.get(i) == bestV && mstIn.get(i) == shortcutV.get(bestI)) 
						|| (mst.get(i) == shortcutV.get(bestJ) && mstIn.get(i) == bestV) 
						||(mst.get(i) == bestV && mstIn.get(i) == shortcutV.get(bestJ))) {
					removeList[counter++] = i;
				}
			}
			
			mst.remove(removeList[1]);
			mstIn.remove(removeList[1]);
			mst.remove(removeList[0]);
			mstIn.remove(removeList[0]);
			
			
			mst.add(shortcutV.get(bestI));
			mstIn.add(shortcutV.get(bestJ));
			
			needShortcut = new LinkedList<>();
			counter = 0;
			for(int i = 0;i<=mst.size();i++) {
				int helpCounter = 0;
				for(int j = 0;j<mst.size();j++) {
					if(i == mst.get(j)) 
						helpCounter++;
					if(i == mstIn.get(j)) 
						helpCounter++;
				}
				if(helpCounter > 2) {
					counter++;
					needShortcut.add(i);
				}
			}
		}
		
		int[] solution = new int[cities.length +1];
		int remove = -1;
		solution[0] = 0;
		solution[solution.length - 1] = 0;
		
		for(int i = 0;i<cities.length-1;i++){
			for(int j = 0;j<mst.size();j++){
				if(solution[i] == mst.get(j)) {
					solution[i+1] = mstIn.get(j);
					remove = j;
				}
				if(solution[i] == mstIn.get(j)) {
					solution[i+1] = mst.get(j);
					remove = j;
				}
			}
			mst.remove(remove);
			mstIn.remove(remove);
		}
		
		totalDuration = 0;
		for(int k = 0; k < solution.length -1; k++) {
			totalDuration += getFIFOTravellingTime(solution[k],solution[k + 1],totalDuration);
		}
		LocalTime timeEnd = LocalTime.now();
		
		for(int i = 0;i < solution.length;i++)
			solution[i] = cities[solution[i]];
		System.out.println(Arrays.toString(solution) + " objective value: "+ totalDuration);

		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;
		saveInCSV(fileName,"Christofides algorithm",totalDuration,solution,computingTime);
	}
	

	
	static void doPerfectMatchingPermutation() {
		
		// calculate nb of odd degree vertices
		List<Integer> needMatching = new LinkedList<>();
		counter = 0;
		for(int i = 0;i<=mst.size();i++) {
			int helpCounter = 0;
			for(int j = 0;j<mst.size();j++) {
				if(i == mst.get(j)) 
					helpCounter++;
				if(i == mstIn.get(j)) 
					helpCounter++;
			}
			if(helpCounter%2 == 1) {
				counter++;
				needMatching.add(i);
			}
		}
		
		//!!! Need perfect matching
		
		while(needMatching.size() !=0) {
			mst.add(needMatching.get(0));
			mstIn.add(needMatching.get(1));
			needMatching.remove(needMatching.get(0));
			needMatching.remove(needMatching.get(0));
		}
		

	}
	    
    
	/**
	 * Transform the distance function into one that respects the FIFO (First In First Out) property
	 */
	public static void setFIFODistanceFct() {

		FIFODistanceFct = new FIFOTimeStep[nbLocations][nbLocations][nbTimeSteps];
		for(int i=0;i<nbLocations;i++) {
			for(int j=0;j<nbLocations;j++) {
				for(int s=0;s<nbTimeSteps;s++) {
					if(s==0) {
						if(getTravellingTimeTimeStep(i, j, s)>getTravellingTimeTimeStep(i, j, (s+1))) {
							FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
							LinearFunction f = new LinearFunction(getTravellingTimeTimeStep(i, j, (s+1)),(s+1)*durationTimeStep);
							fts.setLinearFunction(f);
							FIFODistanceFct[i][j][s]=fts;
						}
						else {
							FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
							FIFODistanceFct[i][j][s]=fts;
						}
					}
					if(s>0 && s<nbTimeSteps-1) {
						if(getTravellingTimeTimeStep(i, j, s)>getTravellingTimeTimeStep(i, j, (s+1))) {
							FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
							LinearFunction f = new LinearFunction(getTravellingTimeTimeStep(i, j, (s+1)),(s+1)*durationTimeStep);
							fts.setLinearFunction(f);
							FIFODistanceFct[i][j][s]=fts;
							for (int sprime=0; sprime<s;sprime++) {
								if(f.getB()<FIFODistanceFct[i][j][sprime].getF().getB()) {
									FIFODistanceFct[i][j][sprime].setLinearFunction(f);
								}
							}
						}
						else {
							FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
							FIFODistanceFct[i][j][s]=fts;
						}
					}
					if(s==nbTimeSteps-1) {
						FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
						FIFODistanceFct[i][j][s]=fts;
					}
				}
			}
		}
	}
	
	public static double getTravellingTimeTimeStep(int i, int j, int s) {
		return distanceFct[i][j][s];
	}
	
	public static double getFIFOTravellingTime(int i, int j, double time) {
		if(isCordeau) {
			double partOfWay = 1;
			double timeInIntervall = 0;
			double FIFOTravellingTime = 0;
			while((int)((distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)]*partOfWay + time)/durationTimeStep) > (int) (time/durationTimeStep) ) {
				timeInIntervall = ((int) (time/durationTimeStep)+1)*durationTimeStep - time;
				partOfWay -= timeInIntervall / (int)(distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)]);
				time += timeInIntervall;
				FIFOTravellingTime += timeInIntervall;
			}
			FIFOTravellingTime += partOfWay * distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)];
			return FIFOTravellingTime;
		} else {
			return FIFODistanceFct[cities[i]][cities[j]][(int) ((time + serviceTime[i])/durationTimeStep)].getCost(time) + serviceTime[i];

		}
	}
}
