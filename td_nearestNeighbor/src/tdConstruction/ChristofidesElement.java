package tdConstruction;

import java.util.Arrays;

public class ChristofidesElement {
	int vertice;
	int predecessor;
	int currentTime;
	int nbInEdges;
	int nbOutEdges;
	
	ChristofidesElement(int vertice,int predecessor, int currentTime){
		this.vertice = vertice;
		this.predecessor = predecessor;
		this.currentTime = currentTime;

		
	}
	
	public void getInAndOutEdges(int[] edges, int[] predecessors) {
		this.nbInEdges = 0;
		this.nbOutEdges = 0;
		for(int i=1;i<edges.length;i++) {
			if (this.vertice == edges[i]) {
				this.nbInEdges++;
			}
			if (this.vertice == predecessors[i]) {
				this.nbOutEdges++;
			}
		}
	}
	
	public String printElement() {
		
		
		return "vertice: " + this.vertice + " predecessor: " + this.predecessor + " currentTime: " + this.currentTime + " nbInEdges: " + this.nbInEdges + " nbOutEdges " + this.nbOutEdges;
	}
}

// Older Christofides Version
/*
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
 */     

// Christofides old shortcut

/**
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
				if(distanceFctTimeindependent[cities[connectedV.get(i)]][cities[connectedV.get(j)]] - distanceFctTimeindependent[cities[vertex]][cities[j]] - distanceFctTimeindependent[cities[connectedV.get(i)]][vertex] < compare) {
					// Check for duplicate
					
	    			boolean duplicate = false;
	        		for(int k = 0; k < mst.size() -1; k+= 2) {
        				if((connectedV.get(i) == mst.get(k) && connectedV.get(j) == mstIn.get(k))
        						|| (connectedV.get(j) == mst.get(k) && connectedV.get(i) == mstIn.get(k)))
        					duplicate = true;
	        		} 
	    			if(!duplicate) {
	    				
	    				// check for independent cycles
						Graph graph = new Graph(cities.length);

					    for(int m = 0; m<mst.size();m++) {
					    	graph.addEdge(mstIn.get(m), mst.get(m));
					    }
					    graph.addEdge(i, j);
					    graph.removeEdge(i,vertex);
					    graph.removeEdge(j,vertex);
					    graph.removeEdge(vertex,i);
					    graph.removeEdge(vertex,j);
					    
					    if(!graph.isCyclic()|| mst.size() == cities.length + 1 ) {
							compare = distanceFctTimeindependent[cities[connectedV.get(i)]][cities[connectedV.get(j)]] - distanceFctTimeindependent[cities[vertex]][cities[connectedV.get(j)]] - distanceFctTimeindependent[cities[connectedV.get(i)]][vertex];
							bestV = vertex;
							bestI = i;
							bestJ = j;
					    }
	    			}
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
	
	System.out.println("After remove");
	System.out.println(mst);
	System.out.println(mstIn);
	
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
**/