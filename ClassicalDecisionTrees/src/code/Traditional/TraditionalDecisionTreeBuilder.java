/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code.Traditional;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import code.Trees.*;

/**
    the class that builds a traditional decision tree
*/  
public abstract class TraditionalDecisionTreeBuilder {
    protected ArrayList<HashSet<String>> masterAttributes;
    protected HashSet<String> remainingAttributes;
    protected ArrayList<Example> examples;
    protected HashSet<String> attributes;
    protected HashSet<String> outputClasses;
    protected Tree<String, String> tree;
    
        /**
        the constructor
        @param exs the examples
        @param ma the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
    */
    public TraditionalDecisionTreeBuilder(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.examples = exs;
        this.masterAttributes = ma;
        this.tree = new Tree<String, String>();
    }
    /**
        returns argmax a of all attribtes subject to their respective importance. Importance
        is determined by the information gain of splitting  the training set E into d subsets, E_1...E_d,
        based on the d values that Attribute A can assume.  
        @param attribute the attribute on which to 
        
        @return the most important attribute, which will be greedily selected for decision tree expansion 
    */
    protected abstract String getMostImportantAttribute(HashSet<String> allAttributes, ArrayList<Example> exs);
    /**
        making the tree is naturally done recursively, but it is more memory efficient to do it iteratively...
        @param currentExamples the examples that still havent been used for training the tree yet
        @param remainingAttributes the hashSet of remaining attributes
        @return a subtree of trained nodes
    */
    public abstract Tree<String, String> makeDecisionTree(
        ArrayList<Example> currentExamples, HashSet<String> remainingAttributes, Node<String, String> parent)
        throws Exception;
    /**
        gets the plurality classification of a set of examples
        @param exs an arraylist of examples
        @return the plurality
    */
    protected abstract String getPluralityValue(ArrayList<Example> exs);
    /**
        a method that will be overriden to get the importance of an attribute...will be overriden by the
        implementation as either Information Gain or Gain Ratio...
        
        @param attribute the attribute to evaluate
        @param exs the examples over which to evaluate the importance of the attribute in question
        @return the importance as a real positive number;
    */
    protected abstract double getImportance(String attribute, ArrayList<Example> exs);
    /**
        partitions examples by the values of a particular attribute, can be an expensive operation
        
        @param attributeValues an efficient (constant time access) set of valid values for a particular attribute
        @param examplesToPartition an arraylist of examples
        @return a dictionary mapping attribute A's values to a list of examples that share that value for that attribute
        note the arraylists in the output are disjoint, and their union is the original input examples
    */
    protected abstract ArrayList<Example> partitionExamplesByAttributeValue(
        ArrayList<Example> examples, int attributeAsItsIntegerIndex, String valueToSplitOn);
       
    /**
        pretty obvious
        @param a even more obvious
        @return omg
    */ 
    public boolean isValidAttribute(String a) {
        return attributes.contains(a);
    }
    /**
        pretty obvious
        @param a even more obvious
        @return omg
    */
    public boolean isValidClass(String a) {
        return outputClasses.contains(a);
    }
}