/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
public class BreadthFirstSearch extends SearchAlgorithm {


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

    private Queue<Node<int[]>> queue = new LinkedList<Node<int[]>>();
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
    public BreadthFirstSearch(GraphProblem gp) {
        super(gp);
        this.current = graph.getNode(super.makeKey(super.start));
        queue.add(current);
        current.addToFrontier();
   
    }
    
    
//-----methods:
    @Override
    public ArrayList<int[]> findGoal() {
        //test goal condition first, at first current is start
        //ArrayList<int[]> neighborPositions
        Node<int[]> child = null;
        ArrayList<Node<int[]>> children = null;
        System.out.println("======== Beginning Breadth First Search ==========");
        do {
            if (super.goalTest(current)) {
                path = super.makePath(current);
                printGoodnessHoney();
                return path;
            }
            current = queue.remove();
            current.explore();
            
            // System.out.println("removed node " + current.getKey() + " from frontier");
            children = this.expandNode(current);

            for (int i = 0; i < 4; i++) { //Node<int[]> child : this.expandNode(current)) {
                child = children.get(i);
                
                // // System.out.println(child);
                
                if ((child != null) && !child.isOnFrontier() && !child.wasExplored() && !child.isBlocked()) {
                    assert (child != null);
                    child.setParent(current);
                    child.setCostToLocation(child.getCostOfNode() + current.getCostToLocation());
                    
                    if (super.goalTest(child)) {
                        //child.setParent(current);
                        path = super.makePath(child);
                        printGoodnessHoney();
                        return path;
                    } else {
                        queue.add(child);
                        child.addToFrontier();
                        //child.setParent(current);
                    }
                    
                }  else if (child != null && child.isOnFrontier() && queue.contains(child)) {
                    int newCost = child.getCostOfNode() + current.getCostToLocation();
                    if (newCost < child.getCostToLocation()) {
                        //System.out.println("indeed, shorter path found");
                        child.setParent(current);
                        child.setCostToLocation(newCost);
                    }
                }
            }
        } while (!queue.isEmpty());
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