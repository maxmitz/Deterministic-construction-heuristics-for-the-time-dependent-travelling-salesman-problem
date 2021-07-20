package tdConstruction;

import java.io.*;
import java.util.*;

class Graph{
   
    private int V;  
  
    private LinkedList<Integer> adj[];

    Graph(int v){
        V = v;
        adj = new LinkedList[v];
        for(int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }

    void addEdge(int v,int w){
        adj[v].add(w);
        adj[w].add(v);
    }
    
    void removeEdge(int v,int w){
    	try {
        adj[v].remove(w);
        adj[w].remove(v);
    	} catch(Exception e) {
    		
    	}
    }
 

    Boolean isCyclicUtil(int v,Boolean visited[], int parent){

        visited[v] = true;
        Integer i;

        Iterator<Integer> it =
                adj[v].iterator();
        while (it.hasNext()){
            i = it.next();
 

            if (!visited[i])
            {
                if (isCyclicUtil(i, visited, v))
                    return true;
            }

            else if (i != parent)
                return true;
        }
        return false;
    }

    Boolean isCyclic()
    {

        Boolean visited[] = new Boolean[V];
        for (int i = 0; i < V; i++)
            visited[i] = false;

        for (int u = 0; u < V; u++)
        { 
         
            if (!visited[u])
                if (isCyclicUtil(u, visited, -1))
                    return true;
        }
 
        return false;
    }
 
}