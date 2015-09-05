/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code.AdaBoostForest;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import code.Trees.*;
import code.Testing.*;
import code.Traditional.*;
import java.util.Collection;

/**
    the abstract class/interface for a random forest
*/  
public class AdaBoostForest implements AdaBoostForestBuilder {
    private ArrayList<HashSet<String>> masterAttributes;
    private ArrayList<Example> examples;
    private HashSet<Example> masterExamples;
    private HashSet<String> attributes;
    private HashSet<String> outputClasses;
    private Forest<String, String> forest; 
    private int numAttributes;   
    private Random rand;
    private Map<Tree<String, String>, Double> hypothesisWeights;
    private int ensembleSize = 0;
    private AdaBoostForestTester tester;
    private DecisionTreeTester treeTester;
    private int treeDepth;
    
    /**
        the constructor
        @param exs the examples
        @param ma the list of all attributes and valid values
        @param attributes a hashset of attributes
        @param outputClasses a hashset of output classes
    */
    public AdaBoostForest(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses, int ensambleSize, int depth) {
        //super(exs, ma, attributes, outputClasses);
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.masterExamples = new HashSet<Example>(deepCopy(exs));
        this.masterAttributes = ma;
        this.ensembleSize = ensambleSize;
        this.forest = new Forest<String,String>(ensambleSize);
        this.numAttributes = attributes.size();
        double temp = 1/(double) exs.size();
        for (Example e : this.masterExamples) {
            e.setWeight(temp);
        }
        this.hypothesisWeights = new HashMap<Tree<String, String>, Double>();
        rand = new Random();
        this.tester = new AdaBoostForestTester(this.examples, this.masterAttributes, this.attributes, this.outputClasses);
        this.treeTester = new DecisionTreeTester(this.examples, this.masterAttributes, this.attributes, this.outputClasses);
        this.treeDepth = depth;
    }

    /**
        train the adaboost forest
        @param k the ensamble size
    */
    @Override
    public Forest<String,String> trainForest() throws Exception {
        //maybe bagsize can vary??
        boolean b = false;
        TraditionalDecisionTreeBuilder tdtb;
        double error = 0;
        double newValue = 0;
        assert (this.attributes != null);
        assert (this.attributes.size() > 0);
        
        
        if (this.ensembleSize == 0) {
            System.err.println("forest size is defined to be zero");
            return null;
        }
        
        
        for (int i =0; i < this.ensembleSize; i++) {
            assert (this.forest.getSize() == i);
            System.out.println("training tree " +i);
            HashSet<String> copy = this.deepCopy(this.attributes);
            assert (copy.size() == this.numAttributes);

            //select samples for training set for this tree
            this.examples = selectNewExamplesFromDistribution(this.masterExamples);
            //train tree on the selected multiset of samples drawn i.i.d from distribution
            tdtb = new GreedyInformationGainDecisionTree(this.examples, this.masterAttributes,
                 copy, this.outputClasses, true, this.treeDepth); //true meaning use IG         
             Tree<String, String> t = tdtb.makeDecisionTree(this.examples, copy, null);
             assert (t != null);
             b = this.forest.add(t, i);
             assert (b);
             
             //now calculate error. Test the entire tree on the training set, 
             //regardless if not all examples were selected for training
             this.resetCorrectness(this.masterExamples);
             this.treeTester.setExamples(new ArrayList<Example>(this.masterExamples));
             this.treeTester.test(t);
            // System.out.println(this.treeTester.printPerformanceMetrics());
             
             error = 0.0;
             int countWrong = 0;
             for (Example e : this.masterExamples) {
                if (!e.isCorrect()) {
                    countWrong++;
                    error = error + 1*e.getWeight();
                } else {
                    e.setCorrect();
                }
             }
             //System.out.println("sum of misclassified example weights was: " + error);

             //calculate weight of the entire tree based on its error
             double hypWeight = (0.5) * Math.log(1.0/error - 1);
             if (hypWeight < 0.0) {
                hypWeight = 0;
                System.out.println("ERROR: hypothesis weight was negative because accuracy was less than 50%");
             }
             //System.out.println("put hypothesis weight of " +  hypWeight);
             this.hypothesisWeights.put(t, hypWeight);

             //update weights of the examples based on whether they were correctly classified
             this.updateWeights(this.masterExamples, hypWeight, error);
        }
        return this.forest;    
    }
    private void resetCorrectness(HashSet<Example> e) {
        for (Example ex : e) {
            ex.setCorrect();
        }
    }

    private HashSet<String> deepCopy(HashSet<String> h) {
        HashSet<String> n = new HashSet<String>();
        for(String e : h) {
            n.add(e);
        }
        return n;
    }
    private ArrayList<Example> deepCopy(ArrayList<Example> h) {
        ArrayList<Example> n = new ArrayList<Example>();
        for(Example e : h) {
            n.add(e);
        }
        return n;
    }
    private void updateWeights(HashSet<Example> exs, double learnerWeight, double error) {
        //ArrayList<Example> n = this.deepCopy(exs);

        //compute denominator: sum of Pr(x_i) * exp(-w_t * y_i * h_t(x_i))
        double den = 0;
        double correct = 0;
        double temp = 0.0;
        for (Example e : exs) {
            correct = (e.isCorrect()) ? 1 : -1;
            den += e.getWeight() * Math.exp(-1*learnerWeight*correct);
        }
        //den = 2 * Math.sqrt(error * ( 1 - error));
        //System.out.println("denominator: " + den);
        for (Example e : exs) {
            correct = (e.isCorrect()) ? 1 : -1;
            temp = (e.getWeight() * Math.exp(-1*learnerWeight*correct))/den;
            e.setWeight(temp);
            if (!(temp >= 0 && temp <= 1)) {
                System.out.println("ERROR: probabilities getting too small to handle, try reducing the depth of the trees");
                System.exit(1);
            }
        }

        //check it's normalized:
        double check = 0.0;
        for (Example e : exs) {
            check += e.getWeight(); //Math.pow(e.getWeight(), 2);
        }
        //check = Math.sqrt(check);
      //  System.out.println("check:" + check);
        assert (check < 1.0000005 && check > 0.99999995);
    }
    public  Map<Tree<String, String>, Double> getHypothesisWeights() {
        Map<Tree<String, String>, Double> n = new HashMap<Tree<String, String>, Double>();
        for(Tree<String, String> e : this.hypothesisWeights.keySet()) {
            n.put(e, this.hypothesisWeights.get(e));
        }
        return n;
    }  
    public ArrayList<Example> selectNewExamplesFromDistribution(HashSet<Example> exs) {
        Random rand = new Random();
        double t = 0.0;
        double count = 0.0;
        ArrayList<Example> l = new ArrayList<Example>();

        for (int i = 0; i < exs.size(); i++) {
            t = rand.nextDouble();
            count = 0.0;
            for (Example e : exs) {
                count += e.getWeight();
                if (count >= t) {
                    l.add(e);
                    break;
                }
            }
            assert (count <= 1.0000005);
        }
        assert (l.size() == exs.size());
        return l;
    }
    public AdaBoostForest getAdaBoostForest() {
        return this;
    }
    public ArrayList<Tree<String, String>> getForest() {
        return this.forest.getForest();
    }
    public int getSize() {
        return this.forest.getForest().size();
    }
    private double getMagnitude(Collection<Double> l) {
        double sum = 0.0;
        for (Double d : l) {
          sum += d*d;  
        }
        return Math.sqrt(sum);
    }
}