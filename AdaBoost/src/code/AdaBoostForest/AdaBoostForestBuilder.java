/*
    Corbin Rosset. JHU Artificial Intelligence 600.335 Assignment 4 Decision Trees
    crosset2@jhu.edu
*/
package code.AdaBoostForest;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import code.Trees.*;

/**
    the abstract class/interface for a random forest
*/  
public interface AdaBoostForestBuilder {

    /**
        making the tree is naturally done recursively, but it is more memory efficient to do it iteratively...
        @param currentExamples the examples that still havent been used for training the tree yet
        @param remainingAttributes the hashSet of remaining attributes
        @return a subtree of trained nodes
    */
    public Forest<String,String> trainForest() throws Exception;
} 