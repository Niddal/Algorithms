/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
/**
    The basic graph class like the one from data structures
*/

public class Graph<T> {
    private ArrayList<Node<T>> nodes = new ArrayList<Node<T>>();
    private HashMap<String, Node<T>> nodesInGraph = new HashMap<String, Node<T>>();
    private int numNodes;

    /**
        the constructor, pretty much blank
    */
    public Graph() {
        this.numNodes = 0;
    }
    /**
        makes a node given parameters, but does not attach it to the graph
        
        @param data the int[] of (x,y) coordinates of the node
        @param key the "x-y" string made from (x,y) coordinates to uniquely specify a node
        @param blocked boolean of whether node is made from a "#"
        @param cost the cost of stepping into this node, if it's open
        @param goal the coordinates of the goal
        @return the node that was just made
    */
    public Node<T> makeNode(T data, String key, boolean blocked, int cost, T goal) {
        Node<T> n =  new Node<T>(data, key, blocked, cost, goal);
        assert(n.getKey().equals(key));
        this.nodesInGraph.put(key, n);
        this.nodes.add(n);
        this.numNodes++;
        return n;
    }
    /**
        connects a child node to a parent. Note that the parent contains a hashTable of
        size four of references to the existing child nodes. Since every node contains
        such a hashTable referencing all adjacent nodes, the graph contains effectively only
        2-way edges. 
    
        @param parentKey the key for the parent, used for hashtable and getting the node
        @param childKey the key of the child
        @param indexInParentChildsList the direction (up, right, down, left) of the child wrt the parent
    */
    public void connectNodes(String parentKey, String childKey, int indexInParentChildsList) {
        assert (this.nodesInGraph.containsKey(parentKey) && this.nodesInGraph.containsKey(childKey));
        Node<T> parent = nodesInGraph.get(parentKey);
        Node<T> child = nodesInGraph.get(childKey);
        assert (parent.getChildren().get(indexInParentChildsList) == null);
        ArrayList<Node<T>> children = parent.getChildren();
        parent.incrementNumChildren();
        boolean success = parent.addChild(child, indexInParentChildsList);
        assert(parent.getChildren().get(indexInParentChildsList).getKey().equals(childKey));
        assert (success);
        assert (parent.getChildren().size() == 4);
        
    }
    /**
        getter method
        @return the number of nodes in the graph
    */
    public int getNumNodes() {
        return this.numNodes;
    }
    private int getCostOfNode(String key, Node<T> p) {
        assert (false);
        int r = p.getCostOfNode();
        assert (r != -1);
        return r;
    }
    /**
        setter method for the g(n) of reaching a node
        @param key the key of the node
        @param cost the new cost, g(n)
    */
    public void updateTotalCost(String key, int cost) {
        Node<T> n = getNode(key);
        n.setCostToLocation(cost);
    }
    /**
        getter method
        @param key the key
        @return node uniquely assoc with that key
    */
    public Node<T> getNode(String key) {
        return nodesInGraph.get(key); 
    }
    /**
        getter method
        @param n the node to get a key for
        @return the key
    */
    public String getKey(Node<T> n) {
        return n.getKey();
    }
    /**
        getter method
        @param n the node to get a children for
        @return the children as an ArrayList instead of a hashTable
    */
    public ArrayList<Node<T>> getChildren(Node<T> n) {
        return n.getChildren();
    }
    /**
        getter method
        @param key the key of the node
        @return whether the graph has the key and thus the node
    */
    public boolean containsState(String key) {
        return (nodesInGraph.containsKey(key));
    }
    /**
        a toString method for the graph
        @return the string
    */
    public String toString() { 
        String s = "digraph {\n";
        Node<T> child = null;
        for (Node<T> n : nodes) {
            ArrayList<Node<T>> children = this.getChildren(n);
            assert (children.size() == 4);
            // System.out.println(n.getNumChildren());
            //s += ("    \"" + (n).getKey() + "\";\n");
            for (int w = 0; w < 4; w++) {//Node<T> child : children) {
                child = children.get(w);
                if (child != null) {
                    s += ("    \"" + (n).getKey() + "\" -> \""
                     + (child).getKey() + "\";\n"); 
                }               
            }
        
        }
        s += "}";
        return s;
    }
    /**
        print all the nodes and their children
    */
    public void printAllNodesAndChildren() {
        Node<T> n = null;
        ArrayList<Node<T>> children = null;
        for (int i = 0; i < nodes.size(); i++) {
            n = nodes.get(i);
            System.out.println("node " + n.getKey() + " has children: ");
            children = n.getChildren();
            for (int j = 0; j < 4; j++) {
                if (children.get(j) == null) System.out.println("null at index " + j);
                else {System.out.println(children.get(j).getKey() + " at index " + j);}
                
            }
            System.out.println("------------");
        }
    }

}