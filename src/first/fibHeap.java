package first;

import java.io.*;
import java.util.*;

public class fibHeap {

	TreeNode min = new TreeNode(Integer.MAX_VALUE);
	LinkedList<TreeNode> heap;
	Hashtable<Integer,TreeNode> deg = new Hashtable<Integer,TreeNode>();
	
	//construct linkedlist of trees
	void insert(int x){
		insert(new TreeNode(x));	    
	}
	
	//construct linkedlist of trees
		void insert(TreeNode x){
			if(heap == null) {
				heap = new LinkedList<TreeNode>();
			}
			System.out.println("inside insert"+x.data);
			if(heap.size() == 0) {
				heap.add(x);
				min = x;
				
			}
			else {
				TreeNode temp = null;
				
				if(heap.getLast() != null) {
					temp = heap.getLast();
					temp.left = x;
					x.right = temp;
					
				}
				heap.add(x);
			//	System.out.println("temp = "+heap.getFirst().data+" "+heap.getLast().data);
				//TODO:
				if (x.data < min.data) {
					min = x;
				}
			}
			System.out.println("inside insert");
			displayTopHeap();
		}
		
		// outside calls: decrease key
		void decreaseKey(TreeNode x, int amount) {
			//System.out.println("inside decreaseKey "+x.data);
			int temp = x.data;
			if((temp - amount) < 0){
				return;
			}
			x.data = temp - amount;
			if(x.parent != null) {
				if(x.data < x.parent.data) {
					CascadeCut(x);
				}
			}
		}
	
	// internal calls : decrease key
	void decreaseKey(Boolean forDel, TreeNode x, int amount) {
		//System.out.println("inside decreaseKey "+x.data);
		int temp = x.data;
		if((temp - amount) < 0 && !forDel){
			return;
		}
		x.data = temp - amount;
		if(x.parent != null) {
			if(x.data < x.parent.data) {
				CascadeCut(x);
			}
		}
	}
	
	int deleteKey(int x1) {
		TreeNode x=null;
		for(TreeNode e:heap) {
			if(e.data == x1) {
				x = e;
			}//System.out.println("Inside deleteKey.."+e.data);
		}
		if(x != null) {
			int temp = x.data;
			decreaseKey(true, x, temp+1);
			removeMin();
			return temp;
		}
		return -1;
	}
	
	int deleteKey(TreeNode x) {
		decreaseKey(true, x, Integer.MAX_VALUE);
		int temp = x.data;
		removeMin();
		return temp;	
	}
	
	public void removeFromTopLevel(TreeNode x) {
		if(x == null) {
			return;
		}
		//handle left right part
		if(x.left != null) {
			TreeNode temp = x.left;
		
			if(x.right != null) {
				temp.right = x.right;
				temp.right.left = x.left;
			}
			else{
				x.left.right = null;
			}
		}
		else {	
			if(x.right != null) {
				x.right.left = null;			
			}	
		}
		heap.remove(x);
		updateMin();
	}
	
	public void updateMin() {
		if(heap == null || heap.size() == 0){
			min = null;
		}
		else {
			min = heap.getFirst();
			for(TreeNode x: heap){
				if (x.data < min.data) {
					min = x;
				}
			}
		}
	}
	
	// also include meld
	public TreeNode removeMin() {
		//Meld and change min pointer
		if(this.min == null) {
			return null;
		}
		TreeNode x = this.min;
		
		removeFromTopLevel(x);
		
		System.out.println("from inside removemin-before");
		displayTopHeap();
		
		if(x.children != null) {
			System.out.println(x.children.size());
			//disconnect children
			for(TreeNode e:x.children) {
				System.out.println("found child..."+e.data+" "+x.children.size());
				e.parent = null;
				insert(e);
			}
		}
		System.out.println("from inside removemin");
		displayTopHeap();
		//meld();
		min = heap.getFirst();
		//System.out.println("inside removeMin..."+min.data);
		//min = (min.left==null) ? min.left : min.right;
		if(min != null) {
			for (TreeNode a:heap) {
				if(min.data > a.data) {
					min = a;
				}
			}
		}
		else{
			min = null;
		}
		
		return x;
	}
	
