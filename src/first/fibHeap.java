package first;

import java.io.*;
import java.util.*;

public class fibHeap {

	TreeNode min = new TreeNode(Integer.MAX_VALUE);
	LinkedList<TreeNode> heap;
	Hashtable<Integer,TreeNode> deg = new Hashtable<Integer,TreeNode>();
	
	//construct linkedlist of trees
	public void insert(int x){
		insert(new TreeNode(x));	    
	}
	
	//construct linkedlist of trees
		void insert(TreeNode x){
			if(heap == null) {
				heap = new LinkedList<TreeNode>();
			}
			x.left = null;
			x.right = null;
			
			//System.out.println("inside insert"+x.data);
			if(heap.size() == 0) {
				
				heap.add(x);
				min = x;
			/*	System.out.println("added "+x.data+" to heap. ");
				if(x.left != null) {
					System.out.println("x.left= "+x.left.data);
				}
				if(x.right != null) {
					System.out.println("x.right= "+x.right.data);
				}
				System.out.println("----"); */
			}
			else {
				TreeNode temp = null;
				
				if(heap.getLast() != null) {
					temp = heap.getLast();
					temp.left = x;
					x.right = temp;
					
				}
				heap.add(x);
				/*
				System.out.println("added "+x.data+" to heap. ");
				if(x.left != null) {
					System.out.println("x.left= "+x.left.data);
				}
				if(x.right != null) {
					System.out.println("x.right= "+x.right.data);
				}
				System.out.println("----");
				*/
			//	System.out.println("temp = "+heap.getFirst().data+" "+heap.getLast().data);
				//TODO:
				if (x.data < min.data) {
					min = x;
				}
			}
			//System.out.println("inside insert");
			//displayTopHeap();
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
		if(x!=null) {
			return deleteKey(x);
		}
		return -1;
	}
	
	int deleteKey(TreeNode x) {
		decreaseKey(true, x, Integer.MAX_VALUE);
		int temp = x.data;
		min = x;
		//updateMin();
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
		if(deg.get(x.degree)==x) {
			deg.remove(x.degree);
		}
		heap.remove(x);
		
		//TODO: call only if necessary
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
		
	//	System.out.println("from inside removemin-before");
		//displayTopHeap();
		
		if(x.children != null) {
		//	System.out.println(x.children.size());
			//disconnect children
			for(TreeNode e:x.children) {
				//System.out.println("found child..."+e.data+" "+x.children.size());
				e.parent = null;
				insert(e);
				//displayTopHeap();
			}
		}
		
		meld();
		//System.out.println("after meld, degree size "+deg.size());
		//System.out.println("after meld, heap size "+heap.size());
		//displayTopHeap();
		return x;
	}
	
	public int getMin(){
		if(this.min != null) {
			return this.min.data;
		}
		return -1;
	}
	
	public void CascadeCut(TreeNode x) {
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
	
	public void addToChildrenList(TreeNode par, TreeNode child) {
		if(par.children.size() == 0) {
			child.left = null;
			child.right = null;
		}
		if(par.children.size() != 0) {
			TreeNode temp = par.children.get(par.children.size()-1);
			temp.right = child;
			child.left = temp;
		}
		child.parent = par;
		par.children.add(child);
		par.degree++;
	}
	
	public void meld() {
		
		//Maintain hashTable of degree
		//if contained in deg, then degree not 0
		while (deg.size() != heap.size()) {
		System.out.println("inside meld");
		
		/*for (TreeNode e: heap){
			System.out.println("e.data="+e.data);
		if(e.left != null) {
			System.out.println("e.left.data ="+e.left.data);
		}
		if(e.right != null) {
			System.out.println("e.right.data ="+e.right.data);
		}
		} */
		
		{			
		
		TreeNode e = heap.getFirst();
		TreeNode next = null;
		while (e != null) {
			int oldDeg = e.degree;
			next = e.left;
		//	System.out.println("inside meld1...not in deg..e.data and deg="+e.data+e.degree);
			if(deg.containsKey(oldDeg)) {
				// then degree of key not 0
				TreeNode temp = deg.get(e.degree);
				
				if(e.data < temp.data) {
					removeFromTopLevel(temp);
					addToChildrenList(e,temp);
					
					deg.remove(oldDeg);
					if(!deg.containsKey(e.degree)) {
						deg.put(e.degree, e);
					}
				}
				else {
					if(temp != e) {
						removeFromTopLevel(e);
						addToChildrenList(temp,e);
						
						deg.remove(oldDeg);
						if(!deg.containsKey(temp.degree)) {
							deg.put(temp.degree, temp);
					}
					}
				}
			} 
				else {
					//non zero unregistered degree
					deg.put(oldDeg, e);
				}	
				e = next;
			}
		}
	}
	updateMin();
}

	public void displayTopHeap(){
		System.out.println("Top level list");
		if(heap == null || heap.size()==0){
			return;
		}
		TreeNode e = heap.getFirst();
		while(e!=null) {
			System.out.print("I am here "+e.data);//+" "+e.left.data+" "+e.right.data);
			e=e.left;
		}
		System.out.print("------");
		for(TreeNode x:heap) {
			System.out.print(": "+ x.data);
		}
	}
	
	public void displayList(List<TreeNode> x){
		for (int i = 0; i < x.size(); i++) {
			TreeNode j = x.get(i);
			System.out.println("x.data, x.parent="+j.data+" "+j.parent.data);
			if(j.children.size() != 0){
				displayList(j.children);
			}
		}
	}
	
	public void displayFull(){
		System.out.println("full list");
		if(heap == null || heap.size()==0){
			return;
		}
		TreeNode e = heap.getFirst();
		while(e!=null) {
			System.out.print(" "+e.data);//+" "+e.left.data+" "+e.right.data);
			if(e.children.size() != 0){
				displayList(e.children);
			}
			e=e.left;
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

		x.deleteKey(4);
		x.displayTopHeap();
		//System.out.println(x.getMin());
		x.insert(5);
	    x.displayTopHeap();
	    x.meld();
	    
	    x.displayFull();
		x.removeMin();
		x.displayTopHeap();
		System.out.println("min value"+x.getMin());
		
		x.removeMin();
		System.out.println("min value"+x.getMin());
		x.displayTopHeap();
		//System.out.println(x.removeMin().data);
		//System.out.println(x.removeMin().data);
		/*x.deleteKey(4);
		x.displayTopHeap();
		System.out.println("------------------");
		
		System.out.println("min value = "+x.getMin()); */
		
	}
	
}
