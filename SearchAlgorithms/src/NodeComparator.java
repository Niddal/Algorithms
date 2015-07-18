/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 1
    crosset2@jhu.edu
*/
import java.util.Comparator;

/**
    the node comparator, implements our h(n) heuristic function applied
    over two nodes.
*/
public final class NodeComparator implements Comparator<Node<int[]>> {
  public static final NodeComparator INSTANCE = new NodeComparator();

  private NodeComparator() {}

  @Override
  /**
        returns the f(n_1) - f(n_2) for nodes n1 and n2 and evaluation function
        f(n) = g(n) + h(n);
        @param n1 the first node
        @param n2 the second
        @return the difference in evaluation functions
  */
  public int compare(Node<int[]> n1, Node<int[]> n2) {
    //return Integer.valueOf(n1.priority).compareTo(n2.priority);
    int[] data1 = n1.getData();
    int[] data2 = n2.getData();
    int totalCost1 = n1.getCostToLocation();
    int totalCost2 = n2.getCostToLocation();
    int[] goal = n1.getGoal();


//----Modified Manhattan distance heuristic. This code says that
//    h(node 1) = h1 = the difference in x values plus the difference
//    in y values IFF the difference in x values is not zero. Meaning,
//    the algorithm will try hard to align the current node with the
//    goal node vertically. Once this occurs, h(n) goes to zero,
//    and f(n) becomes just g(n), which is exactly Dijkstra's algorithm
//    and expands very very few nodes as it descends on a straight line
//    to the goal node. This method should expand about 29,000 nodes
//    as opposed to regular manhattan heuristic, which expands about 
//    330,000. This heuristic is admissable because at no point is 
//    h(n) greater than the actual distance to the goal; in other words
//    h(n) is at most the manhattan distance. Note that both these 
//    heuristics return the optimal solution since they are both 
//    admissable
    int h1 = (Math.abs(data1[1] - goal[1]));
    assert (h1 > -1);
    if (h1 > 1) {
            h1 = h1 + Math.abs(data1[0] - goal[0]);
    }
    int h2 = (Math.abs(data2[1] - goal[1]));
    if (h2 > 1) {
            h2 = h2 + Math.abs(data2[0] - goal[0]);
    }
    assert (h2 > -1);
    int evaluationFunction1 = h1 + totalCost1;
    int evaluationFunction2 = h2 +  totalCost2;
 
 /*
    Alternatively, you can comment out lines 47-58 above and uncomment the below
    lines to activate the pure manhattan distance heuristic. This heuristic is
    clearly admissable, yet it is not as good as the above heuristic in a wide
    open space such as map6.txt and map9.txt.
 */   
//     int deltax1 = Math.abs(data1[0] - goal[0]);
//     int deltay1 = Math.abs(data1[1] - goal[1]);
//     int deltax2 = Math.abs(data2[0] - goal[0]);
//     int deltay2 = Math.abs(data2[1] - goal[1]);
//     double h1 = deltax1 + deltay1; 
//     double h2 = deltax2 + deltay2;
//     int evaluationFunction1 = (int) (totalCost1 + h1);
//     int evaluationFunction2 = (int) (totalCost2 + h2);

//     int evaluationFunction1 = deltax1 + deltay1;
//     int evaluationFunction2 = deltax2 + deltay2;
    
    return  (evaluationFunction1 - evaluationFunction2);
    
  }
}