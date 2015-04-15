package first;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class TreeNode implements Comparable<TreeNode>{

    int data; //vertex number
    public ArrayList<Edge> adj; //to hold adjacent vertices and weight
    public int minDist = Integer.MAX_VALUE; // distance which is the priority decider
    public TreeNode previous;
    public int compareTo(TreeNode other)
    {
        return Double.compare(minDist, other.minDist);
    }
    
    // required for heap
    Boolean childCut; //childcut field for cascadecut
    TreeNode left;    // left sibling
    TreeNode right;	  // right sibling
    TreeNode parent;  
    List<TreeNode> children;
    int degree = 0;

    
    public TreeNode(int data) {
        this.data = data;
        this.childCut = false;
        this.children = new LinkedList<TreeNode>();
        this.adj = new ArrayList<Edge>();
    }
 }

 class Edge
	{
	    public final TreeNode dest;
	    public final int weight;
	    public Edge(TreeNode d, int w)
	    { 
	    	dest = d; 
	    	weight = w; }
	}


public class graph {
	 
	 private final int numVer;
	 private int numEdg;
     public final ArrayList<TreeNode> vertexList = new ArrayList<TreeNode>();

public graph(int Ver) {
    if (Ver < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
    this.numVer = Ver;
    this.numEdg = 0;
    
    // build an reference list 
    for (int i = 0; i < numVer; i++) {
    	vertexList.add(i,new TreeNode(i)); 	
    }
    
}

public int getVert(){
	return numVer;
}

public int getEdg(){
	return numEdg;
}

public void addEdge(int vertexAval, int vertexBval, int weight){
	if(vertexAval < 0 || vertexBval < 0 || weight < 0) {
		return;
	}
	TreeNode nodeA =  vertexList.get(vertexAval);
	TreeNode nodeB = vertexList.get(vertexBval);
	addEdge(nodeA, nodeB, weight);
}

public void addEdge(TreeNode vertexA, TreeNode vertexB, int weight){
	if(vertexA == null || vertexB == null || weight < 0) {
		return;
	}
	Edge temp = new Edge(vertexB, weight);
	vertexA.adj.add(temp);
	//System.out.println("for "+vertexA.data+" "+temp.dest.data+","+temp.weight);
	
	temp = new Edge(vertexA, weight);
	vertexB.adj.add(temp);
	//System.out.println("for "+vertexB.data+" "+temp.dest.data+","+temp.weight);
}

public void displayAdj() {
	 for (int i = 0; i < vertexList.size(); i++) {
		 System.out.print("i="+i);	
		 TreeNode temp =vertexList.get(i);
		 for (int j = 0; j < vertexList.get(i).adj.size(); j++) {
		       Edge tempE =temp.adj.get(j);
	    			System.out.print(" "+tempE.dest.data + ","+tempE.weight);
	    	} 
		 System.out.println();
	    }
}

}