package tdConstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main_Construction {
	
	static int nbLocations = 0;
	static int nbTimeSteps = 0;
	static double durationTimeStep = 0;
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
	static double bestDuration = 999999;
	static String[] stringListOfFiles;
	static int counter;
	static boolean isCordeau = false;
	static int[] serviceTime;
	public static double[][] distanceFctTimeIndependent;
	static List<Integer> mst;
	static List<Integer> mstIn;
	static int[] bestMatching;
	private static FIFOTimeStep[][][] FIFODistanceFct;
	static String folderName;
	static DataReading dataReading;
	static File folder;
	static File[] listOfFiles;

	
	public static void main(String[]args) throws FileNotFoundException {

		// Cordeau
		
		//data reading
		isCordeau = true;
		int[] numbersCordeau = {15,20,25,30,35,40};
		int counter = 0;
		stringListOfFiles = new String[6*30];
		for(int j=0;j<numbersCordeau.length;j++) {
			// 
			File folder = new File("C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\"+Integer.toString(numbersCordeau[j]));
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
			  if (listOfFiles[i].isFile()) {
			    stringListOfFiles[counter] = Integer.toString(numbersCordeau[j]) +"\\"+ listOfFiles[i].getName();
			    counter++;
			  }
			}
		}
		
		// By deleting // in the two following lines only the first instance from Cordeau is taken
		//stringListOfFiles = new String[1];
		//stringListOfFiles[0] = "15\\15ANodi_1";
		for (String name :stringListOfFiles) {
			System.out.println("\n"+name);
			// Has to changed to location of Cordeau benchmark
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\"+name;
			DataReading dataReading = new DataReading(fileName);
			fileName = name;
			distanceFct = dataReading.getDistanceFctCordeau();
			nbLocations = dataReading.getnbLocations();
			nbTimeSteps = dataReading.getnbTimeSteps();
			durationTimeStep = dataReading.getdurationTimeStepCordeau();
			setFIFODistanceFct();	
			
			cities = new int[nbLocations-1];
			for(int i=0;i<nbLocations-1;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
						
			// Used heuristics 
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
		}
		isCordeau = false;
		
		
		
		
		// Melgarejo
		
		// Has to be changed to matrix file
		String fileNameBenchmark = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Melgarejo\\Matrices\\matrix00.txt";				
		DataReading dataReading = new DataReading(fileNameBenchmark);
		distanceFct = dataReading.getDistanceFctMelgarejo();
		nbLocations = dataReading.getnbLocations();
		nbTimeSteps = dataReading.getnbTimeSteps();
		durationTimeStep = dataReading.getdurationTimeStep();
		
		// Get instances
	
		int[] numbersMelgarejo = {10,20,30,50,100};
		counter = 0;
		stringListOfFiles = new String[220];
		
		// Has to be changed to instance location without time windows
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
		
		// By deleting // in the two following lines only the first instance from Melgarejo is taken
		//stringListOfFiles = new String[1];
		//stringListOfFiles[0] = "10\\inst_10_1.txt";
		
		for(String fileNamet : stringListOfFiles) {
			fileName = fileNamet;
			System.out.println(fileName);
			dataReading = new DataReading(fileNameBenchmark);
			distanceFct = dataReading.getDistanceFctMelgarejo();
			file = new Scanner(new File(folderName+fileName));
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
			
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			// run heuristics
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
			
			
		}
		
		
		
		
		// Rifki
		System.out.println("Rifki Benchmark");
		folderName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Rifki";
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
		
		for(String fileNamet:stringListOfFiles) {
			fileName = fileNamet;
			System.out.println("\n" + fileName);
			dataReading = new DataReading(folderName + "\\" + fileName);
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
			setFIFODistanceFct();
			
			serviceTime = new int[nbLocations];
			Arrays.fill(serviceTime, 180);
			
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
						
		}
		
		
		// Normal TSP
		
		//Location has to be changed
		folderName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TSPBenchmarks";
		folder = new File(folderName);
		listOfFiles = folder.listFiles();
		stringListOfFiles = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    stringListOfFiles[i] = listOfFiles[i].getName();
		  }
		}

		for(String fileNamet:stringListOfFiles) {
			fileName = fileNamet;
			System.out.println("\n" + fileName);
			dataReading = new DataReading(folderName + "\\" + fileName);
			distanceFct = dataReading.getDistanceFctTimeIndependent();
			nbLocations = dataReading.getnbLocations();
			nbTimeSteps = 1;
			durationTimeStep = Integer.MAX_VALUE;
			setFIFODistanceFct();	
			
			serviceTime = new int[nbLocations];
			Arrays.fill(serviceTime, 0);
			
			
			// General TD-TSP
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			citiesHelp = new int[cities.length];
			tspPath = new int[cities.length + 1];
			bestResultPath = new int[cities.length + 1];
			
			// Do heuristics
			doFirstFit();
			doNearestNeighbor();
			doNearestInsertion();
			doSavingsAlgo();
			doChristofidesAlgorithm();
			
		}
		
	}
	
	
	public static void saveInCSV(String fileName, String heuristic,double bestResult, int[]bestResultPath, int time) {
		try {
			File test = new File("results.csv");
			if(!test.exists()) {
				FileWriter writer = new FileWriter("results.csv",true);
				writer.append("Instance"+";"+"Heuristic"+";"+"Objective function value"+";"+"Solution"+";"+ "Computation Time (in seconds)"+";"+"Number of time steps");
				writer.flush();
				writer.close();
			}
			FileWriter writer = new FileWriter("results.csv",true);
			writer.append("\n"+fileName+";"+heuristic+";"+(int)bestResult+";"+Arrays.toString(bestResultPath)+";"+ time + ";" +nbTimeSteps);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void doFirstFit() {
		System.out.println("First-Fit algorithm");
		LocalTime timeStart = LocalTime.now();
		bestResult = Double.MAX_VALUE;
		int[] originalCities = new int[cities.length];
		for(int m = 0; m < cities.length; m++) {
			originalCities[m] = cities[m];
		}
		
		for(int l = 0; l < 1; l++) {
			totalDuration = 0;
			for(int k = 0; k < cities.length -1; k++) {
				currentTimestep = (int) (totalDuration / durationTimeStep);
				// for Cordeau
				if(currentTimestep > nbTimeSteps -1) {
					currentTimestep = nbTimeSteps -1;
				}
				totalDuration += getFIFOTravellingTime(k,k+1,totalDuration);
			}
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
		
		LocalTime timeEnd = LocalTime.now();
		System.out.println(Arrays.toString(bestResultPath) + " objective value: " + bestResult);
		float computingTime = Duration.between(timeStart,timeEnd).toSeconds();	
		saveInCSV(fileName,"First-Fit algorithm",bestResult,bestResultPath, (int) computingTime);
				  
		
	}
	
	public static void doNearestNeighbor() {
		System.out.println("Nearest neighbor algorithm");
		LocalTime timeStart = LocalTime.now();
		double neighborTime;
		bestResult = Double.MAX_VALUE;
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

		LocalTime timeEnd = LocalTime.now();
		System.out.println(Arrays.toString(tspPathSolution)+ " objective value: " + totalDuration);
		float computingTime = Duration.between(timeStart,timeEnd).toSeconds();
		saveInCSV(fileName,"Nearest neighbor algorithm",totalDuration,tspPathSolution,(int)computingTime);
	}
	
	public static void doNearestInsertion() {
		System.out.println("Nearest insertion algorithm");
		LocalTime timeStart = LocalTime.now();
		int[] tspPathSolution = new int[tspPath.length];
		double compare = Double.MAX_VALUE;
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
				compare = Double.MAX_VALUE;
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
		LocalTime timeEnd = LocalTime.now();
		System.out.println(Arrays.toString(tspPathSolution) + " objective value: " + bestDuration);
		float computingTime = Duration.between(timeStart,timeEnd).toSeconds();
		saveInCSV(fileName,"Nearest insertion algorithm",bestDuration,tspPathSolution,(int)computingTime);
	}
	
	static void doSavingsAlgo() {
		System.out.println("Savings Algorithm");
		LocalTime timeStart = LocalTime.now();
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
			bestDuration = Double.MAX_VALUE;
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
		
		LocalTime timeEnd = LocalTime.now();
		System.out.println(Arrays.toString(bestResultPath) + " objective value: "+ bestDuration);		
		float computingTime = Duration.between(timeStart,timeEnd).toSeconds();
		saveInCSV(fileName,"Savings algorithm",bestDuration,bestResultPath,(int)computingTime);
		
	}
	
	
	
	
	static void doChristofidesAlgorithm() {
		System.out.println("Christofides Algorithm");
		LocalTime timeStart = LocalTime.now();

		// transform matrix
		distanceFctTimeIndependent = new double[cities.length][cities.length];
		

		// Get median for distanceFct
		
		for(int i = 0;i<cities.length;i++) {
			for(int j = 0;j<cities.length;j++) {
				double[] listMedian = new double[nbTimeSteps*2];
				for(int t = 0;t<nbTimeSteps;t++) {
					listMedian[t] = distanceFct[cities[i]][cities[j]][t];
					listMedian[t+nbTimeSteps] = distanceFct[cities[j]][cities[i]][t];
				}
				Arrays.sort(listMedian);
				if(nbTimeSteps == 1)
					distanceFctTimeIndependent[i][j] = (listMedian[nbTimeSteps] + listMedian[0])/2;
				else
					distanceFctTimeIndependent[i][j] = (listMedian[nbTimeSteps] + listMedian[nbTimeSteps+1])/2;
			}
		}
		
		
		//Get average for distanceFct
		/*
		for(int i = 0;i<cities.length;i++) {
			for(int j = 0;j<cities.length;j++) {
				double average = 0;
				for(int t = 0;t<nbTimeSteps;t++) {
					average += distanceFct[cities[i]][cities[j]][t];
					average += distanceFct[cities[j]][cities[i]][t];
				}
				distanceFctTimeIndependent[i][j] = average / nbTimeSteps;
			}
		}
		*/
		
	    Christofides ch = null;
	    try{
	    	ch = new Christofides();
	    }
	    catch(Exception e){
		    e.printStackTrace();
		    System.exit(0);
	    }
	
	      
        int [] shortestPath = ch.solve(distanceFctTimeIndependent);          
		
		int[] solution = new int[cities.length +1];
		for(int i = 0;i<cities.length;i++)
			solution[i] = shortestPath[i]; 
		solution[cities.length] = 0;
		
		totalDuration = 0;
		for(int k = 0; k < solution.length -1; k++) {
			totalDuration += getFIFOTravellingTime(solution[k],solution[k + 1],totalDuration);
		}
		LocalTime timeEnd = LocalTime.now();
		
		for(int i = 0;i < solution.length;i++)
			solution[i] = cities[solution[i]];
		System.out.println(Arrays.toString(solution) + " objective value: "+ totalDuration);

		float computingTime = Duration.between(timeStart,timeEnd).toSeconds();
		saveInCSV(fileName,"Christofides algorithm",totalDuration,solution,(int)computingTime);
	}
	
    
	public static void setFIFODistanceFct() {
		
		if(nbTimeSteps == 1) {
			FIFODistanceFct = new FIFOTimeStep[nbLocations][nbLocations][nbTimeSteps];
			for(int i = 0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					for(int s=0;s<nbTimeSteps;s++) {
						FIFOTimeStep fts = new FIFOTimeStep(getTravellingTimeTimeStep(i, j, s));
						FIFODistanceFct[i][j][s]=fts;
					}
				}
			}
		} else {
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
	}
	
	public static double getTravellingTimeTimeStep(int i, int j, int s) {
		return distanceFct[i][j][s];
	}
	
	public static double getFIFOTravellingTime(int i, int j, double time) {
		if(isCordeau) {
			double partOfWay = 1;
			double timeInIntervall = 0;
			double FIFOTravellingTime = 0;
			if(time/durationTimeStep >=3) {
				time = 2*durationTimeStep+1;
			}
			while((int)((distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)]*partOfWay + time)/durationTimeStep) > (int) (time/durationTimeStep) ) {
				timeInIntervall = ((int) (time/durationTimeStep)+1)*durationTimeStep - time;
				partOfWay -= timeInIntervall / (int)(distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)]);
				time += timeInIntervall;
				FIFOTravellingTime += timeInIntervall;
				if(time+0.00001/durationTimeStep >=3) {
					time = 2*durationTimeStep+1;
				}
			}
			FIFOTravellingTime += partOfWay * distanceFct[cities[i]][cities[j]][(int) (time/durationTimeStep)];
			return FIFOTravellingTime;
		} else {
			
			// THIS HAS TO BE CHANGED FOR THE SERVICE TIMES
			
			if(nbTimeSteps > (int) ((time + serviceTime[i])/durationTimeStep)) {
				return FIFODistanceFct[cities[i]][cities[j]][(int) ((time + serviceTime[i])/durationTimeStep)].getCost(time) + serviceTime[i];
			} else {
				return FIFODistanceFct[cities[i]][cities[j]][nbTimeSteps-1].getCost(time) + serviceTime[i];

			}
			 /*
			if(nbTimeSteps > (int) ((time)/durationTimeStep)) {
				return FIFODistanceFct[cities[i]][cities[j]][(int) ((time)/durationTimeStep)].getCost(time);
			} else {
				return FIFODistanceFct[cities[i]][cities[j]][nbTimeSteps-1].getCost(time) + serviceTime[i];
			}
			*/
		}
	}


 }

