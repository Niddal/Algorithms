/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Examples;


import java.util.HashMap;
/**
    Abstract because we don't want anyone to instantiate a generic example,
    we want examples to be explicitly tied to a dataSet like MONK or Mushroom.
    This is an easy substitute for assigning a "manufacturer" field to every
    example class to associate it with a particular data set.
    
    There is no way to set an attribute's value once an Example has been instantiateed,
    we dont want people changing the characteristics of an example in any way. 
*/
public abstract class Example {
    /*class variables*/
    
    /* instance variables */
    protected int Id;
    protected HashMap<String, String> AttributeList; 
    protected String trueClassification;
    
    public Example(int exampleID) {
        this.Id = exampleID;
    } 
    public Example(int exampleID, HashMap<String, String> attributes) {
        this.Id = exampleID;
        this.AttributeList = attributes;
        
    }
    
    public String getAttributeValue(String attribute) {
       return AttributeList.get(attribute); 
    }
    public String getTrueClassification() {
        return this.trueClassification;
    }
    public HashMap<String, String> getAttributeList() {
        return this.AttributeList; //probably should return a deep copy to avoid malicious attackers...
    }
}