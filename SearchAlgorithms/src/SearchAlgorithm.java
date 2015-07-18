/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.ArrayList;
/**
    The superclass for all search algorithms like DFS, BFS and A*
    
   The SearchAlgorithm abstract class provides an interface and basic scaffolding for 
   all types of search algorithms. The key feature is the 

   public abstract ArrayList<int[]> findGoal();

   method which actually runs the search and returns the list of (x,y) coordinates
   corresponding to the solution, or null if no solution is found. Another pivotal
   function is the 

   protected boolean goalTest(Node<int[]> n)

   method, which provides a universal goalTest against which all nodes can be compared.
   The makePath method in the SearchAlgorithm class is a helper method to follow the
   parent pointers starting from the goal node back up the search tree to the start.
*/
public abstract class SearchAlgorithm {

   protected int[][] map = null;
   protected int[] start = new int[2];
   protected int[] goal = new int[2];
   protected int numRows = 0;
   protected int numColumns = 0;
   protected int numNodesExpanded = 0;
   protected int totalCostOfPath = 0;
   protected ArrayList<int[]> path = null;
   protected Graph<int[]> graph = null;
   protected boolean goalWasFound;
   
   
   /** The constructor for a search algorithm. 
        
        @param gp the graph problem 
   */
   public SearchAlgorithm(GraphProblem gp) {
       this.map = gp.getMap();
       this.start = gp.getStartCoordinates();
       this.goal = gp.getGoalCoordinates();
       this.numColumns = gp.getNumColumns();
       this.numRows = gp.getNumRows();
       this.graph = gp.getGraph();
       this.goalWasFound = false;
   }
   public abstract ArrayList<int[]> findGoal();
   protected String makeKey(int[] position) {
        String key = position[0] + "-" + position[1];
        return key;
   }
   //remember, expandNode creates new states (states are int[], to
   //represent (x,y) coordinate pairs
   protected abstract ArrayList<Node<int[]>> expandNode(Node<int[]> parent); 
   
   protected boolean goalTest(Node<int[]> n) {
        int[] currentPosition = n.getData();
        //System.out.println("testing goal " + currentPosition[0] + ", " + currentPosition[1]);

        if (currentPosition[0] == goal[0] && currentPosition[1] == goal[1]) {
            return true;
        }
        return false;
   }
   protected ArrayList<int[]> makePath(Node<int[]> goal) {
        Node<int[]> curr = goal;
        ArrayList<int[]> path = new ArrayList<int[]>();
        path.add(goal.getData());
        
        while (curr != null) {
            curr = curr.getParent();
            if (curr != null) {
                path.add(curr.getData());
            }

        }
        for (int[] n : path) {
            this.totalCostOfPath += graph.getNode(this.makeKey(n)).getCostOfNode();
        }
        return path;
   }

}