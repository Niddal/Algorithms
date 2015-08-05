/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/

package code.Traditional;

import code.Trees.*;
import code.Examples.*;
import code.Traditional.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GreedyInformationGainDecisionTree extends TraditionalDecisionTreeBuilder {

/*----- instance variables */
    private boolean IG = false;

    
    /**
        the constructor
        @param exs the examples
        @param masterAttributes the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
        @param ig whether it's ig or gr
    */
    public GreedyInformationGainDecisionTree(ArrayList<Example> exs, 
        ArrayList<HashSet<String>> masterAttributes, HashSet<String> attributes, 
        HashSet<String> outputClasses, boolean ig) {
        super(exs, masterAttributes, attributes, outputClasses);  
        this.IG = ig; //information gain or not?   
   
    }
    
/*----- implemented methods */
    @Override
    protected String getMostImportantAttribute(HashSet<String> remainingAttributes, ArrayList<Example> exs) {
        double largestImportance = -1;
        double temp = 0.0;
        String attributeToSplitOn = null;
        for (String att : remainingAttributes) {
            temp = getImportance(att, exs);
            //System.out.println(" - importance " + temp + " found for attribute " + att);
            if (temp > largestImportance) {
                attributeToSplitOn = att;
                largestImportance = temp; 
            }
        }
        if (attributeToSplitOn == null || !isValidAttribute(attributeToSplitOn)) {
            System.err.println("ERROR: can't find attribute to split on in function getMostImportantAttribute ");
            System.exit(2);
        }
        assert (attributeToSplitOn != null);
        return attributeToSplitOn;
        
    }
    @Override
    public Tree<String, String> makeDecisionTree(
        ArrayList<Example> currentExamples, HashSet<String> rmAttributes, Node<String, String> parent) 
        throws Exception {
        
        assert (currentExamples != null);
        assert (rmAttributes != null);
        
        assert (this.remainingAttributes == null);
        this.remainingAttributes = rmAttributes; //a class variable essentially
        
        Node<String, String> root = recursiveTreeBuilder(currentExamples, remainingAttributes, parent);
        assert (tree.getRoot().equals(root));
        return root.getManufacturer();
    }
    private Node<String, String> recursiveTreeBuilder(
        ArrayList<Example> currentExamples, HashSet<String> remainingAttributes, Node<String, String> parent) 
        throws Exception {
                
        if (currentExamples.isEmpty()) { //if no more examples then get plurality of parent examples as leaf
            String classification =  getPluralityValue(parent.getExamples());
            Node<String, String> leaf = tree.makeNode(classification, (parent.getDepth() + 1), true, currentExamples, parent);
            //tree.connectNodes(parent, leaf, classification); //FIX FIX FIX
            return leaf;
        } else if (doExamplesHaveSameClass(currentExamples)) { //if all exs have same class make leaf node their class
            String classification = currentExamples.get(0).getTrueClassification();
            Node<String, String> leaf = tree.makeNode(classification, (parent.getDepth() + 1), true, currentExamples, parent);
            //tree.connectNodes(parent, leaf, classification);
            return leaf;
        } else if (remainingAttributes.isEmpty()) { //no more attributes to split on, but we still have examples
            //return plurality of examples
            String classification =  getPluralityValue(currentExamples);
            assert(parent!=null);
            Node<String, String> leaf = tree.makeNode(classification, (parent.getDepth() + 1), true, currentExamples, parent);
            return leaf;
        
        } else { //split examples on most important attribute normally, or as per random forest algorithm,
            //split on the best attribute from a subset of randomly chosen attributes
            int numValuesForAttribute = 0;
            String value;
            int attributeIndex;
            ArrayList<Example> exs = null;
            Node<String, String> subtree;
            
            
                        String attributeToSplitOn = getMostImportantAttribute(remainingAttributes, currentExamples);
            // System.out.println(" \n ** attribute to split on is " + attributeToSplitOn + " ** \n");
            attributeIndex = Integer.parseInt(attributeToSplitOn);
            try {
                numValuesForAttribute = masterAttributes.get(attributeIndex).size();
            } catch (NumberFormatException e) {
                System.err.println("ERROR: bad attribute");
                System.exit(2);
            }
            Node<String, String> internalNode;
            if (parent == null) { //we are making root...
                internalNode = tree.makeNode(attributeToSplitOn, 0, false, currentExamples, parent);

            } else {
                internalNode = tree.makeNode(attributeToSplitOn, (parent.getDepth() + 1), false, currentExamples, parent);
            }

            for (String v : masterAttributes.get(attributeIndex)) {
                exs = partitionExamplesByAttributeValue(currentExamples, attributeIndex, v);
                boolean success = remainingAttributes.remove(attributeToSplitOn);
                subtree = recursiveTreeBuilder(exs, remainingAttributes, internalNode);
                //add an edge with value v from the internal node (parent) to the child (subtree).
                tree.connectNodes(internalNode, subtree, v);
            }
            return internalNode;
        
        }

    
    
    }
    
    
    @Override
    protected String getPluralityValue(ArrayList<Example> exs) {
        //find the plurality classifaction of these examples
        HashMap<String, Integer> classCounter = new HashMap<String, Integer>(); //a frequency counter for each class
        String temp = "";
        int count;
        int highestCount = 0;
        String pluralityClass = null;
        //initialize counter:
        for (String s : outputClasses) {
            classCounter.put(s, 0);
        }
        //TODO: implement
        for (Example x : exs) {
            temp = x.getTrueClassification();
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
    @Override
    protected double getImportance(String attribute, ArrayList<Example> exs) {
        if (!IG) {
            return calculateInformationGain(attribute, exs);
        } else {
            return calculateGainRatio(attribute, exs);
        }
    }
    private double calculateGainRatio(String attribute, ArrayList<Example> exs) {
        double informationGain = calculateInformationGain( attribute,  exs);
        double gainRatio = 0;
        
        assert (informationGain >= 0);
        int index = -1;
        double sum = 0; //the remaining entropy after splitting on attribute A
        HashSet<String> valuesForAttributeA = null;
        double proportion = 0;
        ArrayList<Example> subsetOfExamplesWithAttributeValue = null;
        
        try {
            index = Integer.parseInt(attribute);
            valuesForAttributeA = masterAttributes.get(index);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: bad attribute");
            System.exit(1);
        }
        if (!attributes.contains(attribute)) {
            System.err.println("ERROR: bad output class");
            System.exit(1);
        }
        assert (exs.size() > 0);
        for (String val : valuesForAttributeA) {
            //partition exs on the attribute A, then count the number of 
            subsetOfExamplesWithAttributeValue = partitionExamplesByAttributeValue(exs, index, val);
            //System.out.println(" === size of partition: " + subsetOfExamplesWithAttributeValue.size());
            if (subsetOfExamplesWithAttributeValue.size() == 0) {
                continue;
            } else {
                //System.out.println(" === some examples had value: " + val + " for attribute: " + index);
                assert (subsetOfExamplesWithAttributeValue.size() > 0);
                proportion = ((double) subsetOfExamplesWithAttributeValue.size())/ exs.size();
                assert (proportion > 0);
                if (proportion == 1.0) {
                    return informationGain; //bc if all of the examples have the same value for this attribute, then we are done
                    //this is the most valuable attribute
                }
             
                double temp1 = proportion;
                double temp = Math.log(temp1);
                assert (temp < 0);
                proportion = proportion * temp;
                assert (proportion != 0.0);
                assert (proportion <= 0);
                sum += proportion;
                
                
            }
        }
        //System.out.println("sum: " + sum);
        sum = (-1)*sum;
        assert (sum > 0);
        gainRatio = informationGain/sum;
        assert (!Double.isNaN(gainRatio));
        return gainRatio;
        
    }
    private double calculateInformationGain(String attribute, ArrayList<Example> exs) {
        double IG = getEntropy(exs); //the entropy of the currentExamples
        //System.out.println("* Information Gain of currentExamples is: " + IG);
        assert (IG > 0);
        int index = -1;
        double tempEntropy = 0;
        double remainder = 0; //the remaining entropy after splitting on attribute A
        HashSet<String> valuesForAttributeA = null;
        double proportion = 0;
        ArrayList<Example> subsetOfExamplesWithAttributeValue = null;
        
        try {
            index = Integer.parseInt(attribute);
            valuesForAttributeA = masterAttributes.get(index);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: bad attribute");
            System.exit(1);
        }
        if (!attributes.contains(attribute)) {
            System.err.println("ERROR: bad output class");
            System.exit(1);
        }
        
        for (String val : valuesForAttributeA) {
            //partition exs on the attribute A, then count the number of 
            subsetOfExamplesWithAttributeValue = partitionExamplesByAttributeValue(exs, index, val);
            //System.out.println(" === size of partition: " + subsetOfExamplesWithAttributeValue.size());
            if (subsetOfExamplesWithAttributeValue.size() == 0) {
                continue;
            } else {
                //System.out.println(" === some examples had value: " + val + " for attribute: " + index);
                proportion = ((double) subsetOfExamplesWithAttributeValue.size())/ exs.size();
                assert (proportion > 0);
                tempEntropy = getEntropy(subsetOfExamplesWithAttributeValue);
                //assert (tempEntropy > 0); //we allow IG's of zero...
                remainder += proportion*tempEntropy;
            }
        }
        
        if ( remainder > IG || remainder < 0) {
            System.err.println("ERROR: bad information gain; negative or too big for some reason");
            System.exit(1);
        }
        //assert (IG != remainder); MAYBE ALLOW THIS TOO??
        IG = IG - remainder;
        assert (!Double.isNaN(IG));
        return IG;
    }
    private boolean doExamplesHaveSameClass(ArrayList<Example> exs) {
        String classification = exs.get(0).getTrueClassification();
        for (Example e : exs) {
             if ( !e.getTrueClassification().equals(classification)) {
                return false;
             }
        }
        return true;
    }
    private double getEntropy(ArrayList<Example> ex) {
        double entropy = 0;
        double propExamplesWithThisClass = 0;
        String whichClass = "";
        assert (ex.size() > 0);
        //for (int i = 0; i < outputClasses.size(); i++) {
        for (String s : outputClasses) {
            whichClass = s;
            propExamplesWithThisClass = getProportionOfExamplesWithClass(ex, s);
            if (propExamplesWithThisClass == 0) {
                //System.out.println("zero examples with output class: " + s);
                continue;
                //no entropy with empty set...
            } else {
                entropy += (-1)*propExamplesWithThisClass*Math.log(propExamplesWithThisClass); //make log base 2...
            }
        }
        assert (!Double.isNaN(entropy));
        return entropy; //entropy can be zero if no more examples left...
    }
    //or rather should I partition by class and return the size?
    private double getProportionOfExamplesWithClass(ArrayList<Example> ex, String c) {
        Example temp;
        double count = 0;
        assert (ex.size() > 0);
        if (!outputClasses.contains(c)) {
            System.err.println("ERROR: bad output class");
            System.exit(1);
        }
        for (int i = 0; i < ex.size(); i++) {
            temp = ex.get(i);
            if (temp.getTrueClassification().equals(c)) {
                count++;
            }   
        }
        assert (!Double.isNaN(count/ex.size()));
        return count/ex.size();
    }
    protected ArrayList<Example> partitionExamplesByAttributeValue(ArrayList<Example> ex, int att, String val) {
        Example temp;
        ArrayList<Example> subset = new ArrayList<Example>();
        double count = 0;
        
        assert (ex != null && ex.size() > 0);
        assert (att >= 0);
        assert (val != null);
        
        if (!masterAttributes.get(att).contains(val)) {
            System.err.println("ERROR: bad attribute or value");
            System.exit(1);
        }
        for (int i = 0; i < ex.size(); i++) {
            temp = ex.get(i);
            if (temp.getAttributeValue(Integer.toString(att)).equals(val)) {
                count++;
                subset.add(temp);
            }   
        }
        //return count/ex.size();
        return subset;
    }
    
    



}

