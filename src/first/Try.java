package first;

import java.util.Hashtable;
import java.util.LinkedList;

public class Try {

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
			//System.out.println("inside insert"+x.data);
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
			//System.out.println("inside insert");
			//displayTopHeap();
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
		
		
		public void meld2(){
			TreeNode e = heap.getFirst();
			TreeNode ZDegNode = null;
			
			while (e!=null){
				int oldDeg = e.degree;
				TreeNode next = e.left;
				if(deg.contains(oldDeg)){
				System.out.println("oldegree in there ="+oldDeg);
				
			}
			else {
				if(ZDegNode == null) {
					ZDegNode = e;
					System.out.println("ZDegNode ="+ZDegNode.data);
				}
				else{
						  if(e.data < ZDegNode.data) {
							  removeFromTopLevel(ZDegNode);
							  addToChildrenList(e,ZDegNode);
							  System.out.println("added "+e.data+"to list of "+ZDegNode.data);
							  if(!deg.containsKey(e.degree)) {
								deg.put(e.degree, e);
							  }
						  } else {
							removeFromTopLevel(e);
						    addToChildrenList(ZDegNode,e);
							System.out.println("added "+e.data+"to list of "+ZDegNode.data);
							if(!deg.containsKey(ZDegNode.degree)) {
								deg.put(ZDegNode.degree, ZDegNode);
							}
							ZDegNode = null;
						}
				}
				System.out.println("oldegree not in there ="+oldDeg);
			    deg.put(e.degree, e)	;
			}
				e = next;
			}
			for (int i = 0; i < deg.size(); i++) {
				if(deg.containsKey(i))
				System.out.println(deg.get(i));
			}
			
		}
		
		
		public void meld() {
			//Maintain hashTable of degree
			//if contained in deg, then degree not 0
		//	while (deg.size() != heap.size()) {
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
			
			//{			
			TreeNode ZDegNode = null;
			TreeNode e = heap.getFirst();
			TreeNode next = e.left;
			//while (e != null) {
				int oldDeg = e.degree;
				System.out.println("inside meld1...not in deg..e.data and deg="+e.data+e.degree);
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
					// it doesnt contain key
					// degree must be 0
					if(oldDeg == 0) {
						if(ZDegNode == null) {
							ZDegNode = e;
							//System.out.println("inside meld...not in deg..e.data and deg="+e.data+e.degree);
						} else {
							  if(e.data < ZDegNode.data) {
								  removeFromTopLevel(ZDegNode);
								  addToChildrenList(e,ZDegNode);
								 
								  if(!deg.containsKey(e.degree)) {
									deg.put(e.degree, e);
								  }
							  } else {
								removeFromTopLevel(e);
							    addToChildrenList(ZDegNode,e);
								
								if(!deg.containsKey(ZDegNode.degree)) {
									deg.put(ZDegNode.degree, ZDegNode);
								}
								ZDegNode = null;
							}
						}
					}
				}	
				e = next;
			//}
			System.out.println("d: "+deg.size()+" h: "+heap.size());
			for(int k = 0; k<deg.size(); k++) {
				System.out.print(" "+deg.get(k));
			//}
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
			/*System.out.print("------");
			for(TreeNode x:heap) {
				System.out.print(": "+ x.data);
			}*/
		}
		
		public static void main(String[] args){
			Try x = new Try();
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
		    x.meld2();
		    x.displayTopHeap();
		}
	
}
