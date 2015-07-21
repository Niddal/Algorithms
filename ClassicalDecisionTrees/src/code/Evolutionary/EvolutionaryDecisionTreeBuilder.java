/*
    Corbin Rosset, Artifical Intelligence 600.335 JHU Homework 4
    crosset2@jhu.edu
*/
package code.Evolutionary;

import code.Examples.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import code.Trees.*;
import java.util.Random;
import code.Testing.*;

/**
    class that builds genetic decision tree
*/
public abstract class EvolutionaryDecisionTreeBuilder {
    /* class variables */
    protected static final int POPULATION_SIZE = 100; //100
    protected static final double PROB_OF_MUTATION = 0.06;
    protected static final double PROB_OF_CROSSOVER = 0.75; //.75?
    //protected static final double PROB_OF_LEAF = 0.37; //taken care of by function
    protected static final double STOP_TRAVERSING = 0.4;
    protected static final int NUM_GENERATIONS = 50;
    protected static final double ELITE_PERCENTAGE = 0.06; //0.06
    protected static final double MINIMUM_FITNESS_ALLOWED_IN_NEXT_GENERATION = 0.5; //0.5
    protected static final boolean REDUCE_FITNESS_IF_TREE_TOO_BIG = true; //true
    protected static final boolean INCLUDE_PRECISION_IN_FITNESS = false; //true
    protected static final int RESELECT_ELITE_AT_MOST = 1;
    
    /* administrative data structures: manifests, etc */
    protected ArrayList<HashSet<String>> masterAttributes;
    protected HashSet<String> remainingAttributes;
    protected ArrayList<Example> examples;
    protected HashSet<String> attributes;
    protected HashSet<String> outputClasses;
    
    /* algorithm-specific data structures */
    protected ArrayList<Tree<String, String>> population;
    protected Tree<String, String> mostFitIndividual;
    protected Random rand;
    protected DecisionTreeTester tester;
    
    /*
        TERMINATION will be based on diminishing return of fitness: if the fitness of the best
        individual found so far has not markedly improved for k generations, then it's time to 
        terminate; perhaps subject termination to some kind of randomization? Or maybe termination
        is a random variable with mean x = numGenerations and a specified variance
    */
    
    public EvolutionaryDecisionTreeBuilder(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        this.attributes = attributes;
        this.outputClasses = outputClasses;
        this.examples = exs; //training examples...
        this.masterAttributes = ma;
        this.rand = new Random();
        this.tester = new DecisionTreeTester(this.examples, this.masterAttributes,
        this.attributes, this.outputClasses);
    }
    /**
        run the genetic search algorithm and return the best decision tree
    */
    public abstract Tree<String, String> makeDecisionTree();
    /**
        way to initialize the population based on population size. calls makeRandomTree()
        several times.
    */
    protected abstract void initializePopulation();
    
    /**
        Make a random tree based on the piazza post from Dr. Mitchell:
        have a fixed (probability) threshold for calling a given node a 
        leaf (in which case you assign a randomly chosen class label), 
        and otherwise call it an internal node (in which case you randomly 
        select an attribute (repetition is fine), and generate the right 
        number of children for it and recurse)
    */
    protected abstract Tree<String, String> makeRandomTree();
    
    /**
        evaluates the fitness of an individual (tree) in the population based on 
        a linear combination of accuracy, precision, and recall. The training set,
        or a randomly selected subset thereof thereof, will be run on the individual
        to acquire these performance metrics and generate fitness. This will be an
        expensive operation. 
    */
    protected abstract double fitness(Tree<String, String> individual);
    
    /**
        selects an individual from the global population set according to a selection 
        strategy such as fitness-proportion, ranking, or tournament. Tournament seems
        rather expensive (nlogn time for |population| = n), fitness proportion is O(n^2),
        and ranking could be the best...
        
        alternatively evaluate and store fitness as a field when you generate the individual 
        (after recombination, etc) to save time to make fitness proportion O(n). ** BEST **
        
   */
    protected abstract int/*Tree<String, String>*/ selectIndividual(ArrayList<Tree<String, String>> population, double threshold);
   
   /**
        Will mutate an individual (a tree) by either 1) changing the attribute of an internal node 
        provided that the new variable has the same number of values as the original or 
        2) add/delete a node: adding a leaf by turning a prior leaf into an internal node. Deletion:
        delete an entire subtree as long as it isn't too high up the tree. Maybe assign a prob dist
        over the depth of the tree that is skewed toward deeper depths. 
   */
   protected abstract Tree<String, String> mutateIndividual(Tree<String, String> individual);
   
   /**
        crossover: select a node from tree A and swap it with a node from tree B, essentially
        swapping subtrees.
   */
   
   protected abstract Tree<String, String> reproduce(Tree<String, String> parent1, Tree<String, String> parent2);
   
   /**
        replacement: is this individual so fit that in fact it should persist in the new generation?
        ALWAYS KEEP TRACK OF THE MOST FIT INDIVIDUAL SEEN THUS FAR
        
        strategies: 1) elitist: keep best say 10% of previous generation
                    2) non - elitist: ...?????
   */   
   protected abstract boolean replaceIntoNextGeneration(Tree<String, String> individual);
   
   
   public abstract void printPopulation();
   
         
        
    public boolean isValidAttribute(String a) {
        return attributes.contains(a);
    }
    public boolean isValidClass(String a) {
        return outputClasses.contains(a);
    }
}