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
	public	void insert(TreeNode x){
		if(x == null) {
			return;
		}	
		if(heap == null) {
				heap = new LinkedList<TreeNode>();
			}
			x.left = null;
			x.right = null;
			x.childCut = false;
			x.parent = null;
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
		
		public TreeNode find(int val){
			if(heap == null || heap.size()==0){
				return null;
			}
			
			for(TreeNode e:heap){
				if(e.data == val){
					return e;
				}
				else{
					if(e.children.size() != 0) {
						TreeNode result = lookInChildren(e, val);
						if(result != null) {
							return result; 				
						}
					}
				}
			}
			return null;
		}
		
		public TreeNode lookInChildren(TreeNode e, int val) {
			if(e == null) {
				return null;
			}
			if(e.children.size() != 0) {
					for (TreeNode x:e.children) {
						if(x.data == val)
							return x;
						else {
							TreeNode result = lookInChildren(x, val);
						
							if(result != null) {
								return result; 				
							} //else dont do anything
						}
					}	//check for next element
				}
			return null;
		}
		
		public Boolean decreaseKey(int nodeVal, int amount){
			TreeNode x=find(nodeVal);
			
			if(x != null) {
				decreaseKey(false, x, amount);
				return true;
			}
			
			return false;
		}
		// outside calls: decrease key
		public void decreaseKey(TreeNode x, int amount) {
			if(x == null) {
				return;
			}
			decreaseKey(false, x, amount);
		}
	
	// internal calls : decrease key
	public void decreaseKey(Boolean forDel, TreeNode x, int amount) {
		if(x == null) {
			return;
		}
		
		System.out.println("inside decreaseKey "+x.data);
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
		updateMin();
	}
	
	public int deleteKey(int x1) {
		TreeNode x=find(x1);
		if(x != null) {
				return deleteKey(x);
			}
			
		return -1;
	}
	
	public int deleteKey(TreeNode x) {
		if(x == null) {
			return -1;
		}
		decreaseKey(true, x, Integer.MAX_VALUE);
		int temp = x.data;
		//min = x;
		//updateMin();
		removeMin(true);
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
		
		//clear from degree table cos called from in between meld
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
	
	public TreeNode removeMin() {
		return removeMin(false);
	}
	
	// also include meld
	public TreeNode removeMin(Boolean ifDel) {
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
				System.out.println("found child..."+e.data+" "+x.children.size());
				insert(e);
				//displayTopHeap();
			}
		}
		
		if(!ifDel) meld();
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
		if(x == null) {
			return;
		}
		if(x.parent != null) {
			TreeNode par = x.parent;
			removeFromChildrenList(par, x);
			
			if(par.childCut == false) {
				par.childCut = true;
				insert(x);
			}
			else {
				//cut off the tree and keep doing until u meet a childCut false parent
				insert(x);
				x.parent = null;
				CascadeCut(par);
			}
		}		
	}
	
	public void removeFromChildrenList(TreeNode par, TreeNode child) {
		if(par == null || child == null) {
			return;
		}
		if(par.children.size() == 1) {
			TreeNode temp = par.children.get(0);
			if(temp == child) {
			//no need to adjust child left/right pointers
			}
		}
		else {
			//more than one child
			if(child.left != null) {
				TreeNode temp = child.left;
				if(child.right != null) {
					child.right.left = temp;
					temp.right = child.right;
				}
				else {
					temp.right = null;
				}
			}
			else {
				if(child.right != null) {
					child.right.left = null;
				}
			}
		}
		par.children.remove(child);
		par.degree--;
	}
	
	public void addToChildrenList(TreeNode par, TreeNode child) {
		if(par == null || child == null) {
			return;
		}
		if(par.children.size() == 0) {
			child.left = null;
			child.right = null;
		}
		else {
			TreeNode temp = par.children.get(par.children.size()-1);
			temp.right = child;
			child.left = temp;
		}
		child.parent = par;
		par.children.add(child);
		// check if needed
		// par.chilCut = false;
		child.childCut = false;
		par.degree++;
	}
	
	public void meld() {
		//int iter = 0;
		//Maintain hashTable of degree
		deg.clear();
		
		while (deg.size() != heap.size()) {
		//iter++;
			//System.out.println("inside meld round#"+ iter);
				
		{			
		
		TreeNode e = heap.getFirst();
		//System.out.println("first element ="+e.data);
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
					//doing 2nd time
					deg.remove(oldDeg);
					if(!deg.containsKey(e.degree)) {
						deg.put(e.degree, e);
					}
				}
				else {
					if(temp != e) {
						removeFromTopLevel(e);
						addToChildrenList(temp,e);
						//doing 2nd time
						deg.remove(oldDeg);
						if(!deg.containsKey(temp.degree)) {
							deg.put(temp.degree, temp);
					}
					}
				}
			} 
				else {
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
			System.out.print(" "+e.data);//+" "+e.left.data+" "+e.right.data);
			e=e.left;
		}
		System.out.print("------");
		for(TreeNode x:heap) {
			System.out.print(": "+ x.data);
		}
	}
	
	public void displayList(List<TreeNode> x){
		if(x == null) {
			return;
		}
		if(x.size() == 0) {
			return;
		}
		for (int i = 0; i < x.size(); i++) {
			TreeNode j = x.get(i);
			System.out.println("data, parent, childcut ="+j.data+" "+j.parent.data+" "+j.childCut+" ");
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
			System.out.print(" data= "+e.data+" cc ="+e.childCut);//+" "+e.left.data+" "+e.right.data);
			
			if(e.children.size() != 0){
				displayList(e.children);
			}
			e=e.left;
		}
	
	}
	
