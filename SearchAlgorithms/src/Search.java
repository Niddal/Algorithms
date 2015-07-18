/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
    Command Line usage: ./run.sh Search <map.txt file> <which search algorithm> 
    for example: "./run.sh Search ../data/map1.txt 1"
    
   This is the main driver program. It will call BFS, DFS, and A* search through SearchAlgorithm.
   Furthermore, Search.java is responsible for reading the input symbols from the 
   mapX.txt file, extracting and assembling all useful information each node will need,
   and then calling the GraphProblem constructor to make the graph of nodes and 
   connect them.
   
   when reading the characters from the map, it converts them into numbers of
   of the set {0, 1, 2}. 0 = block (#), 1 = open (.) w/ cost 1, 
   2 = open (,) with cost 2, s = start 3, g = goal 4.

*/

public class Search {
  
    private static int horizontalDistance = 0;
    private static int verticalDistance = 0;
    private static int[][] graph = null;
    private static int[] indicesOfStart = new int[2];
    private static int[] indicesOfGoal = new int[2];
    private static int whichAlgorithm = 0;
    private static GraphProblem gp = null;
    private static ArrayList<int[]> path = null;
    
/**
   * The searching driver program.
   *
   *
   * @param  args the arguments read
   */
    public static void main(String[] args) throws IOException {
    //args[0] is the file path, args[1] is which search algorithm
        readAndConvertInputMap(args[0]);
        readWhichSearchAlgorithm(args[1]);
        gp = new GraphProblem(graph, indicesOfStart, indicesOfGoal);
        SearchAlgorithm sa = null;
        
        switch (whichAlgorithm) {
            case 1: { sa = new BreadthFirstSearch(gp); break;}
            case 2: { sa = new DepthFirstSearch(gp); break;}
            case 3: { sa = new AStarSearch(gp, NodeComparator.INSTANCE); break;}    
        }
        
        ArrayList<int[]> answer = sa.findGoal();
        path = answer;
        if (answer != null) {
            printAnswer(answer); //will print the path taken to goal
            //printSolutionsOnGraph(); //will print the path on the graph
        }
        
        //printGraph(); //prints the raw graph   
    }
    private static void printAnswer(ArrayList<int[]> path) {
        System.out.print("====== Printing Path ======\nbacktrack from goal:\n");
        for (int[] n : path) {
            System.out.println("(" + n[0] + ", " + n[1] + ")");
        }    
        System.out.print("====== End Printing Path ======\n");
    }
    private static void displayOptions() {
        System.out.println("Enter the integer corresponding to the search algorithm" 
            + " you would like to use");
        System.out.println("1. Breadth First Search\n2. Depth First Search\n 3."
            + " A* Search\n");
        
    }
    private static void readWhichSearchAlgorithm(String which) {
        int num = 0;
        try {
            num = Integer.parseInt(which);
            if (num < 1 || num > 3) {
                System.err.println("ERROR: not a valid selection");
                System.exit(1);
            }
            whichAlgorithm = num;
        } catch (NumberFormatException e) {
            System.err.println("ERROR: not a number");
        }
    
    }
    private static void readAndConvertInputMap(String fileName) 
        throws IOException {
        String line = "";
        String[] sizes; 
        BufferedReader reader = null;
        int lineCounter = 0;
        int withinLineCounter = 0;
        char[] chars = null;
        char ch;
        //attempt to load the file
        try {
            reader = new BufferedReader(new FileReader(
                new File(fileName)));
            line = reader.readLine();
        } catch (IOException e) {
            System.err.println("ERROR: could not read file");
            System.exit(1);
        }  
        //get the size of the map and initialize graph
        try {
            sizes = line.split(" ");
            assert (sizes.length == 2);
            horizontalDistance = Integer.parseInt(sizes[0]);
            verticalDistance = Integer.parseInt(sizes[1]);
            graph = new int[verticalDistance][horizontalDistance];
        } catch (NumberFormatException e) {
            System.err.println("ERROR: number format problem");
            System.exit(1);
        }
        //read the characters from the map and convert into numbers of
        //of the set {0, 1, 2}. 0 = block (#), 1 = open (.) w/ cost 1, 
        //2 = open (,) with cost 2, s = start 3, g = goal 4. 
        while ((line = reader.readLine()) != null) { //read each line
           
            //do sanity checks on each line 
            chars = line.toCharArray();
            assert (chars.length == horizontalDistance);
            if (lineCounter == 0 || lineCounter == verticalDistance - 1) {
                checkTopAndBottomEdgesOfMap(chars);
            } else {
                checkLeftAndRightEdgesOfMap(chars);
            }
            //iterate through every character in a line
            withinLineCounter = 0;
            for (int i = 0; i < chars.length; i++) {    
                ch = chars[i];
                switch (ch) {
                    case '#': {graph[lineCounter][i] = 0; break;}
                    case ',': {graph[lineCounter][i] = 2; break;}
                    case '.': {graph[lineCounter][i] = 1; break;}
                    case 's': {graph[lineCounter][i] = 3; 
                                indicesOfStart[0] = lineCounter;
                                indicesOfStart[1] = i;
                                break;}
                    case 'g': {graph[lineCounter][i] = 4;
                                indicesOfGoal[0] = lineCounter;
                                indicesOfGoal[1] = i;
                                break;}
                    default: { System.err.println("Error: " + ch + " is not" 
                                    + " a valid character");
                               System.exit(1);
                             }
                }
            }
            lineCounter++;
        }
    }
    private static void checkTopAndBottomEdgesOfMap(char[] line) {
        for (char c: line) {
            assert (c == '#');
        }
    }
    private static void checkLeftAndRightEdgesOfMap(char[] chars) {
        assert (chars[0] == '#');
        assert (chars[chars.length - 1] == '#');
    }
    private static void printGraph() {
        int i = 0;
        int j = 0;
        
        for (i = 0; i < verticalDistance; i++) {
            for (j = 0; j < horizontalDistance; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println();
        } 
        System.out.println("indices of Start: (" + indicesOfStart[0] + ", " + indicesOfStart[1] + ")\n" 
            + "indices of Goal: ("+ indicesOfGoal[0] + ", " + indicesOfGoal[1] + ")");
    }
    private static void printSolutionsOnGraph() {
        int[][] solutionMap = new int[verticalDistance][horizontalDistance];
        //deep copy the map
        int x = 0; 
        int y = 0;
        for (int i = 0; i < verticalDistance; i++) {
            solutionMap[i] = Arrays.copyOf(graph[i], horizontalDistance);
        }
        for (int[] position : path) {
            x = position[0];
            y = position[1];
            solutionMap[x][y] = 5;
        }
        int i = 0;
        int j = 0;
        
        for (i = 0; i < verticalDistance; i++) {
            for (j = 0; j < horizontalDistance; j++) {
                System.out.print(solutionMap[i][j]);
            }
            System.out.println();
        }
    }
}