/*
// Compare with results from instances
counter = 0;
//permute(java.util.Arrays.asList(190,81,32,9,132,240 ,74 ,127, 150),0);

// optimal solution 10_1
int[] solution = {0,4,5,8,9,1,3,7,2,6,0};
// for savings inst_10_1
//int[] solution = {0, 5, 4, 3, 1, 9, 8, 7, 2, 6, 0};

double tTotalDuration = 0;
int tCurrentTimestep = 0;
for(int k = 0; k < solution.length -1; k++) {
	tCurrentTimestep = (int) (tTotalDuration / durationTimeStep);
	tTotalDuration += distanceFct[cities[solution[k]]][cities[solution[k + 1]]][tCurrentTimestep] + serviceTime[solution[k]];
}
System.out.println("total duration without FIFO: " + tTotalDuration);

tTotalDuration = 0;
for(int k = 0; k < solution.length -1; k++) {
	tTotalDuration += getFIFOTravellingTime(solution[k],solution[k + 1],tTotalDuration);
}
System.out.println("total duration with FIFO: " + tTotalDuration);

System.out.println(distanceFct[cities[0]][cities[1]][0]);

cities = new int[2];
cities[0] = 0;
cities[1] = 1;
for(int t = 4305;t<4325;t++) 
	System.out.println("t: " + t + "  " + distanceFct[cities[0]][cities[1]][(int)(t)/360]+ "  " + getFIFOTravellingTime(0, 1,t));

for(int t = 4305;t<4325;t++) 
	System.out.print(t + ",  ");
System.out.println();
for(int t = 4305;t<4325;t++) 
	System.out.print(distanceFct[cities[0]][cities[1]][(int)(t)/360]+ ",  ");
System.out.println();
for(int t = 4305;t<4325;t++) 
	System.out.print(getFIFOTravellingTime(0, 1,t)+ ",  ");
 */


