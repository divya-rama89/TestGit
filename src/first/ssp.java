package first;

import java.io.*;
import java.util.*;

class result{
	int weight;
	ArrayList<Integer> path;
	
	public result(int w, ArrayList<Integer> p){
		this.weight = w;
		this.path = p;
	}
	
}


public class ssp {
	static graph graphx;
	static int numVer = 0;
	static int numEdg = 0;
	
	public result route(int src, int dest){
		
		result res = new result(0, new ArrayList<Integer>());
		return res;
		
	}
	
	public static void init(PriorityQueue<TreeNode> vertexQueue, TreeNode source){
		//fill in queue initially with all nodes
        //System.out.println("called init with "+source.data);
		for(TreeNode x:graphx.vertexList){
        	if(x != source){
        		x.minDist = Integer.MAX_VALUE;
        	//	System.out.println("entered here");
        	}
        	else{
        		x.minDist = 0;
        	}
        	x.previous = null;
        	vertexQueue.add(x);
        }
		for(TreeNode x:vertexQueue){
			//System.out.println("init "+x.data+" "+x.minDist);
		}
	}
	
    public static void computePaths(TreeNode source)
    {
    	System.out.println("inside computePaths for "+source.data);
    	source.minDist = 0;
        PriorityQueue<TreeNode> vertexQueue = new PriorityQueue<TreeNode>();
      	
       // vertexQueue.add(source);

        init(vertexQueue, source);
        
	while (!vertexQueue.isEmpty()) {
		TreeNode u = vertexQueue.poll(); //removeMin for heap
		//System.out.println("Polled out "+u.data);
            // Visit each edge exiting u
            for (Edge e : u.adj)
            {
            	TreeNode v = e.dest;
                int weight = e.weight;
                int fullDist = u.minDist + weight;
		if (fullDist < v.minDist) {
		    vertexQueue.remove(v);
		    v.minDist = fullDist ;
		    v.previous = u;
		    vertexQueue.add(v);
		}
            }
        }
    }

    public static List<TreeNode> getShortestPathTo(TreeNode d)
    {
        //System.out.println("inside getShortestPathto for "+d.data);
    	List<TreeNode> path = new ArrayList<TreeNode>();
        for (TreeNode vertex = d; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
	
	
  public static void main(String[] args) {
		  
	int cntChk = 0;
	
	try {
		Scanner	in = new Scanner(new File("/home/a/workspace/first/src/abc.txt"));
		
		if(in.hasNext()){
			numVer = in.nextInt();
			// create a graph object
			System.out.println("num vertices = "+numVer);
			graphx = new first.graph(numVer);
			//graphx.displayAdj();
			System.out.println("numver:"+numVer);
			numEdg = in.nextInt();
			System.out.println("numedge" + numEdg);
			while(in.hasNext()){
				graphx.addEdge(in.nextInt(), in.nextInt(), in.nextInt());
				cntChk++;
			}
			if(cntChk != numEdg) {
				System.out.println("Invalid input! Program exiting!");
				return;
			}
			graphx.displayAdj();
			
			//////////SSP
			for (TreeNode x : graphx.vertexList) {    
			//{
			//	TreeNode x = graphx.vertexList.get(3);
			computePaths(x);
				System.out.println("Distance from " + x.data);
			    for (TreeNode v : graphx.vertexList)
				{
				    System.out.print("Distance to " + v.data + ": " + v.minDist);
				    List<TreeNode> path = getShortestPathTo(v);
				    System.out.print(" Path: ");
				    for (int i = 0; i < path.size(); i++) {
				    	System.out.print(" "+path.get(i).data);
					}
				    System.out.println();
				    
				}
			    }
			
			
		}
		
		
		
		in.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
 
}
