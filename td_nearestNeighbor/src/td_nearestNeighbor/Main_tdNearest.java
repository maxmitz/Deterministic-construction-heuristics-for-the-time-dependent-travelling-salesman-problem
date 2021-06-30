package td_nearestNeighbor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		
		
		
		// Cordeau
		File folder = new File("C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\15");
		File[] listOfFiles = folder.listFiles();
		String[] stringListOfFiles = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    stringListOfFiles[i] = listOfFiles[i].getName();
		  }
		}
		for (String name :stringListOfFiles) {
			if (name != "15CNodi_7") {
			System.out.println("\n"+name);
			//fileName = "Cordeau_40ANodi_10.txt";
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\15\\15ANodi_1";
			fileName = "C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Benchmarks\\TDTSPBenchmark_Cordeau\\15\\"+name;
			double[][] distanceFct_Cordeau;
			double[][] speed = new double[3][3];
			nbLocations = -1;
			nbTimesteps = 3;
			durationTimestep = 999999999;
			try {
				file = new Scanner(new File(fileName));
				file.useLocale(Locale.US);
				file.skip("N:");
				nbLocations = file.nextInt()+2;
				file.next();file.next();file.next();
				distanceFct_Cordeau = new double[nbLocations][nbLocations];
				distanceFct = new int[nbLocations][nbLocations][nbTimesteps];
				
				for(int i=0;i<nbLocations;i++) {
					for(int j=0;j<nbLocations;j++) {
							distanceFct_Cordeau[i][j]=file.nextDouble();
					}
				}
				
				for (int i=0;i< 3; i++) {
					file.nextLine();
				}
				
				file.nextDouble();
				// Are these the right timesteps?
				durationTimestep = (int) file.nextDouble() *60*2;
				
				for (int i=0;i< 5; i++) {
					file.nextLine();
				}
				
				for(int t = 0;t<3;t++) {
					for(int j = 0;j<3;j++) {
						speed[t][j] = file.nextDouble();
					}
				}
				for (int i=0;i< 9; i++) {
					file.nextLine();
				}
				
				int[] zone = new int[nbLocations];
				file.nextInt();
				zone[0] = file.nextInt();
				
				for(int i=0;i<nbLocations-1;i++) {
					zone[i+1] = file.nextInt();
					file.nextLine();
				}
				//System.out.println(Arrays.toString(zone));

				file.close();
				
				for(int i = 0;i<nbLocations;i++) {
					for(int j = 0;j<nbLocations;j++) {
						for(int t = 0;t<nbTimesteps;t++) {
							distanceFct[i][j][t] = (int) Math.round(distanceFct_Cordeau[i][j] / speed[t][zone[i]-1]);
							//System.out.println("i: "+ i + " j: " + j + " distance: " + distanceFct[i][j][t] + " disCor: " + distanceFct_Cordeau[i][j] + " speed: " + speed[t][zone[i]-1] );
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
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
			int[] cities = {5,6,8,23,34,51};
			cities = new int[nbLocations];
			for(int i=0;i<nbLocations;i++) {
				cities[i] = i;
			}
			
			int[] citiesHelp = new int[cities.length];
			int[] tspPath = new int[cities.length + 1];
			int currentTimestep = 0;
			int totalDuration = 0;
			int bestResult = 999999;
			int[] bestResultPath = new int[cities.length + 1];
			
			// first fit
			System.out.println("Iterated First-Fit algorithm");
			for(int l = 0; l < cities.length; l++) {
				totalDuration = 0;
				for(int k = 0; k < cities.length -1; k++) {
					//System.out.print(cities[k] + " - ");
					currentTimestep = totalDuration / durationTimestep;
					totalDuration += distanceFct[cities[k]][cities[k + 1]][currentTimestep];
				}
				currentTimestep = totalDuration / durationTimestep;
				totalDuration += distanceFct[cities[cities.length -1]][cities[0]][currentTimestep];
				//System.out.println(cities[cities.length-1] + " - " + cities[0] + ", total duration: " + totalDuration);
				
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
			
			try {
				FileWriter writer = new FileWriter("test.csv",true);
				writer.append("\nProblem;Heuristic;Objective value;Solution");
				writer.append("\n"+fileName+";Iterated-First-Fit;"+bestResult+";"+Arrays.toString(bestResultPath));
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
					  
			// nearest neighbor
			System.out.println("\n\nIterated nearest neighbor algorithm");
			
			Arrays.fill(tspPath, -1);
			int startingCity;
			int step;
			int neighborTime;
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
					currentTimestep = totalDuration / durationTimestep;
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
			
			// Nearest insertion algorithm
			System.out.println("\n\nNearest insertion algorithm");	
			Arrays.fill(tspPath, -1);
			int compare = 99999;
			totalDuration = 0;
			currentTimestep = 0;
			// find first insertion

			for(int i = 0; i < cities.length; i++) {
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
				step++;
			}
			System.out.println(Arrays.toString(bestPath) + " , " + bestDuration);
			saveInCSV(fileName,"Nearest insertion algorithm",bestDuration,bestPath);
			
			
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
					for(int i = 0; i < step; i++) {
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
				
				
				// Start with own Christofides class
				
				List<ChristofidesElement> chris = new ArrayList<ChristofidesElement>(); 
				for(int m =0; m<cities.length;m++) {
					chris.add(new ChristofidesElement(treeElements[m],predecessor[m],timeTree[m]));
					chris.get(m).getInAndOutEdges(matchingTreeElements, matchingPredecessor);
					//System.out.println(chris.get(m).printElement());
				}
				
				// if in != out => Add closest edge
				
				// !!! Order of adding should be thought of
				
				int helpcounter = 0;
				for(int i=0; i<cities.length;i++) {
					compare = 999999;
					if(chris.get(i).nbInEdges > chris.get(i).nbOutEdges) {
						// Look for quickest possible edge that is needed only one direction
						
						for(int j= 0;j<cities.length;j++) {
							if(chris.get(j).nbInEdges < chris.get(j).nbOutEdges && i!=j) {
								if(distanceFct[chris.get(i).vertice][chris.get(j).vertice][chris.get(i).currentTime/durationTimestep] < compare) {
									compare = distanceFct[chris.get(i).vertice][chris.get(j).vertice][chris.get(i).currentTime/durationTimestep];
									// !!! Class needs predecessorSSSSS
									matchingTreeElements[cities.length+helpcounter] = chris.get(j).vertice;
									matchingPredecessor[cities.length+helpcounter] = chris.get(i).vertice;

								}
								
							}
						}
						helpcounter++;
						for(int m =0; m<cities.length;m++) {
							chris.get(m).getInAndOutEdges(matchingTreeElements, matchingPredecessor);
						}	
					}
				}
				//System.out.println("\n");
				System.out.println(Arrays.toString(matchingTreeElements));
				System.out.println(Arrays.toString(matchingPredecessor));
				
				for(ChristofidesElement element : chris) {
					element.getInAndOutEdges(matchingTreeElements, matchingPredecessor);
					System.out.println(element.printElement());
				}
				
				// Detect cycles
				
		        Graph graph = new Graph(nbLocations);
		        for(int i = 1; i<matchingTreeElements.length;i++) {
		        	graph.addEdge(matchingPredecessor[i], matchingTreeElements[i]);
		        }
		        
		        for(int i=0;i<graph.nbVertices;i++) {
		            for(Integer item:graph.adj.get(i)){
		                System.out.println("From " + i + " to " + item);
		            }
		        }
		    	
		        while(graph.containsTwoCycles()[1][0] != -1) {
		            System.out.println("Graph contains at least 2 cycles");
		            int[][] cycles = graph.containsTwoCycles();
		            
		            // Check if there are duplicate vertices in the cycles
		            int duplicateVertice =-1;
		            int inDuplicate1 = -1;
		            int outDuplicate2 = -1;
		            
		            
		            while(duplicateVertice == -1) {
		            	int helper = 1;
			            for(int i=0; i<cycles[0].length;i++) {
				            for(int j=0; j<cycles[0].length;j++) {
				            	if(cycles[0][i] == cycles[helper][j] && cycles[0][i]!= -1) {
				            		duplicateVertice = cycles[0][i];
				            		if(i>0) {
				            			inDuplicate1 = cycles[0][i-1];
				            		} else {
				            			for(int m=0;m<cycles[0].length;m++) {
				            				if(cycles[0][m] != -1 && cycles[0][m] !=cycles[0][0])
				            					inDuplicate1 = cycles[0][m];
				            			}
				            		}
				            		outDuplicate2 = cycles[1][j+1];
				            		
				            	}
				            }
			            }
			            helper++;
			            System.out.println(duplicateVertice + " in 1: "+ inDuplicate1 + " out 2: " + outDuplicate2);

		            }
		            
		            if(duplicateVertice != -1) {
		            	// !!! Improve this by taking best one
		            	// Check in+out from BOTH!!! duplicateVertice
		            	graph.addEdge(inDuplicate1, outDuplicate2);
		            	graph.deleteEdge(inDuplicate1, duplicateVertice);
		            	graph.deleteEdge(duplicateVertice, outDuplicate2);
		            	
		            }
			        for(int i=0;i<graph.nbVertices;i++) {
			            for(Integer item:graph.adj.get(i)){
			                System.out.println("From " + i + " to " + item);
			            }
			        }
		            
		            // If no duplicate vertices, try to connect somewhere
		        }
		        System.out.println("Found TSP-path");
		        int[][] cycles = graph.containsTwoCycles();
		        for(int i=0;i<cities.length+1;i++) {
		        	bestResultPath[i] =cycles[0][i];
		        }
				pathDuration = 0;
				for(int m = 0; m < cities.length;m++) {
					pathDuration += distanceFct[bestResultPath[m]][bestResultPath[m+1]][pathDuration/durationTimestep];
				}
		        System.out.println("Duration: " + pathDuration);
		        

			}
				        
		}
		}
		
	}
	
	public static void saveInCSV(String fileName, String heuristic,int bestResult, int[]bestResultPath) {
		try {
			FileWriter writer = new FileWriter("test.csv",true);
			writer.append("\n"+fileName+";"+heuristic+";"+bestResult+";"+Arrays.toString(bestResultPath));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
