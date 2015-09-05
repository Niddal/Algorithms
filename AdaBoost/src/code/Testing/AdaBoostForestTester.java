/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Testing;

import code.Trees.*;
import code.Examples.*;
import code.Traditional.*;
import code.AdaBoostForest.*;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;

/**
    class that tests a decision tree on test data. This is used to test the output traditional decision tree, 
    the output genetic tree, and the fitness of intermediate genetic trees using training data
*/
public class AdaBoostForestTester {
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
    
    public AdaBoostForestTester(ArrayList<Example> testExamples, ArrayList<HashSet<String>> ma,
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
    public void test(AdaBoostForest forest) {
        assert (forest != null);
        String aggregatePredictedClass = "";
        //store the output class of the tree and how many votes that tree has
        Map<String, Double> predictionsByForest = new HashMap<String, Double>();
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
        
        //values from the forest
        Map<Tree<String, String>, Double> hypothesisWeights = forest.getHypothesisWeights();
        
        
        for (Example currentExample : this.testExamples) {
            predictionsByForest = new HashMap<String, Double>(); //reset
            //System.out.println("example");
            for (Tree<String, String> tree : forest.getForest()) {
                currentNode = traverseTree(tree.getRoot(), currentExample);
                assert (currentNode.isLeaf());
                predictedClass = currentNode.getOutputClass();
                assert (predictedClass != null);
                assert (hypothesisWeights.get(tree) >= 0.0);
                //if the class has already been put:
                if (predictionsByForest.get(predictedClass) != null) {
                    predictionsByForest.put(predictedClass, predictionsByForest.get(predictedClass) + hypothesisWeights.get(tree));
                } else {
                    predictionsByForest.put(predictedClass, hypothesisWeights.get(tree));
                }
                //System.out.println("weight: " + hypothesisWeights.get(tree));
            }
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
    private String majorityVote(Map<String, Double> in) {
        //find the plurality classifaction of these examples by weighted majority vote
        //HashMap<String, Double> classCounter = new HashMap<String, Double>(); //a frequency counter for each class
        Double count = 0.0;
        Double highestCount = 0.0;
        String pluralityClass = null;
        for (String s : in.keySet()) {
            if (in.get(s) > highestCount) {
                highestCount = in.get(s);
                pluralityClass = s;
            }
        }
        //System.out.println("highest count: " + highestCount);
        if (pluralityClass == null) {

            System.err.println("ERROR: plurality");
            System.exit(1);
        }
        return pluralityClass;
    }
    public boolean correct(Tree<String, String> t, Example e) {
        assert (t != null);
        assert (e != null);
        String predictedClass = "";
        String actualClass = "";
        Node<String, String> currentNode;
     
        currentNode = traverseTree(t.getRoot(), e);
        assert (currentNode.isLeaf());
        predictedClass = currentNode.getOutputClass();
        return predictedClass.equals(e.getTrueClassification());
    }

}