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
import code.Examples.CongressionalExample;

public class MonkInputParser extends InputParser {
/*
    super's variables:
    
    protected int numExamplesToRead;
    protected int numExamplesRead;
    protected int numAttributesPerExample;
    protected HashSet<String> outputClasses;
    protected ArrayList<HashSet<String>> masterAttributes;
    protected HashSet<String> attributes;
    protected ArrayList<Example> examples;
    protected double percentTraining; //number on [0,1]
    protected double percentTesting; 
    protected double percentDeveloper;
*/
    
    public MonkInputParser() {
        super();
        this.numAttributesPerExample = 6;
        //this.numExamplesToRead = 435;
        this.numExamplesRead = 0;
        
    }
    public HashSet<String> getSetOfAttributes() {
        return attributes;
    }
    public HashSet<String> initializeOutputClasses() {
        outputClasses.add("0");
        outputClasses.add("1");   
        return outputClasses;
    }
    public ArrayList<HashSet<String>> initializeMasterAttributesAndValues() {
        HashSet<String> one = new HashSet<String>();
        this.attributes.add("0");
        one.add("1");
        one.add("2");
        one.add("3");
        this.masterAttributes.add(one);
        
        HashSet<String> two = new HashSet<String>();
        this.attributes.add("1");
        two.add("1");
        two.add("2");
        two.add("3");
        this.masterAttributes.add(two);
        
        HashSet<String> three = new HashSet<String>();
        this.attributes.add("2");
        three.add("1");
        three.add("2");
        this.masterAttributes.add(three);
        
        HashSet<String> four = new HashSet<String>();
        this.attributes.add("3");
        four.add("1");
        four.add("2");
        four.add("3");
        this.masterAttributes.add(four);
        
        HashSet<String> five = new HashSet<String>();
        this.attributes.add("4");
        five.add("1");
        five.add("2");
        five.add("3");
        five.add("4");
        this.masterAttributes.add(five);
        
        HashSet<String> six = new HashSet<String>();
        this.attributes.add("5");
        six.add("1");
        six.add("2");
        this.masterAttributes.add(six);

        return masterAttributes;
    }
    public ArrayList<Example> readExamples(String fileName) throws IOException {
        String line = "";
        String trueClassification = null;
        String[] attributeValues; 
        String temp;
        BufferedReader reader = null;

        this.examples = new ArrayList<Example>();

        int lineCounter = 0;
        this.numExamplesRead = 0;
        Example ce = null;
        HashMap<String, String> tempAttributeTable = new HashMap<String, String>(21);
        //so it never resizes (it resizes at 16, or .75 of 20)

        
        //attempt to load the file
        try {
            reader = new BufferedReader(new FileReader(
                new File("../data/MONK/" + fileName)));
            //line = reader.readLine();
            System.out.println("File opened successfully...");
        } catch (IOException e) {
            System.err.println("ERROR: could not read file");
            System.exit(1);
        }  
        //use the specific parser to read each line bc file formats different for each dataset        
        //first character in each line is the true classification...
        while ((line = reader.readLine()) != null) { //read each line
            line = line.trim();
           
            //congressional data separated by commas
            attributeValues = line.split(" ");
            
            //get all data to create new example...
            tempAttributeTable = new HashMap<String, String>(10);  //make new hashtable for every example...
            trueClassification = attributeValues[0];
            //System.out.println("true class: " + trueClassification);
            assert (trueClassification.equals("0") || trueClassification.equals("1"));
            for (int i = 1; i <= this.numAttributesPerExample; i++) { //start at 1, not zero for monk  
                temp = attributeValues[i];
                if (masterAttributes.get(i-1).contains(temp)) {
                    //temp = "" + (Integer.parseInt(temp) - 1);
                    tempAttributeTable.put(Integer.toString(i-1), temp);
                } else {
                    System.err.println("ERROR: not a valid attribute for congressional dataset");
                    System.exit(1); 
                }
            
            }
            
            //make the new example
            assert (tempAttributeTable.size() == 6);
            ce = new CongressionalExample(lineCounter, tempAttributeTable, trueClassification);
            examples.add(ce);
            lineCounter++;
            numExamplesRead++;
        }
        System.out.println(lineCounter);

        assert (lineCounter == this.numExamplesRead);
        assert (examples.size() == lineCounter);
        System.out.println("Successfully read " + numExamplesRead +  " examples from file...");
        return examples;
    }
}