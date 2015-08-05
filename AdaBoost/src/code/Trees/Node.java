/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/

package code.Trees;

import code.Examples.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
    Vhe classic node class
*/
public class Node<V, E> {
    private V attribute;
    private V output;
    private Node<V, E> parent;
    private HashMap<E, Node<V, E>> children; //maps edge values to child nodes
    private int numChildren;
    private int depth;
    private boolean isLeaf;
    private ArrayList<Example> examples; //the subset of all examples on which to train the remaining subtree
    private Tree<V, E> manufacturer = null;
    
    
    /**
        the constructor for the traditional decision tree
        @param data
        @param depth
        @param isLeaf
        @param ex
        @param parent
        @param manufacturer
    */
    public Node(V data, int depth, boolean isLeaf, ArrayList<Example> ex, Node<V, E> parent, Tree<V, E> manufacturer) {
        this.isLeaf = isLeaf;
        //quick and dirty way to separate leaf and internal nodes:
        if (isLeaf) { //if leaf, then set the output
            this.output = data;
            this.attribute = data;
            //System.out.println(" -- made a LEAF node");
        } else { //if internal node, then set the attribute
            this.output = null;
            this.attribute = data;
            //System.out.println(" -- made INTERNAL node");
        }
        this.manufacturer = manufacturer;
        this.parent = parent;
        this.children = new HashMap<E, Node<V, E>>();
        this.numChildren = 0;
        this.depth = depth;
        this.examples = ex; //even leaf nodes get examples, i.e. all examples have same output classification...
        

    }
    /**
        make a node
        @param data 
        @param isLeaf
        @param parent pointer
        @param manufacturer which tree this node belongs to....irrelevant
    */
    public Node(V data, int depth, boolean isLeaf, Node<V, E> parent, Tree<V, E> manufacturer) {
        this.isLeaf = isLeaf;
        //quick and dirty way to separate leaf and internal nodes:
        if (isLeaf) { //if leaf, then set the output
            this.output = data;
            this.attribute = data;
            //System.out.println(" -- made a LEAF node");
        } else { //if internal node, then set the attribute
            this.output = null;
            this.attribute = data;
            //System.out.println(" -- made INTERNAL node");
        }
        this.manufacturer = manufacturer;
        this.parent = parent;
        this.children = new HashMap<E, Node<V, E>>();
        this.numChildren = 0;
        this.depth = depth;
        this.examples = null;
    }
    /**
        get something
        return something
    */
    public Tree<V, E> getManufacturer() {
        return this.manufacturer;
    }
    /**
        getter method for the examples held in contention by this node, does a sort of deep copying
        to reduce bugs
        @return the list of examples that this attribute will split on
    */
    public ArrayList<Example> getExamples() {
        if (this.examples == null) {
            return null;
        }
        ArrayList<Example> ex = new ArrayList<Example>(this.examples.size());
        for (int i = 0; i < this.examples.size(); i++) {
            ex.add(this.examples.get(i));
        }
        return ex;
    }
    /**
        getter method for the attribute field
        @return the attribute string
    */
    public boolean isLeaf() {
        return this.isLeaf;
    }
    /**
        getter method for the attribute field
        @return the attribute string
    */
    public V getAttribute() {
        if (!this.isLeaf) {
            return this.attribute;
        } else {
            return null;
        }
    }
    /**
        get something
        return something
    */
    public V getData() {
        return this.attribute;
    }
    /**
        getter method for the depth
        @return the depth 
    */
    public int getDepth() {
        return this.depth;
    }
    /**
        getter method for the output field
        @return the output class, as a string
    */
    public V getOutputClass() {
        if (this.isLeaf) {
            return this.output;
        } else {
            return null;
        }
    }
    /**
        connects a parent node to a child node by an edge labeled with valid value of the parent's Attribute
        Ignore the throws Exception, it was just a quick and dirty way of noticing silly errors, it should 
        never be thrown. 
        
        @param attributeValue value of the attribute that splits the examples, basically a label for the edge
        @param child the child node
        @return whether adding was successful
    */
    public boolean addChild(E attributeValue, Node<V, E> child) {
        assert (child != null);
        assert (this.children.containsKey(attributeValue) == false); //not adding duplicate children
        this.children.put(attributeValue, child);
        this.numChildren++;
        return true;
    }
    /**
        WE MIGHT NEED THIS LATER, PREPARE TO FIX IT FIX IT FIX IT FIX FIX FIX
        getter method
        @return the children as an ArrayList
    */
    public HashMap<E, Node<V, E>> getChildren() {
    
        //eh...just do a shallow copy and hope no one screw with it...
        
        return this.children;
    }
    /**
        getter method
        @return the numChildren
    */
    public int getNumChildren() {
        return this.numChildren; 
    }
    /**
        get something
        @param child the whatever
        return something
    */
    public E getEdgeLeadingToChild(Node<V, E> child) {
        for (E e : this.children.keySet()) {
            if (this.children.get(e).equals(child)) {
                return e;
            }
        }
        return null;
    }
    

    /**
        setter method
        @param p the reference to the parent
    */
    public void setParent(Node<V, E> p) { //MUST ONLY BE USED WHEN SWAPPING SUBTREES
        
        this.parent = p;
    }
    /**
        getter method
        @return parent node
    */
    public Node<V, E> getParent() {
        return this.parent;
    }
    /**
        get something
        @param attributeValue
        return something
    */
    public Node<V, E> getChild(E attributeValue) {
        // assert that the attribute value is actually associated with the attribute
        return this.children.get(attributeValue);
    }
    /**
        get something
        @param t
    */
    public void setLeaf(boolean t) {
        this.isLeaf = t;
    }
    /**
        get something
        @param t
    */
    public void setAttribute(V t) {
        assert (!this.isLeaf);
        this.attribute = t;
    }
    /**
        get something
        @param t
    */
    public void setOutputClass(V t) {
        assert (this.isLeaf);
        this.output = t;
        this.attribute = t;
    }

}