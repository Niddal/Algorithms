/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
/**
    The class that turns a map.txt file (a 2d array of ints)
    into a graph of Nodes<int[]>. It also stores the goal, start
    nodes, as well as other useful information and getter/setter
    methods
*/
public class GraphProblem {

    private int[][] map = null;
    private int[] start = new int[2];
    private int[] goal = new int[2];
    private int numRows = 0;
    private int numColumns = 0;
    private Graph<int[]> graph = null;
    
    /** 
        The constructor
        @param map the 2d array of ints to be made into a graph
        @param start the (x,y) pair of the start position
        @param goal teh coordinates of the goal position
    */
    public GraphProblem(int[][] map, int[] start, int[] goal) {
        this.numRows = map.length;
        this.numColumns = map[0].length;
        this.map = map;
        assert (start.length == 2 && goal.length == 2);
        this.start = start;
        this.goal = goal;
        //make graph based on map
        makeGraph(map);
        //System.out.println(this.graph.toString());
        //graph.printAllNodesAndChildren();
    }
    /**
        getter method
        @return number of rows
    */
    public int getNumRows() {
        return this.numRows;
    }
    /**
        getter method
        @return number of columns
    */
    public int getNumColumns() {
        return this.numColumns;
    }
    /**
        getter method
        @return (vertical distance, horizontal distance) coordinates of start position
    */
    public int[] getStartCoordinates() {
        return this.start;
    }
    /**
        getter method
        @return (vertical distance, horizontal distance) coordinates of goal position
 
    */
    public int[] getGoalCoordinates() {
        return this.goal;
    }
    /**
        getter method
        @return the int[][] map of raw ints on from {0, 1, 2, 3, 4}
    */
    public int[][] getMap() {
        return this.map;
    }
    /**
        getter method
        @return the graph of nodes
    */
    public Graph<int[]> getGraph() {
        return this.graph;
    }
    private void makeGraph(int[][] map) {
        this.graph = new Graph<int[]>();
        int x;
        // int[] data = new int[2];
        String key = "";
        boolean blocked = false;
        int indexInParent = 0;
        int cost = 0;
        
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numColumns; j++) {
                x = map[i][j];
                // data[0] = i;
//                 data[1] = j;
                int[] dataObject = new int[2];
                dataObject[0] = i; dataObject[1] = j;
                key = this.makeKey(dataObject);
                blocked = this.getIsBlocked(x);
                cost = this.getCost(x);
                Node<int[]> n = graph.makeNode( dataObject,  key,  blocked,  cost, goal);
            }
        }
        for (int i = 0; i < this.numRows; i++) {
            String parent = "";
            int[] position = new int[2];
            for (int j = 0; j < this.numColumns; j++) {
                position[0] = i; 
                position[1] = j;
                parent = this.makeKey(position);
                assert (graph.containsState(parent));
                addNode(i-1, j, 0, parent); //up
                addNode(i+1, j, 2, parent); //down
                addNode(i, j-1, 3, parent); //left  //previous three work
                addNode(i, j+1, 1, parent); //right
            }
        }

    
    }
    private void addNode(int x, int y, int indexInParentChildsList, String parentKey) {
        int[] data = new int[2]; 
        data[0] = x;
        data[1] = y;
        String childKey = this.makeKey(data);
        if (x > -1 && x < this.numRows && y > -1 && y < this.numColumns) {
            assert (this.graph.containsState(parentKey) && this.graph.containsState(childKey));
            this.graph.connectNodes( parentKey, childKey, indexInParentChildsList);
        }
        
    }
    private boolean getIsBlocked(int x) {
        if (x == 0) {return true;}
        return false;
    }
    private int getCost(int i) {
        if (i == 1 || i == 4) {
            return 1;
        } else if ( i == 2 ) {
            return 2;
        } else {
            return 0;
        }
    }
    private String makeKey(int[] position) {
        String key = position[0] + "-" + position[1];
        return key;
   }
}