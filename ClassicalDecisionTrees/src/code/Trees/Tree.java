/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/

package code.Trees;

import code.Examples.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
/**
    The basic graph class like the one from data structures. T is the node type, 
    for this implementation, the node type will be Node<V, E> where V are vertices
    of type string, and E are edges of type string. 
    
    I had a hashtable (nodes In Graph) that stored all keys in the tree and their nodes, but I removed
    it because it made reproduction very inconvenient 
*/

public class Tree<V, E> {
    private ArrayList<Node<V, E>> nodes = new ArrayList<Node<V, E>>();
    //maps attribute strings to the associated attribute test nodes in the tree
    //private HashMap<V, Node<V, E>> nodesInGraph = new HashMap<V, Node<V, E>>(); 
    private int numNodes; //this is just an ESTIMATE: it changes based on reproduction (swapping subtrees)
    private int numLeafNodes;
    private Node<V, E> root;
    private double fitness;

    /**
        the constructor, pretty much blank
    */
    public Tree() {
        this.numNodes = 0;
        this.numLeafNodes = 0;
        this.root = null;
        this.fitness = 0;
    }
    /* for traditional decision tree: able to pass examples to a node */
    public Node<V, E> makeNode(V data, int depth, boolean isLeaf, ArrayList<Example> ex, Node<V, E> parent) {
        Node<V, E> n =  new Node<V, E>(data, depth, isLeaf, ex, parent, this);

        if (numNodes == 0) { //then set this to root
            this.root = n;
        }
        //this.nodesInGraph.put(data, n);  //PROBLEM?
        this.numNodes++;
        this.nodes.add(n);
        assert (n != null);
        return n;
    }
    
    /**
        only used in subtree swapping function of genetic algorithm
    */
    public void setRoot(Node<V, E> newRoot) {
        assert (newRoot != null);
        this.root = newRoot;
        this.nodes = null;
        this.numNodes = 1;
    }
    /* for genetic tree, no examples passed */
    public Node<V, E> makeNode(V data, int depth, boolean isLeaf, Node<V, E> parent) {
        assert (data != null);
        Node<V, E> n =  new Node<V, E>(data, depth, isLeaf, parent, this);

        if (numNodes == 0) { //then set this to root
            this.root = n;
        }
        //this.nodesInGraph.put(data, n); //PROBLEM?
        this.numNodes++;
        this.nodes.add(n);
        assert (n != null);
        return n;
    }
    public Node<V, E> getRoot() {
        return this.root;
    }
    /**
        connects a child node to a parent. Note that the parent contains a hashTable of
        size four of references to the existing child nodes. Since every node contains
        such a hashTable referencing all adjacent nodes, the graph contains effectively only
        2-way edges. Ignore the throws Exception, it will never be thrown. 
    
        @param parent the Attribute for the parent, used for hashtable and getting the node
        @param child the child, could be an attribute or a leaf, regardless, both are strings anyway.
        @param attributeValue the string describing the value of the attribute that partitions examples.
    */
    public void connectNodes(Node<V, E> parent, Node<V, E> child, E attributeValue) {
        assert (!parent.equals(child)); //no self-loops
        //check the attribute value: 
        
        boolean success = parent.addChild(attributeValue, child);
        //assert(parent.getChildren().get(indexInParentChildsList).getKey().equals(childKey));
        assert (success);
        
    }
    /**
        getter method
        @return the number of nodes in the graph
    */
    public int getNumNodes() {
        return this.numNodes;
    }
    /**
        blah
        @param n blah
    */
    public void setNumNodes(int n) {
        this.numNodes = n;
    }
    /**
        getter method
        @return node uniquely assoc with that key
    */
    public ArrayList<Node<V, E>> getNodes() { //PROBLEM?
        return nodes; //PROBLEM?
    }
    /**
        getter method
        @param n the node to get a children for
        @return the children as an ArrayList instead of a hashTable
    */
    public HashMap<E, Node<V, E>> getChildren(Node<V, E> n) {
        return n.getChildren();
    }
    /**
        blah
        @param fit blah
    */
    public void setFitness(double fit) {
        //assert (fitness == 0);
        this.fitness = fit;
    }
    /**
        blah
        @return blah
    */
    public double getFitness() {
        return this.fitness;
    }
    /**
        a toString method for the graph
        @return the string
    */
    public String toString() { 
        String s = "digraph {\n";
        Node<V, E> child = null;
        for (Node<V, E> n : nodes) {
            HashMap<E, Node<V, E>> children = this.getChildren(n);
            // System.out.println(n.getNumChildren());
            if (n.getNumChildren() == 0) {
                //s += ("    \"" + (n).getData() + "\";\n");
            } else {
                for (E key : children.keySet()) {
                    //child = children.get(w);
                    if (key != null) {
                        s += ("    \"" + (n).getData() + "\" -> \""
                         + (children.get(key)).getData() + "\" [label=\"" + key + " " + isLeaf(children.get(key)) 
                         + " depth " + (children.get(key)).getDepth()  + " " + "\"];\n"); 
                    }               
                }
            }
        
        }
        s += "}";
        return s;
    }
    private String isLeaf(Node<V, E> n) {
        if (n.isLeaf()) {
            return "leaf";
        } else {
            return "internal";
        }
    }
    /**
        makes a deep copy of a tree starting at the given node
        @param start start node of the tree below which to copy
        @return a new tree with the same start node
    */
    public Tree<V, E> makeCopy(Node<V, E> start) {
        Tree<V, E> child = new Tree<V, E>();
        //assert (this.manufacturer != child.getManufacturer());
        Node<V, E> root = traverseTreeToCopy(start, child, null);

        
        //we have to stop using toString() because we no longer update the tree class's node list..
        //assert(this.toString().equals(child.toString())); //are the trees equal?

        
        
        return child;
        
    
    }
    private Node<V, E> traverseTreeToCopy(Node<V, E> nodeToCopy, Tree<V, E> copyTreeToBuild, Node<V, E> parentOfCopyNode) {
        assert (nodeToCopy != null);
        //assert (nodeToCopy.getManufacturer() == this);
        if (parentOfCopyNode != null) assert (parentOfCopyNode.getManufacturer() == copyTreeToBuild);
        if (nodeToCopy.isLeaf()) { //make copy of the leaf node
            V classification = nodeToCopy.getOutputClass();
            assert (classification != null);
            Node<V, E> leaf = copyTreeToBuild.makeNode(classification, (nodeToCopy.getDepth()), true, parentOfCopyNode);
            return leaf; 
        } else { //make an internal node
            E value;
            Node<V, E> subtree;
            
            V attributeToSplitOn = nodeToCopy.getAttribute();
            assert (attributeToSplitOn != null);

            Node<V, E> internalNode = copyTreeToBuild.makeNode(attributeToSplitOn, (nodeToCopy.getDepth()), false, parentOfCopyNode);
 
            for (E edge : nodeToCopy.getChildren().keySet()) { //for each attribute value of the nodeToCopy...
                Node<V, E> nextOriginal = nodeToCopy.getChild(edge); //get the next nodeToCopy, a child of this nodeToCopy
                assert (nextOriginal != null);
                subtree = traverseTreeToCopy(nextOriginal, copyTreeToBuild, internalNode);
                //assert (subtree.getManufacturer() == internalNode.getManufacturer());
                copyTreeToBuild.connectNodes(internalNode, subtree, edge);
            }
            assert (internalNode.getNumChildren() == nodeToCopy.getChildren().size());
            
            return internalNode;
        
        }
     }
}