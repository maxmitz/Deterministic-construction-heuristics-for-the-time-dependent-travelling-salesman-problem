package tdConstruction;


import java.util.*;


public class GraphNode{
  ArrayList<GraphNode> childList;
  int name;
  boolean visited;

  public GraphNode(int name){
              this.name=name;
              childList=new ArrayList<GraphNode>();
              visited=false;
  }

  public boolean isVisited(){
              return visited;
  }

  public void setVisited(){
              visited=true;
  }

      public int getNumberOfChilds(){
              return childList.size();
  }

  public void setNotVisited(){
              visited=false;
  }

      public void addChild(GraphNode node){
              if(!(this.getName()==node.getName()) ){
                      childList.add(node);
              }
  }

      public void removeChild(GraphNode node){
              childList.remove(node);
  }


  public boolean hasMoreChilds(){
              return childList.size()>0;
  }

  public int getName(){
              return name;
  }

  public void getNextChild(int goal, Vector<Integer> path, boolean firstTime){
              if(this.getName()==goal && !firstTime){
                      path.add(new Integer(this.getName()));
              }
              else{
                      if( childList.size()>0 ){
                              GraphNode tmpNode=(GraphNode)childList.remove(0);
                              tmpNode.removeChild(this);
                              path.add(new Integer(this.getName()));
                              tmpNode.getNextChild(goal,path,false);
                      }
              }
  }
}