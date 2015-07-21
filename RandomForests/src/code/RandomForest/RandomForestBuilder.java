/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code.RandomForest;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import code.Trees.*;

/**
    the abstract class/interface for a random forest
*/  
public abstract class RandomForestBuilder {
    protected ArrayList<HashSet<String>> masterAttributes;
    protected HashSet<String> remainingAttributes;
    protected ArrayList<Example> examples;
    protected HashSet<String> attributes;
    protected HashSet<String> outputClasses;
    protected Tree<String, String> tree;
    protected int numBags;
    
    /**
        the constructor
        @param exs the examples
        @param ma the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
    */
    public RandomForestBuilder(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.examples = exs;
        this.masterAttributes = ma;
        this.tree = new Tree<String, String>();
    }

    /**
        making the tree is naturally done recursively, but it is more memory efficient to do it iteratively...
        @param currentExamples the examples that still havent been used for training the tree yet
        @param remainingAttributes the hashSet of remaining attributes
        @return a subtree of trained nodes
    */
    public abstract ArrayList<Tree<String, String>> makeForest(
        ArrayList<Example> currentExamples, HashSet<String> attributes, int numBags)
        throws Exception;
}