package tdConstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
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
	
	//from FIFO
	private static FIFOTimeStep[][][] FIFODistanceFct;

	
	public static void main(String[]args) throws FileNotFoundException {


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
			int[] solutionCordeau = {0, 6 ,15 ,14, 3 ,1 ,11, 8, 2, 12, 4 ,7, 9, 13 ,10, 5, 0};
			double tTotalDuration = 0;
			int tCurrentTimestep = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				tCurrentTimestep = (int) (tTotalDuration / durationTimeStep);
				tTotalDuration += distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][tCurrentTimestep];
				//System.out.println("from: " + solutionCordeau[k] + " to: "+ solutionCordeau[k + 1] + " in Timestep: " + tCurrentTimestep + " takes " + distanceFct[solutionCordeau[k]][solutionCordeau[k + 1]][tCurrentTimestep] + " total " + tTotalDuration );
			}
			System.out.println("total duration: " + tTotalDuration);
			
			tTotalDuration = 0;
			for(int k = 0; k < solutionCordeau.length -1; k++) {
				tTotalDuration += getFIFOTravellingTime(solutionCordeau[k],solutionCordeau[k + 1],tTotalDuration);
			}
			System.out.println("total duration: " + tTotalDuration);
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
		
		
		/*
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
			int[] serviceTime = new int[cities.length];
			for(int i = 0;i<cities.length;i++) {
				file.nextLine();
				cities[i] = file.nextInt();
				serviceTime[i] = file.nextInt();
			}
			file.close();
			
			for(int i = 0;i<cities.length;i++)
				for(int j = 0;j<serviceTime.length;j++) 
					for(int t = 0;t<nbTimeSteps;t++) 
						distanceFct[cities[i]][cities[j]][t] +=serviceTime[j];
			
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
			//doChristofidesAlgorithm();

			// Compare with results from instances
			counter = 0;
			//permute(java.util.Arrays.asList(190,81,32,9,132,240 ,74 ,127, 150),0);
			
			// best solution from permutation for 10_1
			int[] solution = {222, 9, 132, 127, 150, 190, 32, 81, 74, 240, 222};
			// 10_12 for savings
			//int[] solution = {35, 120, 112, 68, 70, 210, 208, 149, 147, 150, 35};
			double tTotalDuration = 0;
			int tCurrentTimestep = 0;
			for(int k = 0; k < solution.length -1; k++) {
				tCurrentTimestep = (int) (tTotalDuration / durationTimeStep);
				tTotalDuration += distanceFct[solution[k]][solution[k + 1]][tCurrentTimestep];
				System.out.println("from: " + solution[k] + " to: "+ solution[k + 1] + " in Timestep: " + tCurrentTimestep + " takes " + distanceFct[solution[k]][solution[k + 1]][tCurrentTimestep] + " total " + tTotalDuration );
			}
			System.out.println("total duration without FIFO: " + tTotalDuration);
			
			tTotalDuration = 0;
			for(int k = 0; k < solution.length -1; k++) {
				tTotalDuration += getFIFOTravellingTime(solution[k],solution[k + 1],tTotalDuration);
			}
			System.out.println("total duration with FIFO: " + tTotalDuration);

		}
		*/
		
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
				// for Melgarejo
				if(currentTimestep > nbTimeSteps -1) {
					currentTimestep = nbTimeSteps -1;
				}
				// totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
				totalDuration += getFIFOTravellingTime(cities[k],cities[k+1],totalDuration);
			}
			currentTimestep = (int) (totalDuration / durationTimeStep);
			// for Cordeau
			if(currentTimestep > nbTimeSteps -1) {
				currentTimestep = nbTimeSteps -1;
			}
			//totalDuration += distanceFct[cities[cities.length -1]][cities[0]][currentTimestep];
			totalDuration += getFIFOTravellingTime(cities[cities.length -1],cities[0],totalDuration);

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
		Arrays.fill(tspPath, -1);
		int startingCity;
		double neighborTime;
		bestResult = 999999;

		//change to l<cities.length to treat every vertex as possible depot
		for(int l = 0; l < 1; l ++) {
			startingCity = cities[l];
			Arrays.fill(tspPath, -1);
			tspPath[0] = cities[l];
			tspPath[tspPath.length - 1] = cities[l];
			step = 1;
			totalDuration = 0;
			
			while (step < cities.length) {
				neighborTime = 99999;
				currentTimestep = (int) (totalDuration / durationTimeStep);
				for (int j = 0; j < cities.length; j++ ) {
					duplicate = false;
					for (int k = 0; k < tspPath.length; k++) {
						if (cities[j] == tspPath[k]) {
							duplicate = true;
						}
					}
					//if (distanceFct[startingCity][cities[j]][currentTimestep] < neighborTime && !duplicate) {
					if (getFIFOTravellingTime(startingCity,cities[j],totalDuration) < neighborTime && !duplicate){;
						tspPath[step] = cities[j];
						//neighborTime = distanceFct[startingCity][cities[j]][currentTimestep];
						neighborTime= getFIFOTravellingTime(startingCity,cities[j],totalDuration);

					}
				}
				//totalDuration += distanceFct[startingCity][tspPath[step]][currentTimestep];
				totalDuration += getFIFOTravellingTime(startingCity,tspPath[step],totalDuration);
				startingCity = tspPath[step];
				step++;
			}
			currentTimestep = (int) (totalDuration / durationTimeStep);
			//totalDuration += distanceFct[tspPath[tspPath.length-2]][tspPath[tspPath.length-1]][currentTimestep];
			totalDuration += getFIFOTravellingTime(tspPath[tspPath.length-2],tspPath[tspPath.length-1],totalDuration);
			//System.out.println(Arrays.toString(tspPath)+ "total duration: " + totalDuration + ", current Timestep: " + currentTimestep);
			if(totalDuration < bestResult) {
				bestResult = totalDuration;
				for(int i = 0;i<bestResultPath.length;i++) {
					bestResultPath[i] = tspPath[i];
				}
			}
		}
		System.out.println(Arrays.toString(bestResultPath)+ " objective value: " + bestResult);

		LocalTime timeEnd = LocalTime.now();
		
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos() /1000;
		saveInCSV(fileName,"Nearest neighbor algorithm",bestResult,bestResultPath,computingTime);
	}
	
	public static void doNearestInsertion() {
		System.out.println("Nearest insertion algorithm");
		LocalTime timeStart = LocalTime.now();
		Arrays.fill(tspPath, -1);
		double compare = 99999;
		totalDuration = 0;
		currentTimestep = 0;
		// find first insertion
		//change to i < cities.length to treat every vertex as possible depot
		for(int i = 0; i < 1; i++) 
			for(int j = 0; j < cities.length; j++)
				if(i != j) {
					//if(distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimeStep)] < compare) {
					if(getFIFOTravellingTime(cities[i],cities[j],totalDuration) + getFIFOTravellingTime(cities[j],cities[i],totalDuration) < compare) {	
						tspPath[0] = cities[i];
						tspPath[1] = cities[j];
						tspPath[2] = cities[i];
						//compare = distanceFct[cities[i]][cities[j]][currentTimestep] + distanceFct[cities[j]][cities[i]][(int) (distanceFct[cities[i]][cities[j]][currentTimestep]/durationTimeStep)];
						compare = getFIFOTravellingTime(cities[i],cities[j],totalDuration) + getFIFOTravellingTime(cities[j],cities[i],totalDuration);
					}
				}
		
		totalDuration = compare;
		step = 2;		
		// flexible duration calculation
		double pathDuration = 0;
		
		for(int i = 0; i < step;i++) {
			//pathDuration += distanceFct[tspPath[i]][tspPath[i+1]][(int) (pathDuration/durationTimeStep)];
			pathDuration += getFIFOTravellingTime(tspPath[i],tspPath[i+1],pathDuration);
		}
		
		// try second insertion
		
		int[] testPath = new int[tspPath.length];
		int[] bestPath = new int[tspPath.length];
		Arrays.fill(testPath, -1);
		Arrays.fill(bestPath, -1);
		double bestDuration = 0;
		
		//Changed to tspPath-1
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
						for(int m = 0; m <= step;m++) {
							// for Melgarejo
							if (pathDuration/durationTimeStep >= nbTimeSteps) {
								//pathDuration += distanceFct[testPath[m]][testPath[m+1]][nbTimeSteps-1];
								pathDuration += getFIFOTravellingTime(testPath[m],testPath[m+1],nbTimeSteps-1);
							} else {
								//pathDuration += distanceFct[testPath[m]][testPath[m+1]][(int) (pathDuration/durationTimeStep)];
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
					//pathDuration += distanceFct[bestPath[m]][bestPath[m+1]][nbTimeSteps-1];
					pathDuration += getFIFOTravellingTime(bestPath[m],bestPath[m+1],nbTimeSteps-1);
				} else {
					//pathDuration += distanceFct[bestPath[m]][bestPath[m+1]][(int) (pathDuration/durationTimeStep)];
					pathDuration += getFIFOTravellingTime(bestPath[m],bestPath[m+1],pathDuration);

				}
			}
			step++;
		}
		System.out.println(Arrays.toString(bestPath) + " objective value: " + bestDuration);
		LocalTime timeEnd = LocalTime.now();
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;
		saveInCSV(fileName,"Nearest insertion algorithm",bestDuration,bestPath,computingTime);
	}
	
	static void doSavingsAlgo() {
		LocalTime timeStart = LocalTime.now();
		System.out.println("Savings Algorithm");
		int[] bestPathAllIterations = new int[cities.length +1];
		double bestTimeAllIterations = Double.MAX_VALUE;
		int[] depotzero = {cities[0]};
		for(int depot : depotzero) {
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
			double totalDuration = 0;
			double cycleDuration = 0;
			int currentTimestep = 0;
			for(int j = 0; j < savingsPath.length -1; j++) {
				if(savingsPath[j]== depot)
					cycleDuration = 0;
				currentTimestep = (int) (cycleDuration / durationTimeStep);
				totalDuration += getFIFOTravellingTime(savingsPath[j],savingsPath[j + 1],cycleDuration);
				cycleDuration += getFIFOTravellingTime(savingsPath[j],savingsPath[j + 1],cycleDuration);
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
								currentTimestep = (int) (cycleDuration / durationTimeStep);
								// totalDuration += distanceFct[helperPath[m]][helperPath[m + 1]][currentTimestep];
								// cycleDuration += distanceFct[helperPath[m]][helperPath[m + 1]][currentTimestep];
								totalDuration += getFIFOTravellingTime(helperPath[m],helperPath[m + 1],cycleDuration);
								cycleDuration += getFIFOTravellingTime(helperPath[m],helperPath[m + 1],cycleDuration);

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
			//System.out.println(Arrays.toString(bestResultPath) + " total duration: " + bestResult);
			if (bestResult < bestTimeAllIterations) {
				bestTimeAllIterations = bestResult;
				for(int m=0;m<bestResultPath.length;m++) {
					bestPathAllIterations[m] = bestResultPath[m];
				}
			}
		// Compare overall
		}
		System.out.println(Arrays.toString(bestPathAllIterations) + " objective value: "+ bestTimeAllIterations);
		LocalTime timeEnd = LocalTime.now();
		
		int computingTime = (int) Duration.between(timeStart,timeEnd).toNanos()/1000;
		saveInCSV(fileName,"Savings algorithm",bestTimeAllIterations,bestPathAllIterations,computingTime);
		
	}
	
	static void doChristofidesAlgorithm() {
		System.out.println("Christofides Algorithm");
		// transform matrix
		double[][] distanceFctTimeindependent = new double[nbLocations][nbLocations];
		
		// Get average
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
				//System.out.println(Arrays.toString(listMedian) + " " + distanceFctTimeindependent[i][j]);
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
							if(mst[m] == j ||mstIn[m] == j && mst[m] ==i ||mstIn[m] == i && mst[m] ==j) {
									duplicate = true;
							}
						}
						
						if(!duplicate){
							compare = distanceFctTimeindependent[i][j];
							if(k == 0) {
								mst[k] = i;
								mstIn[k] = -1;
							}
							mst[k+1] = j; 
							mstIn[k+1] = i;
						}
					}
				}
			}
		}
		System.out.println(Arrays.toString(mst));
		System.out.println(Arrays.toString(mstIn));
		
		// calculate nb of odd degree vertices
		counter = 0;
		for(int i = 0               ;i<mst.length;i++) {
			int helpCounter = 0;
			for(int j = 1;j<mst.length;j++) {
				if(cities[i] == mst[j]) 
					helpCounter++;
				if(cities[i] == mstIn[j]) 
					helpCounter++;
			}
			if(helpCounter%2 == 1) {
				counter++;
			}
		}
		System.out.println(counter);
		int[] perfectMatching = new int[counter];
		
		doPerfectMatchingPermutation();
		
		//
	}
	LocalTime timeStart = LocalTime.now();
	
	static void doPerfectMatchingPermutation() {
		
	}
	
	// Only for Melegarejo permutation
    static void permute(java.util.List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            //counter++;
            //System.out.println(java.util.Arrays.toString(arr.toArray()) + " "+ counter);
            	
    		double tTotalDuration = 0;
    		tTotalDuration = getFIFOTravellingTime(222,arr.get(0),0);
    		for(int j = 0; j < arr.size() -1; j++) {
    			tTotalDuration += getFIFOTravellingTime(arr.get(j),arr.get(j+1),tTotalDuration);
    		}
			tTotalDuration += getFIFOTravellingTime(arr.get(arr.size()-1),222,tTotalDuration);

    		if(tTotalDuration < bestDuration) {
    			bestDuration = tTotalDuration;
    			System.out.println("New best duration: " + bestDuration);
    			System.out.println("222 " + Arrays.toString(arr.toArray()) + " 222");
    		
            }
        }
        //System.out.println("Final best duration: " + bestDuration);
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
			while((int)(distanceFct[i][j][(int) (time/durationTimeStep)]*partOfWay + time)/durationTimeStep > (int) time/durationTimeStep ) {
				timeInIntervall = ((int) (time/durationTimeStep)+1)*durationTimeStep - time;
				partOfWay -= timeInIntervall / (int)(distanceFct[i][j][(int) (time/durationTimeStep)]);
				time += timeInIntervall;
				FIFOTravellingTime += timeInIntervall;
			}
			FIFOTravellingTime += partOfWay * distanceFct[i][j][(int) (time/durationTimeStep)];
			return FIFOTravellingTime;
		} else {
			if(time/durationTimeStep > nbTimeSteps-1) {
				return FIFODistanceFct[i][j][nbTimeSteps-1].getCost(time);
			} else {
				return FIFODistanceFct[i][j][(int) (time/durationTimeStep)].getCost(time);

			}
		}
	}
}