	public int getMin(){
		if(this.min != null) {
			return this.min.data;
		}
		return -1;
	}
	
	void CascadeCut(TreeNode x) {
		if(x.parent != null) {
			TreeNode par = x.parent;
			if(par.childCut == false) {
				par.childCut = true;
			}
			else {
				//cut off the tree and keep doing until u meet a childCut false parent
				insert(x);
				x.parent = null;
				CascadeCut(par);
			}
		}		
	}
	
	void meld() {
		//Maintain hashTable of degree
		//if contained in deg, then degree not 0
		while (deg.size() != heap.size()) {
		TreeNode ZDegNode = null;
		TreeNode e = heap.getFirst();
		while (e != null) {
			int oldDeg = e.degree;
			//System.out.println("inside meld1...not in deg..e.data and deg="+e.data+e.degree);
			if(deg.containsKey(oldDeg)) {
				// then degree of key not 0
				TreeNode temp = deg.get(e.degree);
				if(e.data < temp.data) {
					e.children.add(temp);
					temp.parent = e;
					removeFromTopLevel(temp);
					e.degree++;
					deg.remove(oldDeg);
					if(!deg.containsKey(e.degree)) {
						deg.put(e.degree, e);
					}
				}
				else {
					if(temp != e) {
						temp.children.add(e);
						e.parent = temp;
						removeFromTopLevel(e);
						temp.degree++;
						deg.remove(oldDeg);
						if(!deg.containsKey(temp.degree)) {
							deg.put(temp.degree, temp);
					}
					}
				}
			} 
			else {
				// it doesnt contain key
				// degree must be 0
				if(oldDeg == 0) {
					if(ZDegNode == null) {
						ZDegNode = e;
						//System.out.println("inside meld...not in deg..e.data and deg="+e.data+e.degree);
					} else {
						  if(e.data < ZDegNode.data) {
							  e.children.add(ZDegNode);
							  ZDegNode.parent = e;
							  removeFromTopLevel(ZDegNode);
							  e.degree++;
							  if(!deg.containsKey(e.degree)) {
								deg.put(e.degree, e);
							  }
						  } else {
							ZDegNode.children.add(e);
							e.parent = ZDegNode;
							removeFromTopLevel(e);
							ZDegNode.degree++;
							if(!deg.containsKey(ZDegNode.degree)) {
								deg.put(ZDegNode.degree, ZDegNode);
							}
							ZDegNode = null;
						}
					}
				}
			}	
			e = e.left;
		} 
		System.out.println("d: "+deg.size()+" h: "+heap.size());
		/*for(int k = 0; k<deg.size(); k++) {
			System.out.print(" "+deg.get(k));
		}*/
		}
	}

	public void displayTopHeap(){
		System.out.println("Top level list");
		if(heap == null || heap.size()==0){
			return;
		}
		TreeNode e = heap.getFirst();
		while(e!=null) {
			System.out.print(" "+e.data);//+" "+e.left.data+" "+e.right.data);
			e=e.left;
		}
		System.out.print("------");
		for(TreeNode x:heap) {
			System.out.print(": "+ x.data);
		}
	}
	
	public static void main(String[] args){
		fibHeap x = new fibHeap();
		x.insert(3);
		x.displayTopHeap();
		x.insert(4);
		x.displayTopHeap();
		//System.out.println(x.getMin());
		x.insert(2);
		x.displayTopHeap();
		//System.out.println(x.getMin());
		x.insert(5);
	    x.displayTopHeap();
	    x.meld();
	    x.displayTopHeap();
		x.removeMin();
		x.displayTopHeap();
		/*x.removeMin();
		x.displayTopHeap();
		//System.out.println(x.removeMin().data);
		//System.out.println(x.removeMin().data);
		x.deleteKey(4);
		x.displayTopHeap();
		System.out.println("------------------");
		
		System.out.println("min value = "+x.getMin()); */
		
	}
	
}
