package code;

import java.util.Scanner;
import code.Trees.*;
import code.Examples.*;
import code.Parser.*;
import code.Testing.*;
import code.Traditional.*;
import code.RandomForest.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;

/**
    The driver class for a random forest of decision trees.*/

public class RandomForestDriver {
    private static final double PERCENT_TRAIN = 0.8;
/* static or class variables */
    private static TraditionalDecisionTreeBuilder traditionalDecisionTreeBuilder[];
    private static RandomForestTester tester;
    private static InputParser parser;
    private static ArrayList<HashSet<String>> masterAttributes;
    private static HashSet<String> attributes;
    private static HashSet<String> outputClasses;
    private static ArrayList<Example> examples = null;
    private static ArrayList<Example> trainingExamples = null;
    private static ArrayList<Example> testExamples = null;
    private static String fileName;


    public static void main(String[] args) throws IOException, Exception{
        //break into cases for each data set
        //for congressional, partition into training and test sets
        
        int whichDataSet = -1;
        Scanner in = new Scanner(System.in);
        String whichMonk = "";
        boolean IG = false;
        int tempIG = -1;
        
        //check input parameters
        try {
            if (args.length > 3) throw new NumberFormatException();
            tempIG = Integer.parseInt(args[0]);
            whichDataSet = Integer.parseInt(args[1]);
            //System.out.println(tempIG + " " + whichDataSet);
            if (args.length < 2 || whichDataSet < 0 || whichDataSet > 2 || tempIG < 0 || tempIG > 1) {
                throw new NumberFormatException();
            }
            //System.out.println(tempIG + " " + whichDataSet);
            if (args.length < 3 && whichDataSet == 1) {
                throw new NumberFormatException();
            }
            //System.out.println(tempIG + "* " + whichDataSet);
            if (args.length == 3) {
                int whichMonkInt = Integer.parseInt(args[2]);
                if (whichMonkInt == 1) whichMonk = "monks-1";
                else if (whichMonkInt == 2) whichMonk = "monks-2";
                else if (whichMonkInt == 3) whichMonk = "monks-3";
                else throw new NumberFormatException();
            } 
            if (tempIG == 0) IG = false;
            else IG = true;  
        } catch (NumberFormatException e ) {
            System.out.println("ERROR: \n\nUsage: ./run_traditional <IG or GR> <whichDataSet> <whichMonkDataSet> \n"
                + "IG or GR: (0) for information gain, (1) for gain ratio"
                + "\nwhichDataSet can be: (0=Congressional, 1=MONK, 2=Mushroom)"
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
                        // traditionalDecisionTreeBuilder = new GreedyInformationGainDecisionTree(
//                             trainingExamples, masterAttributes, attributes, outputClasses, IG);
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
                        // traditionalDecisionTreeBuilder = new GreedyInformationGainDecisionTree(
//                             trainingExamples, masterAttributes, attributes, outputClasses, IG);
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
                        
                       //  traditionalDecisionTreeBuilder = new GreedyInformationGainDecisionTree(
//                             trainingExamples, masterAttributes, attributes, outputClasses, IG);
                        System.out.println("\n\nsuccess: mushroom decision tree built");

                        break;
                    }
        
        }
        
        /*
        finalDecisionTree =  traditionalDecisionTreeBuilder.makeDecisionTree(trainingExamples, attributes, null);
        
        
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
        
        System.out.println("Successful completion of program");*/

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