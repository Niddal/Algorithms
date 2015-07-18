/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.ArrayList;
import java.util.HashMap;
/**
    The classic node class
*/
public class Node<T> /*implements Comparable<Node<int[]>>*/{
    private T data;
    private Node<T> parent;
    private boolean explored;
    private boolean onFrontier;
    private HashMap<Integer, Node<T>> children; //ArrayList<Node<T>> children;
    private int costToGetHere = 0;
    private int cost;
    private String key = "";
    private boolean isBlocked;
    private int numChildren;
    private T goal; //each node must store this so comparator can use it

    /**
        this constructor is for BFS and DFS
        @param data the data
        @param key the key
        @param blocked whether its blocked
        @param cost the cost
    */
    public Node(T data, String key, boolean blocked, int cost) {
        this.data = data;
        this.parent = null;
        this.children = new HashMap<Integer, Node<T>>(4); //new ArrayList<Node<T>>(4);
        this.key = key;
        this.cost = cost;
        this.numChildren = 0;
        this.isBlocked = blocked;
        this.explored = false;
        this.onFrontier = false;

    }
    /**
        this constructor is for A* bc each node contains location of goal
        @param data the data
        @param key the key
        @param blocked whether its blocked
        @param cost the cost
        @param goal the goal coordinates
    */
    public Node(T data, String key, boolean blocked, int cost, T goal) {
        this.data = data;
        this.parent = null;
        this.children = new HashMap<Integer, Node<T>>(4); //new ArrayList<Node<T>>(4);
        this.key = key;
        this.cost = cost;
        this.numChildren = 0;
        this.isBlocked = blocked;
        this.explored = false;
        this.onFrontier = false;
        this.goal = goal;

    }
    /**
        getter method
        @return the goal coordinates
    */
    public T getGoal() {
        return this.goal;
    }
    /**
        getter method
        @return the coordinates
    */
    public T getData() {
        return this.data;
    }
    /**
        getter method
        @param child the child node
        @param position the up, right, down, left int
        @return whether adding was successful
    */
    public boolean addChild(Node<T> child, int position) {
        if ( position >= 0 && position <= 3) {
            Integer p = new Integer(position);
            assert (this.children.containsKey(p) == false);
            this.children.put(p, child);
            return true;
        }
        return false;
    }
    /**
        getter method
        @return the g(n)
    */
    public int getCostToLocation() {
        return this.costToGetHere;
    }
    /**
        setter method
        @param cost the new g(n)
    */
    public void setCostToLocation(int cost) {
        this.costToGetHere = cost;
    }
    /**
        getter method
        @return the marginal cost of stepping in
    */
    public int getCostOfNode() {
        return this.cost;
    }
    /**
        getter method
        @return the key
    */
    public String getKey() {
        return this.key;
    } 
    /**
        getter method
        @return the children as an ArrayList
    */
    public ArrayList<Node<T>> getChildren() {
        ArrayList<Node<T>> blah = new ArrayList<Node<T>>();
        for (int i = 0; i < 4; i++) {
            blah.add(this.children.get(new Integer(i)));
        }
        return blah;
    }
//     private void initializeList(ArrayList<Node<T>> a) {
//         a.add(null); //north
//         a.add(null); //east
//         a.add(null); //south
//         a.add(null); //west
//     }
    /**
        getter method
        @return the numChildren
    */
    public int getNumChildren() {
        return this.numChildren; 
    }
    /**
        setter method
    */
    public void incrementNumChildren() {
        this.numChildren++;
    }
    /**
        setter method, sets expore to true and onFrontier to false
    */
    public void explore() {
        assert (this.onFrontier == true);
        this.explored = true;
        this.onFrontier = false;
    }
    /**
        getter method
        @return was it explored
    */
    public boolean wasExplored() {
        return this.explored;
    }
    /**
        setter method
    */
    public void addToFrontier() {
        assert (this.explored == false);
        this.onFrontier = true;
    }
    /**
        getter method
        @return is on frontier
    */
    public boolean isOnFrontier() {
        return this.onFrontier;
    }
    /**
        setter method
        @param p the reference to the parent
    */
    public void setParent(Node<T> p) {
        //assert (this.parent == null);
        this.parent = p;
    }
    /**
        getter method
        @return parent node
    */
    public Node<T> getParent() {
        // assert (this.parent != null);
        return this.parent;
    }
    /**
        getter method
        @return is blocked
    */
    public boolean isBlocked() {
        return this.isBlocked;
    }

}