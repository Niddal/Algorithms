/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Testing;

import code.Trees.*;
import code.Examples.*;
import code.Traditional.*;
import code.RandomForest.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;

/**
    class that tests a decision tree on test data. This is used to test the output traditional decision tree, 
    the output genetic tree, and the fitness of intermediate genetic trees using training data
*/
public class RandomForestTester {
    private ArrayList<Example> testExamples;
    private int truePositives;
    private int falsePositives;
    private int trueNegatives;
    private int falseNegatives;
    private ArrayList<HashSet<String>> masterAttributes;
    private HashSet<String> attributes;
    private HashSet<String> outputClasses;
    private int numNegatives;
    private int numPositives;
    private String positive;
    private String negative;
    private double recall;
    private double precision;
    private double accuracy;
    
    public RandomForestTester(ArrayList<Example> testExamples, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.masterAttributes = ma;
        this.testExamples = testExamples;
        this.truePositives = 0;
        this.falsePositives = 0;
        this.trueNegatives = 0;
        this.falseNegatives = 0;
        this.numPositives = 0;
        this.numNegatives = 0;
        recall = 0;
        precision = 0;
        accuracy = 0;
        assert (outputClasses.size() == 2);
        int j = 0;
        for (String s : outputClasses) {
            if (j == 1) {
                this.negative = s;
            } else {
                this.positive = s;
            }
            j++;
            
        }
        //assert(this.positive.equals("republican"));
        //assert(this.negative.equals("democrat"));
    }
    public void setExamples(ArrayList<Example> exs) {
        this.testExamples = exs;
    }
    
    /**
        tests a tree with example data. records precidion, accuracy, and recall.
        @param tree the tree to test
    */
    public void test(Forest<String, String> forest) {
        assert (forest != null);
        String aggregatePredictedClass = "";
        ArrayList<String> predictionsByForest = new ArrayList<String>();
        String actualClass = "";
        String predictedClass = "";
        Node<String, String> currentNode;
        this.truePositives = 0;
        this.falsePositives = 0;
        this.trueNegatives = 0;
        this.falseNegatives = 0;
        this.numPositives = 0;
        this.numNegatives = 0;
        this.recall = 0;
        this.precision = 0;
        this.accuracy = 0;
        
        
        for (Example currentExample : this.testExamples) {
            predictionsByForest = new ArrayList<String>(); //reset
            for (Tree<String, String> tree : forest.getForest()) {
                currentNode = traverseTree(tree.getRoot(), currentExample);
                assert (currentNode.isLeaf());
                predictedClass = currentNode.getOutputClass();
                assert (predictedClass != null);
                predictionsByForest.add(predictedClass);
            }
            assert (predictionsByForest.size() == forest.getSize());
            aggregatePredictedClass = majorityVote(predictionsByForest);
            if (aggregatePredictedClass.equals(currentExample.getTrueClassification())) {
                    //then a true positive, correctly labelled
                    
                    if (aggregatePredictedClass.equals(this.positive)) {
                        this.truePositives++;
                        this.numPositives++;
                    } else {
                        this.trueNegatives++;
                        this.numNegatives++;
                    }
            } else {
                    if (aggregatePredictedClass.equals(this.negative)) {
                        this.falseNegatives++;
                        this.numNegatives++;
                    } else {
                        this.falsePositives++;
                        this.numPositives++;
                    }
                }
            
            }
            assert (this.numNegatives + this.numPositives == this.testExamples.size());
            this.recall =  this.truePositives/ (double) this.numPositives;
            this.precision = this.truePositives/((double) this.truePositives + this.falseNegatives);
            this.accuracy = (this.truePositives + this.trueNegatives)/((double) this.testExamples.size()); 
    }
    /**
        get some stuff
        return stuff
    */
    public double getAccuracy() {
        return this.accuracy;
    }
    /**
        get some stuff
        return stuff
    */
    public double getPrecision() {
        return this.precision;
    }
    /**
        get some stuff
        return stuff
    */
    public double getRecall() {
        return this.recall;
    }
    /**
        print some 
        return stuff
    */
    public String printPerformanceMetrics() {
        
        String s =  "-------------Results---------------\n";
        s += "Number of tested examples:      " + this.testExamples.size() + "\n";
        s += "Number of classified positives: " + this.numPositives + "\n";
        s += "Number of classified negatives: " + this.numNegatives + "\n";
        s += "-----------------------------------\n";

        s += "Number of true positives:       " + this.truePositives + "\n";
        s += "Number of false positives:      " + this.falsePositives + "\n";
        s += "Number of true negatives:       " + this.trueNegatives + "\n";
        s += "Number of false negatives:      " + this.falseNegatives + "\n";
        s += "-----------------------------------\n";

        s += "Recall:                         " + recall + "\n";
        s += "Precision:                      " + precision + "\n";
        s += "Accuracy:                       " + accuracy + "\n";
        
        
        
        return s;
    }
    private Node<String, String> traverseTree(Node<String, String> curr, Example ex) {
        String whichAttribute = "";
        String currentValue = null;
        Node<String, String> next = null;
        if (curr.isLeaf()) {
            assert (this.outputClasses.contains(curr.getOutputClass()));
            return curr;
        } else if (curr == null) {
            System.err.println("ERROR: tree traversal ran into null");
            System.exit(1);
        }
        whichAttribute = curr.getAttribute();
        //assert(this.attributes.contains(whichAttribute));
        assert (whichAttribute != null);
        currentValue = ex.getAttributeValue(whichAttribute);
        assert (currentValue != null);
        //perhaps check that currentValue is a valid attribute value for the attribute whichAttribute...
        next = curr.getChild(currentValue);
        assert (next != null);
        return traverseTree(next, ex);

        
    }
    /**
        traverse tree to get an accurate nodeCount
    */
    public int countNodes(Node<String, String> curr) {
        int numNodes = 1;
        for (Node<String, String> child : curr.getChildren().values()) {
            numNodes += countNodes(child);
        }
        return numNodes;
    
    }
    private String majorityVote(ArrayList<String> in) {
        //find the plurality classifaction of these examples
        HashMap<String, Integer> classCounter = new HashMap<String, Integer>(); //a frequency counter for each class
        int count;
        int highestCount = 0;
        String pluralityClass = null;
        //initialize counter:
        for (String s : this.outputClasses) {
            classCounter.put(s, 0);
        }
        for (String temp : in) {
            if (!classCounter.containsKey(temp) || !outputClasses.contains(temp)) {
                System.err.println("ERROR: bad output class");
                System.exit(1);
            } else {
                classCounter.put(temp, classCounter.get(temp) + 1); //increment counter    
            }
        } 
        //find highest count
        //Iterator<Map.Entry> it = classCounter.entrySet().iterator();
        for (String s : classCounter.keySet()) {
            if (classCounter.get(s) > highestCount) {
                highestCount = classCounter.get(s);
                pluralityClass = s;
            }
        }
        if (pluralityClass == null) {
            System.err.println("ERROR: plurality");
            System.exit(1);
        }
        return pluralityClass;
    }

}