/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
public class DepthFirstSearch extends SearchAlgorithm {


//-----super variables (all are set to proper values): 
//     protected int[][] map
//     protected int[] start 
//     protected int[] goal 
//     protected int numRows
//     protected int numColumns 
//     protected int numNodesExpanded --------(NEEDS UPDATING)
//     protected int totalCostOfPath  --------(NEEDS UPDATING)
//     protected ArrayList<int[]> path
//     protected Graph<int[]> graph

    private Stack<Node<int[]>> stack = new Stack<Node<int[]>>();
    //queueA.add("element 0");
    //Object firstElement = queueA.remove();
    
    //private Tree<int[]> searchTree = null;
    private Node<int[]> current = null;
    private ArrayList<int[]> path = new ArrayList<int[]>();
    private int pathCost = 0;

    /**
        the constructor
        @param gp the graph problem instance
    */
    public DepthFirstSearch(GraphProblem gp) {
        super(gp);
        this.current = graph.getNode(super.makeKey(super.start));
        stack.push(current);
        current.addToFrontier();
   
    }
    
    
//-----methods:
    @Override
    public ArrayList<int[]> findGoal() {
        //test goal condition first, at first current is start
        //ArrayList<int[]> neighborPositions
        Node<int[]> child = null;
        ArrayList<Node<int[]>> children = null;
        System.out.println("======== Beginning Depth First Search ==========");
        if (super.goalTest(current)) { //check trivial case of start = goal;
            path = super.makePath(current);
            printGoodnessHoney();
            return path;
        }
        while (!stack.empty()) {
            
            current = stack.pop();
            current.explore();
            
            // System.out.println("visited node " + current.getKey() + " from stack");
            children = this.expandNode(current);
            
            for (int i = 0; i < 4; i++) { //Node<int[]> child : this.expandNode(current)) {
                child = children.get(i);
                //child.addToFrontier(); no no no
                
                // // System.out.println(child);
                
                if ((child != null) && !child.isOnFrontier() && !child.wasExplored() && !child.isBlocked()) {
                    
                    if (super.goalTest(child)) {
                        child.setParent(current);
                        path = super.makePath(child);
                        printGoodnessHoney();
                        return path;
                    } else {
                        stack.push(child);
                        child.addToFrontier();
                        child.setParent(current);
                    }
                }
            }
        } 
        printFailure();

        return null; //meaning failure
    }
    @Override
    protected ArrayList<Node<int[]>> expandNode(Node<int[]> parent) {
        super.numNodesExpanded++;
        //if parent is state (x, y)
        //then expansion is making nodes for states (x-1, y), (x+1, y), (x, y-1) and (x, y+1);
        
        return parent.getChildren();
    }
    private void printFailure() {
        System.out.println("\n==========================\n!!! Goal Not Reachable !!!\n" 
                + "==========================\n" );
        System.out.println("total cost of found path: infinity");
        System.out.println("number of nodes expanded: " + super.numNodesExpanded);
        

    }
    private void printGoodnessHoney() {
        System.out.println("\n===================\n*** Goal Found! ***\n" + "===================\n" );
        System.out.println("total cost of found path: " + super.totalCostOfPath);
        System.out.println("number of nodes expanded: " + super.numNodesExpanded);
        super.goalWasFound = true;
    }
}