public void test2(){
		
		int[] arr = {8,7,5,30};
		for (int i = 0; i < arr.length; i++) {
			insert(arr[i]);
		}
		displayFull();
		meld();
		System.out.println("min value="+getMin()+" after meld ");
		displayFull();
}

    public void test3(){
    	insert(3);
		displayTopHeap();
		insert(4);
		displayTopHeap();
		//System.out.println(x.getMin());
		insert(2);
		displayTopHeap();

		//x.deleteKey(4);
		//x.displayTopHeap();
		//System.out.println(x.getMin());
		insert(5);
	    displayTopHeap();
	    //x.meld();
	    
	    displayFull();
		removeMin();
		displayTopHeap();
		System.out.println("min value"+getMin());
		
		removeMin();
		System.out.println("min value"+getMin());
		displayTopHeap();
		
		decreaseKey(4,3);
		System.out.println("after decrease key value"+getMin());
		//System.out.println(x.removeMin().data);
		//System.out.println(x.removeMin().data);
		deleteKey(1);
		displayTopHeap();
		//System.out.println("------------------");
		
		//System.out.println("min value = "+x.getMin()); 
    }

	public void test1(){
		
		int[] arr = {15,7,8,6,4,3,30};
		for (int i = 0; i < arr.length; i++) {
			insert(arr[i]);
		}
		
		System.out.println("min value="+getMin()+" after insert ");
		displayFull();
		displayTopHeap();
		removeMin();
		System.out.println("after removeMin). min value="+getMin());
		displayFull();
		displayTopHeap();
		
		deleteKey(6);
		System.out.println("after deleteKey(6). min value="+getMin());
		displayFull();
		displayTopHeap();
		
		insert(22);
		insert(12);
		insert(1);
		System.out.println("after inserting(1,12,22). min value="+getMin());
		
		decreaseKey(15, 10);
		System.out.println("after decreaseKey(15,10). min value="+getMin());
		displayFull();
		displayTopHeap();
		
				
		removeMin();
        System.out.println("after removeMin. min value="+getMin());
        displayFull();
        displayTopHeap();


        decreaseKey(5,3);
		System.out.println("after decreaseKey(5,3). min value="+getMin());
		displayFull();
		displayTopHeap();
        
        for (int i = 0; i <9; i++) {
        	removeMin();
            System.out.println("after removeMin. min value="+getMin());
            displayFull();
            displayTopHeap();	
		}
        

        
	}
	
	public static void main(String[] args){		
		
		fibHeap x = new fibHeap();
		//x.test2();
		//System.out.println("----------------------");
		x.test1();
				
	}
	
}
