package first;

import java.io.*;
import java.util.*;

public class ssp {
  public static void main(String[] args) {
	int numVer = 0;
	int numEdg = 0;
	  
	int cntChk = 0;
	graph graphx;
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
		}
		
		
		
		in.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
 
}
