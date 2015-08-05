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
import code.Traditional.*;

/**
    the abstract class/interface for a random forest
*/  
public class RandomForest implements RandomForestBuilder {
    private ArrayList<HashSet<String>> masterAttributes;
    private ArrayList<Example> examples;
    private HashSet<String> attributes;
    private HashSet<String> outputClasses;
    private Forest<String, String> forest; 
    private int numAttributes;   
    private int numBags;
    private int subsetSize;
    private Random rand;
    
    /**
        the constructor
        @param exs the examples
        @param ma the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
    */
    public RandomForest(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses, int numBags, int subsetSize) {
        //super(exs, ma, attributes, outputClasses);
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.examples = exs;
        this.masterAttributes = ma;
        this.numBags = numBags;
        this.forest = new Forest<String,String>(numBags);
        this.numAttributes = attributes.size();
        this.subsetSize = subsetSize;
        rand = new Random();
    }

    /**
        making the tree is naturally done recursively, but it is more memory efficient to do it iteratively...
        @param currentExamples the examples that still havent been used for training the tree yet
        @param remainingAttributes the hashSet of remaining attributes
        @return a subtree of trained nodes
    */
    @Override
    public Forest<String,String> trainForest(int bagSize) throws Exception {
        //maybe bagsize can vary??
        boolean b = false;
        TraditionalDecisionTreeBuilder tdtb;
        assert (this.attributes != null);
        assert (this.attributes.size() > 0);
        
        if (numBags == 0) {
            System.err.println("forest size is defined to be zero");
            return null;
        }
        for (int i =0; i < this.numBags; i++) {
            assert (this.forest.getSize() == i);
            //acquire bootstrap data of a certain size
            ArrayList<Example> subset = this.getRandomSubset(bagSize);
            //System.out.println("training tree " +i + " on " + subset.size() + " Examples");

            //train a new tree with bagged data
            HashSet<String> copy = this.deepCopy(this.attributes);
            assert (copy.size() == this.numAttributes);
            tdtb = new GreedyInformationGainDecisionTreeForForest(subset, this.masterAttributes,
                copy, this.outputClasses, true, subsetSize); //true meaning use IG
            
            Tree<String, String> t = tdtb.makeDecisionTree(subset, copy, null);
            assert (t != null);
            b = this.forest.add(t, i);
            assert (b);
        }
        return this.forest;    
    }
    /**
        sample with replacement
    */
    private ArrayList<Example> getRandomSubset(int size) {
        int j;
        ArrayList<Example> r = new ArrayList<Example>(size);
        for (int i = 0; i < size; i++) {
            j = rand.nextInt(this.examples.size());
            r.add(i, this.examples.get(j));
        }
        return r;
    }
    private HashSet<String> deepCopy(HashSet<String> h) {
        HashSet<String> n = new HashSet<String>();
        for(String e : h) {
            n.add(e);
        }
        return n;
    }
}