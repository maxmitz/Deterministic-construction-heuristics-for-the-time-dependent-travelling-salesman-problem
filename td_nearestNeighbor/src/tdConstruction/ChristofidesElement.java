package tdConstruction;

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
 */     