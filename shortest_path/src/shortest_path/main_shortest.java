package shortest_path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class main_shortest {
	public static void main(String[] args) {
		
		// read file hi
		int t = 0;
		System.out.println("Test");
		
		Float[][] array = new Float[10][10];
		try {
		      File myObj = new File("C:\\Users\\m-zim\\Desktop\\Masterarbeit\\Python\\test.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  for (int i=0; i<array.length; i++) {
		              String[] line = myReader.nextLine().trim().split(" ");
		              for (int j=0; j<line.length; j++) {
		                 array[i][j] = Float.parseFloat(line[j]);
		              }
		           }
		        
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		//System.out.println(Arrays.deepToString(array));
		//System.out.println(Arrays.deepToString(array[0]));
		//System.out.println(array[0][0]);
		
		
		// random array
		
		 int array_random[][] = new int[10][10];
		 for (int i = 0; i < array_random.length; i++) {
		     for (int j = 0; j < array_random[i].length; j++) {
		    	 array_random[i][j] = ((int) (Math.random() * 100));
		         if (array_random[i][j] % 2 == 0 || array_random[i][j] % 5 == 0) {
		        	 array_random[i][j] = 0;
		         }
				 array_random[1][0] = 5;
		         System.out.print(array_random[i][j] + " ");
		         }
		    System.out.println();
		    }
		 System.out.println("Done");
		 
		 
		 // Shortest path
		 	
		 
		 class Tuple<X, Y> { 
			  public final int x; 
			  public final int y; 
			  public Tuple(int x, int y) { 
			    this.x = x; 
			    this.y = y; 
			  } 
		 } 
		 
		 Tuple test = new Tuple(1,0);		 
		 HashSet<Tuple> currentMap = new HashSet<Tuple>();
		 int iteration = 0;
		 boolean duplicate = false;
		 currentMap.add(test);
		 
		 for(Tuple tuple : currentMap) {
			 System.out.println(tuple.x + " " + tuple.y);
		 }
		 
		while(!currentMap.isEmpty() && iteration < 5) {
			HashSet<Tuple> nextMap = new HashSet<Tuple>();
			
			
			// Find new Vertices from current
			for(Tuple currentVertex : currentMap) {
				if(array_random[currentVertex.x][currentVertex.y] != 0 ) {
					
					HashSet<Tuple> nextVertices = new HashSet<Tuple>();
					for(int i = 0; i < array_random.length; i++) {
						if(array_random[currentVertex.y][i] != 0 ) {
							nextVertices.add(new Tuple(currentVertex.y,i));
							System.out.println("Add nextVertices from " + currentVertex.x + " " + currentVertex.y  + " (" + currentVertex.y + " , " + i + ")");
						}
					}
					
					for(Tuple next : nextVertices) {
						System.out.print("( " + next.x + " , " + next.y + " ) ");
					}
					System.out.println();
					
					
					// add nextVertices to nextMap
					for(Tuple nextVertex : nextVertices) {
						for(Tuple currentVer :currentMap) {
							if (!nextMap.isEmpty()) {
								for(Tuple nextVer :nextMap) {
									duplicate = false;
									if (nextVertex == currentVer || nextVertex == nextVer) {
										duplicate = true;
									}
									if(!duplicate) {
										nextMap.add(nextVertex);
										System.out.println("Add nextMap from nextVertex " + nextVertex.x + " " + nextVertex.y );
									}
								}
							} else {
								nextMap.add(nextVertex);
								System.out.println("Add nextMap from nextVertex " + nextVertex.x + " " + nextVertex.y );
							}
						}
					}
					for(Tuple currentVer : currentMap) {
							nextMap.add(currentVer);
							System.out.println("Add nextMap from currentMap " + currentVertex.x + " " + currentVertex.y );

					}
						
					
				}
			}
			currentMap = nextMap;
			System.out.println(++iteration);
			
			for(Tuple currentVertex : currentMap) {
				System.out.print("( " + currentVertex.x + " , " + currentVertex.y + " ) ");
			}
			System.out.println();
		}
		     
	}
}
