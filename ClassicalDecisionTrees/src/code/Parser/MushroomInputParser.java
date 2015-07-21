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

public class MushroomInputParser extends InputParser {
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
    
    public MushroomInputParser() {
        super();
        this.numAttributesPerExample = 22;
        this.numExamplesToRead = 8124;
        this.numExamplesRead = 0;
        
    }
    public HashSet<String> getSetOfAttributes() {
        return attributes;
    }
    public HashSet<String> initializeOutputClasses() {
        outputClasses.add("p");
        outputClasses.add("e");   
        return outputClasses;
    }
    public ArrayList<HashSet<String>> initializeMasterAttributesAndValues() {
        HashSet<String> zero = new HashSet<String>();
        this.attributes.add("0");
        zero.add("b");
        zero.add("c");
        zero.add("x");
        zero.add("f");
        zero.add("k");
        zero.add("s");
        this.masterAttributes.add(zero);
        
        HashSet<String> one = new HashSet<String>();
        this.attributes.add("1");
        one.add("f");
        one.add("g");
        one.add("y");
        one.add("s");
        this.masterAttributes.add(one);
        
        HashSet<String> two = new HashSet<String>();
        this.attributes.add("2");
        two.add("n");
        two.add("b");
        two.add("c");
        two.add("g");
        two.add("r");
        two.add("p");
        two.add("u");
        two.add("e");
        two.add("w");
        two.add("y");
        this.masterAttributes.add(two);
        
        HashSet<String> three = new HashSet<String>();
        this.attributes.add("3");
        three.add("t");
        three.add("f");
        this.masterAttributes.add(three);
        
        HashSet<String> four = new HashSet<String>();
        this.attributes.add("4");
        four.add("a");
        four.add("l");
        four.add("c");
        four.add("y");
        four.add("f");
        four.add("m");
        four.add("n");
        four.add("p");
        four.add("s");
        this.masterAttributes.add(four);
        
        HashSet<String> five = new HashSet<String>();
        this.attributes.add("5");
        five.add("a");
        five.add("d");
        five.add("f");
        five.add("n");
        this.masterAttributes.add(five);
        
        HashSet<String> six = new HashSet<String>();
        this.attributes.add("6");
        six.add("c");
        six.add("w");
        six.add("d");
        this.masterAttributes.add(six);
        
        HashSet<String> seven = new HashSet<String>();
        this.attributes.add("7");
        seven.add("b");
        seven.add("n");
        this.masterAttributes.add(seven);
        
        HashSet<String> eight = new HashSet<String>();
        this.attributes.add("8");
        eight.add("k");
        eight.add("n");
        eight.add("b");
        eight.add("h");
        eight.add("g");
        eight.add("r");
        eight.add("o");
        eight.add("p");
        eight.add("u");
        eight.add("e");
        eight.add("w");
        eight.add("y");
        this.masterAttributes.add(eight);
        
        HashSet<String> nine = new HashSet<String>();
        this.attributes.add("9");
        nine.add("e");
        nine.add("t");
        this.masterAttributes.add(nine);
        
        HashSet<String> ten = new HashSet<String>();
        this.attributes.add("10");
        ten.add("b");
        ten.add("c");
        ten.add("u");
        ten.add("e");
        ten.add("z");
        ten.add("r");
        ten.add("?");
        this.masterAttributes.add(ten);
        
        HashSet<String> eleven = new HashSet<String>();
        this.attributes.add("11");
        eleven.add("f");
        eleven.add("y");
        eleven.add("k");
        eleven.add("s");
        this.masterAttributes.add(eleven);
        
        HashSet<String> twelve = new HashSet<String>();
        this.attributes.add("12");
        twelve.add("f");
        twelve.add("y");
        twelve.add("k");
        twelve.add("s");
        this.masterAttributes.add(twelve);
        
        HashSet<String> thirteen = new HashSet<String>();
        this.attributes.add("13");
        thirteen.add("n");
        thirteen.add("b");
        thirteen.add("c");
        thirteen.add("g");
        thirteen.add("o");
        thirteen.add("p");
        thirteen.add("e");
        thirteen.add("w");
        thirteen.add("y");
        this.masterAttributes.add(thirteen);
        
        HashSet<String> fourteen = new HashSet<String>();
        this.attributes.add("14");
        fourteen.add("n");
        fourteen.add("b");
        fourteen.add("c");
        fourteen.add("g");
        fourteen.add("o");
        fourteen.add("p");
        fourteen.add("e");
        fourteen.add("w");
        fourteen.add("y");
        this.masterAttributes.add(fourteen);
        
        HashSet<String> fifteen = new HashSet<String>();
        this.attributes.add("15");
        fifteen.add("p");
        fifteen.add("u");
        this.masterAttributes.add(fifteen);
        
        HashSet<String> sixteen = new HashSet<String>();
        this.attributes.add("16");
        sixteen.add("n");
        sixteen.add("o");
        sixteen.add("w");
        sixteen.add("y");
        this.masterAttributes.add(sixteen);
        
        HashSet<String> seventeen = new HashSet<String>();
        this.attributes.add("17");
        seventeen.add("n");
        seventeen.add("o");
        seventeen.add("t");
        this.masterAttributes.add(seventeen);
        
        HashSet<String> eighteen = new HashSet<String>();
        this.attributes.add("18");
        eighteen.add("c");
        eighteen.add("e");
        eighteen.add("f");
        eighteen.add("l");
        eighteen.add("n");
        eighteen.add("p");
        eighteen.add("s");
        eighteen.add("z");
        this.masterAttributes.add(eighteen);
        
        HashSet<String> nineteen = new HashSet<String>();
        this.attributes.add("19");
        nineteen.add("k");
        nineteen.add("n");
        nineteen.add("b");
        nineteen.add("h");
        nineteen.add("r");
        nineteen.add("o");
        nineteen.add("u");
        nineteen.add("w");
        nineteen.add("y");
        this.masterAttributes.add(nineteen);
        
        HashSet<String> twenty = new HashSet<String>();
        this.attributes.add("20");
        twenty.add("a");
        twenty.add("c");
        twenty.add("n");
        twenty.add("s");
        twenty.add("v");
        twenty.add("y");
        this.masterAttributes.add(twenty);
        
        HashSet<String> twentyone = new HashSet<String>();
        this.attributes.add("21");
        twentyone.add("g");
        twentyone.add("l");
        twentyone.add("m");
        twentyone.add("p");
        twentyone.add("u");
        twentyone.add("w");
        twentyone.add("d");
        this.masterAttributes.add(twentyone);

        assert(this.masterAttributes.size() == 22);
        assert(this.attributes.size() == 22);
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
                new File("../data/Mushroom/" + fileName)));
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
            assert (trueClassification.equals("e") || trueClassification.equals("p"));
            for (int i = 1; i <= this.numAttributesPerExample; i++) { //start at 1, not zero for congress  
                temp = attributeValues[i];
                if (masterAttributes.get(i-1).contains(temp)) {
                    tempAttributeTable.put(Integer.toString(i-1), temp);
                } else {
                    System.out.println("attribute number " + (i-1));
                    System.err.println("ERROR: " + temp + " is not a valid attribute for mushroom dataset");
                    System.exit(1); 
                }
            
            }
            //make the new example
            assert (tempAttributeTable.size() == 22);
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