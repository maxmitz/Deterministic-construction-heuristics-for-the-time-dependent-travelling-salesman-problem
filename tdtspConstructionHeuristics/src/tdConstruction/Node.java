package tdConstruction;

import java.util.*;

public class Node{
    boolean isRoot;
    int number;
    ArrayList<Node> children;

    public Node(int _n) {
                number = _n;
                children = null;
                isRoot = false;
    }

    public Node(int _n, boolean _isRoot) {
                number = _n;
                children = null;
                this.isRoot = _isRoot;
    }

        public void addChild(Node _node) {
                if(children == null) children = new ArrayList<Node>();
                children.add(_node);
    }

   
    public void visitBuildRoute(ArrayList<Integer> _route) {
                _route.add(number);
                if(children == null) return;
                for(int i = 0; i < children.size(); i++) {
                        ((Node)children.get(i)).visitBuildRoute(_route);
                }
    }

   
    public void visitFindOddDegreeNodes(ArrayList<Integer> _oddNodes) {
                if(children == null ) {
                        _oddNodes.add(number);
                        return;
                }
                if (isRoot && children.size() % 2 != 0) _oddNodes.add(number);
                if (!isRoot && children.size() % 2 == 0) _oddNodes.add(number);
                for(int i = 0; i < children.size(); i++) {
                        ((Node)children.get(i)).visitFindOddDegreeNodes(_oddNodes);
                }
    }

    public int getNumber() {return number;}
}

