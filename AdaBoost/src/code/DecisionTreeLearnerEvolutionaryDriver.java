/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code;

import java.util.Scanner;
import code.Trees.*;
import code.Examples.*;
import code.Parser.*;
import code.Testing.*;
import code.Evolutionary.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;

/**
    The driver class for a traditional learning decision tree that implements maximum information
    gain to greedily selects the most important attributes to split the set of examples on. This 
    driver will 1) read and parse the input examples from a .data or .train/.test files, then it
    will 2) train the trainerDecisionTree on the training data, 3) it will copy and trim the trainer
    tree into a finalized decision tree over which 4) the test data can be tested. The testing class
    will measure and record all performance statistics specified by the requirements document. 
    
    Please note the very strict separation between the training tree/training data, and the final tree/
    testing data. 
    
    Please see the README for more information
*/
public class DecisionTreeLearnerEvolutionaryDriver {
    private static final double PERCENT_TRAIN = 0.8;
/* static or class variables */
    private static EvolutionaryDecisionTreeBuilder evolutionaryDecisionTreeBuilder;
    private static Tree<String, String> finalDecisionTree; //IMPLEMENT A NEW TRIMMED VERSION OF GREEDY FOR FINAL
    private static DecisionTreeTester tester;
    private static InputParser parser;
    private static ArrayList<HashSet<String>> masterAttributes;
    private static HashSet<String> attributes;
    private static HashSet<String> outputClasses;
    private static ArrayList<Example> examples = null;
    private static ArrayList<Example> trainingExamples = null;
    private static ArrayList<Example> testExamples = null;
    private static String fileName;

    /**
        main
        @param args the command line input
    */
    public static void main(String[] args) throws IOException, Exception{
        //break into cases for each data set
        //for congressional, partition into training and test sets
        
        int whichDataSet = -1;
        Scanner in = new Scanner(System.in);
        String whichMonk = "";
        
        //check input parameters
        try {
            if (args.length > 2) throw new NumberFormatException();
            whichDataSet = Integer.parseInt(args[0]);
            if (args.length > 2 || whichDataSet < 0 || whichDataSet > 2) {
                throw new NumberFormatException();
            }
            if (args.length == 1 && whichDataSet == 1) {
                throw new NumberFormatException();
            }
            if (args.length == 2) {
                int whichMonkInt = Integer.parseInt(args[1]);
                if (whichMonkInt == 1) whichMonk = "monks-1";
                else if (whichMonkInt == 2) whichMonk = "monks-2";
                else if (whichMonkInt == 3) whichMonk = "monks-3";
                else throw new NumberFormatException();
            }   
        } catch (NumberFormatException e ) {
            System.out.println("ERROR: \n\nUsage: ./run_traditional <whichDataSet> <whichMonkDataSet> \n"
                + "whichDataSet can be: (0=Congressional, 1=MONK, 2=Mushroom)"
                + "\nwhichMonkDataSet is only relevant if Monk chosen can be: (1 2 or 3)");
            System.exit(1);
        }
        
        //cases for each data set
        switch (whichDataSet) {
            case 0: {
                        fileName = "house-votes-84.data";
                        System.out.println("\n\nRunning data set: " + fileName);
                        parser = new CongressionalInputParser();
                        outputClasses = parser.initializeOutputClasses();
                        masterAttributes = parser.initializeMasterAttributesAndValues();
                        attributes = parser.getAttributesSet();
                        examples = parser.readExamples(fileName);
                        partitionIntoTestAndTraining(examples);
                        evolutionaryDecisionTreeBuilder = new GeneticDecisionTree(
                            trainingExamples, masterAttributes, attributes, outputClasses);
                        System.out.println("\n\nsuccess: congressional decision tree built");
                        break;
                    }
            case 1: {
                        String fileName1 = whichMonk + ".train";
                        String fileName2 = whichMonk + ".test";
                        System.out.println("\n\nRunning data set: " + fileName1);
                        parser = new MonkInputParser();
                        outputClasses = parser.initializeOutputClasses();
                        masterAttributes = parser.initializeMasterAttributesAndValues();
                        attributes = parser.getAttributesSet();
                        trainingExamples = parser.readExamples(fileName1);
                        testExamples = parser.readExamples(fileName2);
                        evolutionaryDecisionTreeBuilder = new GeneticDecisionTree(
                            trainingExamples, masterAttributes, attributes, outputClasses);
                        System.out.println("\n\nsuccess: monk-" + whichMonk + " decision tree built");
                        break;
                    }
            case 2: {
                        String fileName1 = "agaricus-lepiota.data";
                        System.out.println("\n\nRunning data set: " + fileName1);
                        parser = new MushroomInputParser();
                        outputClasses = parser.initializeOutputClasses();
                        masterAttributes = parser.initializeMasterAttributesAndValues();
                        attributes = parser.getAttributesSet();
                        examples = parser.readExamples(fileName1);
                        
                        //testExamples = parser.readExamples(fileName2);
                        partitionIntoTestAndTraining(examples);
                        
                        evolutionaryDecisionTreeBuilder = new GeneticDecisionTree(
                            trainingExamples, masterAttributes, attributes, outputClasses);
                        System.out.println("\n\nsuccess: mushroom decision tree built");
                        break;
                    }
        
        }
        finalDecisionTree =  evolutionaryDecisionTreeBuilder.makeDecisionTree();

        //evolutionaryDecisionTreeBuilder.printPopulation();
        
        //System.out.println(finalDecisionTree.toString());
        
        
        
        System.out.println("\nTesting the tree on the TRAINING examples...");
        tester = new DecisionTreeTester(trainingExamples, masterAttributes, attributes, outputClasses);
        tester.test(finalDecisionTree);
        String out = tester.printPerformanceMetrics();
        System.out.println(out);
        
        System.out.println("\nTesting the tree on the TEST examples...");
        tester = new DecisionTreeTester(testExamples, masterAttributes, attributes, outputClasses);
        tester.test(finalDecisionTree);
        out = tester.printPerformanceMetrics();
        System.out.println(out);
        
         System.out.println("Successful completion of program");

    }
    private static void partitionIntoTestAndTraining(ArrayList<Example> ex) {
        int train = (int) Math.floor(PERCENT_TRAIN*ex.size());
        trainingExamples = new ArrayList<Example>();
        testExamples = new ArrayList<Example>();
        for (int i = 0; i < ex.size(); i++) {
            if (i < train) {
                trainingExamples.add(ex.get(i));
            } else {
                testExamples.add(ex.get(i));
            }
        }
        assert (trainingExamples.size() == train);
        assert (testExamples.size() == ex.size() - train);
    }

    


}