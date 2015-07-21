/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Parser;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import code.Examples.Example;

/**
    NOTE: MY SPECIFICATIONS *REQUIRE* A CUSTOM PARSER FOR EACH DATA SET
    that follows the interface below...
*/

public abstract class InputParser {
    /* following variables instantiated by initializeDataMetricsMethod */
    protected int numExamplesToRead;
    protected int numExamplesRead;
    protected int numAttributesPerExample;

    /* following two data structures initialized by initializeMasterAttributesAndValues and 
    initializeOutputClasses, respectively */
    protected HashSet<String> outputClasses;
    protected ArrayList<HashSet<String>> masterAttributes; //each HashSet corresponds to the 
    protected HashSet<String> attributes;
    //valid values that the nth attribute can take on, the nth attribute is at index n in the list
    
    /* following arraylist created by the readExamples function */
    protected ArrayList<Example> examples;
    
    /* instantiated by constructor */
    protected double percentTraining; //number on [0,1]
    protected double percentTesting; 
    protected double percentDeveloper;
    
/*----- constructor will be overriden -----*/
    public InputParser(/*double train, double test, double dev*/) {
        // this.percentTraining = train;
//         this.percentTesting = test;
//         this.percentDeveloper = dev;
        
        this.outputClasses = new HashSet<String>();
        this.masterAttributes = new ArrayList<HashSet<String>>();
        this.attributes = new HashSet<String>();
    }
    
/*----- abstract methods, must be overriden by child classes that are more
       particular to the data set ----- */
    public abstract HashSet<String> initializeOutputClasses();
    public abstract ArrayList<HashSet<String>> initializeMasterAttributesAndValues();
    public abstract HashSet<String> getSetOfAttributes();
    public abstract ArrayList<Example> readExamples(String fileName) throws IOException;
    public HashSet<String> getAttributesSet() {
        assert (this.attributes != null);
        return this.attributes;
    }
}