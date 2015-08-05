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

public class CongressionalInputParser extends InputParser {
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
    
    public CongressionalInputParser() {
        super();
        this.numAttributesPerExample = 16;
        this.numExamplesToRead = 435;
        this.numExamplesRead = 0;
        
    }
    public HashSet<String> getSetOfAttributes() {
        return attributes;
    }
    public HashSet<String> initializeOutputClasses() {
        outputClasses.add("republican");
        outputClasses.add("democrat");   
        return outputClasses;
    }
    public ArrayList<HashSet<String>> initializeMasterAttributesAndValues() {
        HashSet<String> temp;
        //this for loop only exists bc congressional attribute values is are the same
        //for every attribute
        for (int i = 0; i < this.numAttributesPerExample; i++) {
            this.attributes.add(Integer.toString(i)); //record the attributes, which are strings (just integers)
            temp = new HashSet<String>(); //now record the values associated with that attribute
            temp.add("y");
            temp.add("n");
            temp.add("?");
            masterAttributes.add(temp);
        }
        assert(this.masterAttributes.size() == 16);
        assert(this.attributes.size() == 16);
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
        Example ce = null;
        HashMap<String, String> tempAttributeTable = new HashMap<String, String>(21);
        //so it never resizes (it resizes at 16, or .75 of 20)

        
        //attempt to load the file
        try {
            reader = new BufferedReader(new FileReader(
                new File("../data/CongressionalVoting/" + fileName)));
            //line = reader.readLine();
            System.out.println("File opened successfully...");
        } catch (IOException e) {
            System.err.println("ERROR: could not read file");
            System.exit(1);
        }  
        //use the specific parser to read each line bc file formats different for each dataset        
        //first character in each line is the true classification...
        while ((line = reader.readLine()) != null) { //read each line
           
            //congressional data separated by commas
            attributeValues = line.split(",");
            
            //get all data to create new example...
            tempAttributeTable = new HashMap<String, String>(21);  //make new hashtable for every example...
            trueClassification = attributeValues[0];
            //System.out.println("true class: " + trueClassification);
            assert (trueClassification.equals("democrat") || trueClassification.equals("republican"));
            for (int i = 1; i <= this.numAttributesPerExample; i++) { //start at 1, not zero for congress  
                temp = attributeValues[i];
                if (temp.equals("y") && masterAttributes.get(i-1).contains(temp)) {
                    tempAttributeTable.put(Integer.toString(i-1), "y");
                } else if (temp.equals("n") && masterAttributes.get(i-1).contains(temp)) {
                    tempAttributeTable.put(Integer.toString(i-1), "n");
                } else if (temp.equals("?") && masterAttributes.get(i-1).contains(temp)) {
                    tempAttributeTable.put(Integer.toString(i-1), "?");
                } else {
                    System.err.println("ERROR: not a valid attribute for congressional dataset");
                    System.exit(1); 
                }
            
            }
            
            //make the new example
            assert (tempAttributeTable.size() == 16);
            ce = new CongressionalExample(lineCounter, tempAttributeTable, trueClassification);
            examples.add(ce);
            lineCounter++;
            numExamplesRead++;
        }
        //this.numAttributes = lineCounter+1;

        assert (lineCounter == this.numExamplesRead);
        assert (examples.size() == lineCounter);
        System.out.println("Successfully read " + numExamplesRead + " examples from file...");
        return examples;
    }
}