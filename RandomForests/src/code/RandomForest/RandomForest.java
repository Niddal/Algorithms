/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code.RandomForest;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import code.Trees.*;

/**
    the abstract class/interface for a random forest
*/  
public class RandomForest extends RandomForestBuilder {
    protected ArrayList<HashSet<String>> masterAttributes;
    protected HashSet<String> remainingAttributes;
    protected ArrayList<Example> examples;
    protected HashSet<String> attributes;
    protected HashSet<String> outputClasses;
    protected Tree<String, String> tree;
    protected int numBags;
    private Random rand;
    
    /**
        the constructor
        @param exs the examples
        @param ma the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
    */
    public RandomForest(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        super(exs, ma, attributes, outputClasses);
        rand =new Random();
    }

    /**
        making the tree is naturally done recursively, but it is more memory efficient to do it iteratively...
        @param currentExamples the examples that still havent been used for training the tree yet
        @param remainingAttributes the hashSet of remaining attributes
        @return a subtree of trained nodes
    */
    @Override
    public Forest<String,String> trainForest(
        ArrayList<Example> currentExamples, HashSet<String> attributes, int numBags, int bagSize) {
        //maybe bagsize can vary??
        Forest<String,String> f = new Forest<String,String>(numBags);
        
        for (int i =0; i < numBags; i++) {
            //acquire bootstrap data of a certain size
            ArrayList<Example> subset = this.getRandomSubset(bagSize, currentExamples);
            //train a new tree with bagged data
        }
        return null;    
    }
    /**
        sample with replacement
    */
    private ArrayList<Example> getRandomSubset(int size, ArrayList<Example> set) {
        int j;
        ArrayList<Example> r = new ArrayList<Example>(size);
        for (int i = 0; i < size; i++) {
            j = rand.nextInt(set.size());
            r.add(i, set.get(j));
        }
        return r;
    }
}