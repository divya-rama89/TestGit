package first;

import java.util.ArrayList;

public class graph {
	 private final int numVer;
	 private int numEdg;
     private ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();

public graph(int Ver) {
    if (Ver < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
    this.numVer = Ver;
    this.numEdg = 0;
    
    // build an adjacency matrix 
    for (int i = 0; i < numVer; i++) {
    	ArrayList<Integer> temp = new ArrayList <Integer>(); 
    	for (int j = 0; j < numVer; j++) {	
    		temp.add(j, -1);
    	}
    	adj.add(i,temp);
    }
}

public int getVert(){
	return numVer;
}

public int getEdg(){
	return numEdg;
}

public void addEdge(int vertexA, int vertexB, int weight){
	ArrayList<Integer> temp = adj.get(vertexA);
	temp.add(vertexB,weight);
	temp = adj.get(vertexB);
	temp.add(vertexA,weight);
}

public void displayAdj() {
	 for (int i = 0; i < this.numVer; i++) {
		 System.out.println();	
		 for (int j = 0; j < this.numVer; j++) {
	    			//System.out.print(i+","+j+" "+ adj[i][j]);
	    			System.out.print(" "+adj.get(i).get(j));
	    	}
	    }
}

}

