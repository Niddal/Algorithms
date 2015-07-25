/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/

package code.Trees;

import code.Trees.Tree;
import java.util.ArrayList;

public class Forest<V, E> {
    private ArrayList<Tree<V, E>> forest;
    private int numTreeMax = 0; //B
    private int numTrees;
    private boolean set = false;
    
    public Forest() {
        this.forest = new ArrayList<Tree<V, E>>();
        this.numTrees = 0;
    }
    public Forest(int B) {
        this.numTreeMax = B;
        this.forest = new ArrayList<Tree<V, E>>(B);
        this.numTrees = 0;
        this.set = true;
    }
    
    
    /**
        append newly learned decision tree to forest
        
    */
    public boolean add(Tree<V,E> t) {
        if (this.numTrees == this.numTreeMax) {
            return false;
        }
        this.forest.add(t);
        this.numTrees++;
        return true;
    }
    public boolean add(Tree<V,E> t, int index) {
        if (this.numTrees == this.numTreeMax || index != this.numTrees
            || index > this.forest.size() ) {
            return false;
        }
        this.forest.add(index, t);
        this.numTrees++;
        return true;
    }
    public int getSize() {
        return this.numTrees;
    }
    public void setSize(int b) {
        if (!this.set) {
            this.numTreeMax = b;
            this.set = true;
        }
    }
}