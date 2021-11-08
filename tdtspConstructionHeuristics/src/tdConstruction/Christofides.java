package tdConstruction;

import java.util.*;

public class Christofides {

	

    public Christofides(){
    }



	  public int[] solve(double[][] weightMatrix){

				int mst[]=prim(weightMatrix, weightMatrix[0].length);
				int match[][] = greedyMatch(mst,weightMatrix,weightMatrix[0].length);				
				GraphNode nodes[] = buildMultiGraph(match, mst);				
				int route[] = getEulerCircuit(nodes);

				return route;
		}

		private int[] getEulerCircuit(GraphNode nodes[]) {
				LinkedList<Integer> path=new LinkedList<Integer>();
				Vector<Integer> tmpPath = new Vector<Integer>();
				int j=0;
				
				nodes[0].getNextChild( nodes[0].getName(), tmpPath, true );
				path.addAll(0, tmpPath);
				
				while(j < path.size()) {
						if(nodes[((Integer)path.get(j)).intValue()].hasMoreChilds()) {
								nodes[((Integer)path.get(j)).intValue()].getNextChild( nodes[((Integer)path.get(j)).intValue()].getName(),tmpPath,true );
								if(tmpPath.size()>0) {
										for(int i = 0; i < path.size(); i++) {
												if( ((Integer)path.get(i)).intValue() == ((Integer)tmpPath.elementAt(0)).intValue() ) {
														path.addAll(i, tmpPath);
														break;
												}
										}
										tmpPath.clear();
								}
								j = 0;
						}
						else j++;
				}
					
				boolean inPath[]=new boolean[nodes.length];
				int[] route=new int[nodes.length];
				j=0;
				for(int i=0;i<path.size();i++){
						if(!inPath[((Integer)path.get(i)).intValue()]){
								route[j]=((Integer)path.get(i)).intValue();
								j++;
								inPath[((Integer)path.get(i)).intValue()]=true;
						}
				}
				return route;				
		}

		private GraphNode[] buildMultiGraph(int[][] match, int[] mst) {
				GraphNode nodes[]=new GraphNode[mst.length];
				for(int i=0;i<mst.length;i++){
						nodes[i]=new GraphNode(i);
				}
				
				for(int i=1;i<mst.length;i++){
						nodes[i].addChild(nodes[mst[i]]);
						nodes[mst[i]].addChild(nodes[i]);				}
				
				for(int i=0;i<match.length;i++){
						nodes[match[i][0]].addChild(nodes[match[i][1]]);
						nodes[match[i][1]].addChild(nodes[match[i][0]]);
				}
				
				return nodes;
		}






    public int[] prim(double[][] wt,int dim){

				Vector<Integer> queue=new Vector<Integer>();
				for(int i=0;i<dim;i++)
						queue.add(i);

				boolean isInTree[] = new boolean[dim];
				double key[]=new double[dim]; 
				int p[]=new int[dim];

				for(int i=0;i<dim;i++){
						key[i]=Integer.MAX_VALUE;
				}

				key[0]=0;
				int u=0;

				double temp;
				Integer elem;
				do{
						isInTree[u] = true; 
						queue.removeElement(u);
						for(int v=0;v<dim;v++){ 
								if( !isInTree[v] && wt[u][v]<key[v] ){
										p[v]=u;
										key[v]=wt[u][v];
								}
						}

						double mint=Double.MAX_VALUE;
						for(int i=0;i<queue.size();i++){
								elem=(Integer)queue.elementAt(i);
								temp=key[elem.intValue()];
								if(temp<mint){
										u=elem.intValue();
										mint=temp;
								}
						}
				} while(!queue.isEmpty());
				
				return p;
    }

    public int[][] greedyMatch(int[] p,double[][] wt,int dim){

				Node nodes[] = new Node[p.length];

				nodes[0] = new Node(0, true);
				for(int i =1; i<p.length;i++) {
						nodes[i] = new Node(i,false);
				}

				for(int i = 0; i<p.length;i++) {
						if(p[i]!=i)
								nodes[p[i]].addChild(nodes[i]);
				}

				ArrayList oddDegreeNodes = findOddDegreeNodes(nodes[0]);
				int nOdd = oddDegreeNodes.size();


				Edge edges[][] = new Edge[nOdd][nOdd];
				for(int i = 0; i < nOdd; i++) {
						for(int j = 0; j < nOdd; j++) {
								if( ((Integer)oddDegreeNodes.get(i)).intValue() != ((Integer)oddDegreeNodes.get(j)).intValue() )
										edges[i][j] = new Edge( ((Integer)oddDegreeNodes.get(i)).intValue(),
																						((Integer)oddDegreeNodes.get(j)).intValue(),
																						wt[((Integer)oddDegreeNodes.get(i)).intValue()][((Integer)oddDegreeNodes.get(j)).intValue()] );
								else
										edges[i][j] = new Edge( ((Integer)oddDegreeNodes.get(i)).intValue(),
																						((Integer)oddDegreeNodes.get(j)).intValue(),Double.MAX_VALUE );
						}
						Arrays.sort(edges[i]); 
				}

				boolean matched[] = new boolean[dim];
				int match[][] = new int[(nOdd/2)][2];

				int k = 0;
				for(int i = 0; i < nOdd; i++) {
						for(int j = 0; j < nOdd; j++) {
								if( matched[edges[i][j].getFrom()] || matched[edges[i][j].getTo()] )
										continue;
								else {
										matched[edges[i][j].getFrom()] = true;
										matched[edges[i][j].getTo()] = true;
										match[k][0] = edges[i][j].getFrom();
										match[k][1] = edges[i][j].getTo();
										k++;
								}
						}
				}

				return match;
		}


    private ArrayList<Integer> findOddDegreeNodes(Node _root) {
				ArrayList<Integer> oddNodes = new ArrayList<Integer>();
				_root.visitFindOddDegreeNodes(oddNodes);
				return oddNodes;
    }
}
