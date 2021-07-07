package tdConstruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    final int nbVertices;
    final List<List<Integer>> adj;
 
    public Graph(int V)
    {
        this.nbVertices = V;
        adj = new ArrayList<>(V);
         
        for (int i = 0; i < V; i++)
            adj.add(new LinkedList<>());
    }
    
    private boolean containsTwoCyclesRecursive(int i, boolean[] visited, boolean[] recStack, int step, int counter, int[][] cycles){
        boolean duplicate;
        boolean oneStep = true;
    	if (recStack[i] && i == cycles[counter][0]) {
        	return true;
        }
 
        if (visited[i]) {
        	//System.out.println();
            return false;
        }
        visited[i] = true;
 
        recStack[i] = true;
        List<Integer> children = adj.get(i);
        // Check if direct way to cycles[counter][0] is possible
        for (Integer c: children) {
        	if (c == cycles[counter][0]) {
        		step++;
        		cycles[counter][step] = c;
        		//System.out.println(c);
                return true;
        	}
        	
        }
        
        
        for (Integer c: children) {
        	// Check for duplicates
			duplicate = false;
			for (int k = 1; k < step; k++) {
				if (c == cycles[counter][k]) {
					duplicate = true;
				}
			}
        	if(!duplicate) {
        		if(oneStep) {
        			step++;
        			oneStep = false;
        		}
            	cycles[counter][step] = c;
            	//System.out.print(c +" + ");
                if (containsTwoCyclesRecursive(c, visited, recStack,step,counter, cycles )) {
                	//System.out.println("Cycle found");
                    return true;
                }
        	}
        }
        recStack[i] = false;
        //System.out.println();
        return false;
    }
 
    void addEdge(int source, int dest) {
        adj.get(source).add(dest);
        System.out.println("added: " + source + " -> " + dest);
    }
    
    void deleteEdge(int source, int dest) {
        adj.get(source).removeIf(i -> i == dest);
        System.out.println("removed: " + source + " -> " + dest);
    }

    int[][] containsTwoCycles(){
        int[][] cycles = new int[100][nbVertices];
        for(int i = 0;i<cycles.length;i++)
        	Arrays.fill(cycles[i], -1);
        int counter = 0;
        for (int i = 0; i < nbVertices; i++) {
        	int step = 0;
            boolean[] visited = new boolean[nbVertices];
            boolean[] recStack = new boolean[nbVertices];
            cycles[counter][step] = i;
        	//System.out.print(i +" + ");
            if (containsTwoCyclesRecursive(i, visited, recStack, step, counter, cycles)) {
            	//System.out.println();
            	if(!sameCycle(cycles,counter)) {
                	counter++;
            	} else {
            		Arrays.fill(cycles[i], -1);
            	}
            }
        }
		for(int j = 0;j<counter;j++) {
			System.out.println(Arrays.toString(cycles[j]));
		}
    	if(counter >= 2) {
    		return cycles;
    	} else {
    		Arrays.fill(cycles[1], -1);
    		return cycles;
    	}
    }
    
    boolean sameCycle(int[][]cycles,int counter){
    	int sizeArray = 0;
    	int[] cycleHelper;
    	boolean duplicate;
    	//get Array size
    	for(int i=0;i<cycles[counter].length;i++) {
    		if(cycles[counter][i] != -1)
    			sizeArray++;
    	}
    	cycleHelper = new int[sizeArray];
    	
    	for(int j = 0;j<cycleHelper.length;j++) {
    		cycleHelper[j] = cycles[counter][j];
    	}
    	
    	for(int j=0;j< cycleHelper.length-2;j++) {
			for(int m = cycleHelper.length-2; m >= 0; m--) {
				cycleHelper[m+1] = cycleHelper[m];
			}
			cycleHelper[0] = cycleHelper[cycleHelper.length-1];
			
	    	for(int i = 0;i<cycles[counter].length;i++) {
	    		if(i != counter) {
		    		for(int[] oneCycle : cycles) {
		        		duplicate = true;
		        		for(int l=0;l<sizeArray;l++) {
		        			if(cycleHelper[l] != oneCycle[l]) {
		        				duplicate = false;
		        			}
		        		}
		        		if(duplicate) {
		        			return true;
		        		}
		    		}
	    		}
	    	}
    	}
    	return false;
    }
}
