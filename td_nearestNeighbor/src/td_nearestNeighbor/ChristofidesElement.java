package td_nearestNeighbor;

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
