/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/

package code.Trees;

import code.Trees.Tree;
import java.util.ArrayList;

public class Forest<V, E> {
    private ArrayList<Tree<V, E>> forest;
    private int numTreeMax; //B
    private int numTrees;
    
    public Forest(int B) {
        this.numTreeMax = B;
        this.forest = new ArrayList<Tree<V, E>>();
        this.numTrees = 0;
    }
    
    
    /**
        append newly learned decision tree to forest
        
    */
    public boolean addTree(Tree<V,E> t) {
        if (this.numTrees == this.numTreeMax) {
            return false;
        }
        this.forest.add(t);
        this.numTrees++;
        return true;
    }
    
    public int getSize() {
        return this.numTrees;
    }
}