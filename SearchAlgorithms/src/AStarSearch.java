/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
/**
    implements A* search, extends SearchAlgorithm
*/
public class AStarSearch extends SearchAlgorithm {


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

//-----class variables:
    private PriorityQueue<Node<int[]>> pq = null;
    //pqA.add("element 0");
    //Object firstElement = pqA.remove();
    
    private Node<int[]> current = null;
    private ArrayList<int[]> path = new ArrayList<int[]>();
    private int pathCost = 0;

    /**
        the constructor
        @param gp the graph problem instance
        @param cp the comparator for the priority queue
    */
    public AStarSearch(GraphProblem gp, NodeComparator cp) {
        super(gp);
        int initialSize = 10;
        pq = new PriorityQueue<Node<int[]>>(initialSize, cp);
        this.current = graph.getNode(super.makeKey(super.start));
        
        
        pq.add(current);
        current.addToFrontier();
   
    }
    @Override
    public ArrayList<int[]> findGoal() {
        //test goal condition first, at first current is start
        //ArrayList<int[]> neighborPositions
        Node<int[]> child = null;
        ArrayList<Node<int[]>> children = null;
        System.out.println("======== Beginning A* Search ==========");
        do {
            if (super.goalTest(current)) {
                path = super.makePath(current);
                printGoodnessHoney();
                return path;
            }
            current = pq.poll();
            current.explore();
            
            //System.out.println("removed node " + current.getKey() + " from frontier");
            children = this.expandNode(current);

            for (int i = 0; i < 4; i++) { //Node<int[]> child : this.expandNode(current)) {
                child = children.get(i);
                                
                if ((child != null) && !child.isOnFrontier() && !child.wasExplored() && !child.isBlocked()) {
                    assert (child != null);
                    child.setParent(current);
                    child.setCostToLocation(child.getCostOfNode() + current.getCostToLocation());
                    
                    if (super.goalTest(child)) {
                        path = super.makePath(child);
                        printGoodnessHoney();
                        return path;
                    } else {
                        pq.add(child);
                        child.addToFrontier();
                    }
                } else if (child != null && child.isOnFrontier() && pq.contains(child)) {
                    
                    
                    int newCost = child.getCostOfNode() + current.getCostToLocation();
                    if (newCost < child.getCostToLocation()) {
                        //here, a shorter path to a node is indeed found, 
                        //but bc java's PriorityQueue does not have an updateKey()
                        //method, we must remove it, then update the cost, then
                        //reinsert it into the queue. See the README
                        boolean removed = pq.remove(child);
                        assert (removed);
                        child.setParent(current);
                        child.setCostToLocation(newCost);
                        pq.add(child);
                    }
                }
            }
        } while (!pq.isEmpty());
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