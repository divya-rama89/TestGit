package first;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode {

    int data;
    Boolean childCut;
    TreeNode left;
    TreeNode right;
    TreeNode parent;
    List<TreeNode> children;
    int degree = 0;

    //need priority field
    
    public TreeNode(int data) {
        this.data = data;
        this.childCut = false;
        this.children = new LinkedList<TreeNode>();
    }

    public TreeNode addChild(int d) {
        TreeNode childNode = new TreeNode(d);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
}
