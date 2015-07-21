/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Examples;

import java.util.HashMap;

public class CongressionalExample extends Example {

    /* super's instance variables 
    private int Id;
    private HashMap<String, String> AttributeList;
    */
    
    public CongressionalExample(int exampleID, HashMap<String, String> attributes, String trueOutputClass) {
        super(exampleID, attributes);
        this.trueClassification = trueOutputClass;
    }
        
}