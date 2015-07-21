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
import java.util.Comparator;
import java.util.Collections;

/**
    implementation of the genetic tree builder
*/
public class GeneticDecisionTree extends EvolutionaryDecisionTreeBuilder {

    private HashSet<String> includedAttributes = new HashSet<String>();
    private int numberGenerations = 0;
    private Comparator<Tree<String, String>> comparator;
    private ArrayList<Double> populationProbabilityDistribution;
    private double distributionSum;
    private double bestFitness = 0;
    private double probabilityOfLeaf = 0;
    private double averageNumberOfNodesPerTreeThisGeneration = 0;
    private double expectedBranchingFactor;
    private double expectedNumberOfNodes; // = branchingFactor^expected depth. expected depth = numAttributes
    
    /**
        private tree comparator class for comparing fitness of trees and sorting the population
    */
    class TreeComparator implements Comparator<Tree<String, String>> {
        @Override
        public int compare(Tree<String, String> A, Tree<String, String> B) {
            assert (A != null && B != null);
            if(A.getFitness() < B.getFitness()) {
                return 1;
            } else if (A.getFitness() > B.getFitness()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    /**
---------constructor
    */
    public GeneticDecisionTree(ArrayList<Example> exs, ArrayList<HashSet<String>> ma,
        HashSet<String> attributes, HashSet<String> outputClasses) {
        super(exs, ma, attributes, outputClasses); 
        this.numberGenerations = 0; 
        
        //one-time calculation: expected branching factor b of a random individual
        this.expectedBranchingFactor = 0; //expected number of values per attribute
        for (HashSet<String> vals : this.masterAttributes) {
            expectedBranchingFactor += vals.size();
        }
        this.expectedBranchingFactor = this.expectedBranchingFactor/this.attributes.size(); 
        this.expectedNumberOfNodes = Math.pow(this.expectedBranchingFactor, ((double) this.attributes.size()));
        
    }
    public Tree<String, String> makeDecisionTree() {
        initializePopulation();
        comparator = new TreeComparator();
        Collections.sort(population, comparator);
        
        //System.out.println("sorted");
        this.populationProbabilityDistribution = makeDistribution(this.population);
        
        //System.out.println("distribution made");

        //System.out.println(populationProbabilityDistribution);
        printPopulation(); 
        if (population.get(0).getFitness() > this.bestFitness) {
            this.bestFitness = population.get(0).getFitness();
            this.mostFitIndividual = population.get(0);
        }
        
        while (!this.terminate()) {
            this.population = makeGeneration(this.population);
            Collections.sort(population, comparator);
            
            printPopulation();
            if (population.get(0).getFitness() > this.bestFitness) {
                this.bestFitness = population.get(0).getFitness();
                this.mostFitIndividual = population.get(0).makeCopy(population.get(0).getRoot());
                // System.out.println("\n\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" 
//                     + "\n Best Fitness is: " + this.bestFitness + " with " + mostFitIndividual.getNumNodes()  
//                     + " nodes\n Average Individual has " + this.averageNumberOfNodesPerTreeThisGeneration
//                     + " nodes \n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
            this.populationProbabilityDistribution = makeDistribution(this.population);
            this.numberGenerations++;
        }
        System.out.println("\n\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" 
                    + "\n Best Fitness is: " + this.bestFitness + " with " + mostFitIndividual.getNumNodes()  
                    + " nodes\n Average Individual has " + this.averageNumberOfNodesPerTreeThisGeneration
                    + " nodes \n "
                    + "branching factor: " + expectedBranchingFactor
                    + "\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        return getMostFitIndividual();
    }
    private double calculateProbOfLeaf(double depth) {
        double a = this.attributes.size(); //num attributes
        double b = this.expectedBranchingFactor; //expected number of values per attribute
        if (depth == 1) return 0;
        return 1 - 1/(Math.pow(((a + b)/a), depth));
    }
    /**
        makes a fitness proportionate distribution on all trees whose fitness is greater than 
        1/2. We will "salt" the top ten trees to skew the population towards them a bit more...
        salt function is: [1, 0.9, 0.8, 0.7, ... 0.1]
    */
    private ArrayList<Double> makeDistribution(ArrayList<Tree<String, String>> pop) {
        //TODO: implement
        //assume that pop is already sorted based on fitness, greatest to least
        
        ArrayList<Double> distribution = new ArrayList<Double>();
        double temp = 0;
        double sumTotal = 0.0;
        double checkSum = 0;
        int index = 0;
        this.averageNumberOfNodesPerTreeThisGeneration = 0;
        //double[] salt = {1, 0.5, 0.4, 0.3, 0.2, 0.1, 0.1, 0.1, 0.1, 0.1};//{5, 4, 3, 2, 1, .5, .5, .5, .5, .5}; //{1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
        for (Tree<String, String> tree : pop) {
            temp = tree.getFitness();
            averageNumberOfNodesPerTreeThisGeneration += tree.getNumNodes();
            if (temp <= MINIMUM_FITNESS_ALLOWED_IN_NEXT_GENERATION) {
                break;
            } else {
                // if (index < 10) {
//                     distribution.add(temp + salt[index]);
//                     sumTotal += (temp + salt[index]);
//                     index++;
//                 } else { //just do this if you don't want salt
                    distribution.add(temp);
                    sumTotal += (temp);
                //} 
                
           
            }
        }
        this.averageNumberOfNodesPerTreeThisGeneration /= pop.size();
        //System.out.println("found all trees with fitness > " + MINIMUM_FITNESS_ALLOWED_IN_NEXT_GENERATION);
        if (sumTotal == 0 || distribution.size() < 5) {
            System.err.println("ERROR: this generation has none or too few fit trees, consider lowering"
                + "the fitness threshold...");
            System.exit(1);
        }
        this.distributionSum = sumTotal;

        return distribution;
    
    }

    /**
     *   this method implemented on page 129 of RN textbook
     *
     *
     *
     *
     *
    */
    private ArrayList<Tree<String, String>> makeGeneration(ArrayList<Tree<String, String>> pop) {
        ArrayList<Tree<String, String>> newPop = new ArrayList<Tree<String, String>>();
        Tree<String, String> parentA = null;
        int indexParentA = -1;
        Tree<String, String> parentB = null;
        int indexParentB = -1;
        Tree<String, String> child;
        double fit = 0.0;
        double prob;
        
        
        //elite-specific variables 
        int numElite = (int) Math.round(ELITE_PERCENTAGE * pop.size());
        if (numElite >= this.populationProbabilityDistribution.size()) {
            numElite = this.populationProbabilityDistribution.size()/2;
        }
        
        ArrayList<Tree<String, String>> elite = getElite(pop, numElite);
        int[] numTimesEliteSelected = new int[numElite];
        double cumulativeProbabilityOfElite = 0.0;
        
        //calculate how likely it is to choose an elite 
        // System.out.println(this.populationProbabilityDistribution.size());
        // System.out.println(numElite);
        for (Double d : this.populationProbabilityDistribution.subList(0, numElite)) {
            // System.out.println("fitness of elite: " + d);
            cumulativeProbabilityOfElite += d;
        }
        assert (cumulativeProbabilityOfElite > 0);
        
        //add elites to new pop:
        for (Tree<String, String> e : elite) {
            newPop.add(e);
        }
        
        //for (Tree<String, String> t : pop) {
        for (int i = 0; i < (pop.size() - numElite); i++) { //save elite for replacement, always keep pop same size
            //Tree<String, String> t = pop.get(i);
        
            
            
            
            prob = rand.nextDouble();
            //CROSSOVER WITH REPROCTION
            if (prob < PROB_OF_CROSSOVER) {
                fit = 0.0;
                indexParentA = selectIndividual(pop, 0.0);
                if (indexParentA < numElite) {
                    if (numTimesEliteSelected[indexParentA] > RESELECT_ELITE_AT_MOST) { 
                        indexParentA = selectIndividual(pop, cumulativeProbabilityOfElite);
                        assert (indexParentA > -1);
                        assert (indexParentA >= numElite);
                    } else {
                        numTimesEliteSelected[indexParentA]++;
                    }
                }
                parentA = pop.get(indexParentA);
                 
                //System.out.println("SELECTED tree with " + parentA.getNumNodes() + " nodes and fitness: " + parentA.getFitness());
                //parentB = selectIndividual(pop, 0.0);
                fit = 0.0;
                indexParentB = selectIndividual(pop, 0.0);
                if (indexParentB < numElite) {
                    if (numTimesEliteSelected[indexParentB] > RESELECT_ELITE_AT_MOST) { 
                        indexParentB = selectIndividual(pop, cumulativeProbabilityOfElite);
                        assert (indexParentB > -1);
                        assert (indexParentB >= numElite);
                    } else {
                        numTimesEliteSelected[indexParentB]++;
                    }
                }
                parentB = pop.get(indexParentB);
                //System.out.println("SELECTED tree with " + parentB.getNumNodes() + " nodes and fitness: " + parentB.getFitness());
                assert (parentA != null && parentB != null);
                
                //NOW REPRODUCE
                child = reproduce(parentA, parentB);
            } else {
                //or make random tree instead
                child = makeRandomTree();
            
            }
            
            assert (child != null);       
            prob = rand.nextDouble();
            if (prob < PROB_OF_MUTATION) {
                child = mutateIndividual(child);
                assert (child != null);
            }
            
            fit = this.fitness(child);
            child.setFitness(fit);
            
            newPop.add(child);
            //System.out.println("REPRODUCED new child with fitness: " + child.getFitness());

        
        }
        //System.out.println(newPop.size());
        //System.out.println(pop.size());
        assert (newPop.size() == pop.size());
        
        
        
        // System.out.println("\n\n *********************************" 
//             + "\n We have made generation " + this.numberGenerations + "\n *********************************");
        return newPop;
        
    }
    private ArrayList<Tree<String, String>> getElite(ArrayList<Tree<String, String>> pop, int num) {
        ArrayList<Tree<String, String>> elite = new ArrayList<Tree<String, String>>();
        for (int i = 0; i < num; i++) {
            elite.add(pop.get(i));   
        }
        return elite;
        
    }
    
    // private ArrayList<Tree<String, String>> makeGeneration(ArrayList<Tree<String, String>> pop) {
//         ArrayList<Tree<String, String>> newPop = new ArrayList<Tree<String, String>>();
//         Tree<String, String> parentA;
//         Tree<String, String> parentB;
//         Tree<String, String> child;
//         double fit = 0.0;
//         double prob;
//         
//         for (Tree<String, String> t : pop) {
//             fit = 0.0;
//             parentA = selectIndividual(pop);
//             //System.out.println("SELECTED tree with " + parentA.getNumNodes() + " nodes and fitness: " + parentA.getFitness());
//             parentB = selectIndividual(pop);
//             //System.out.println("SELECTED tree with " + parentB.getNumNodes() + " nodes and fitness: " + parentB.getFitness());
// 
//             child = reproduce(parentA, parentB);
//             assert (child != null);       
//             prob = rand.nextDouble();
//             if (prob < PROB_OF_MUTATION) {
//                 child = mutateIndividual(child);
//                 assert (child != null);
//             }
//             
//             fit = this.fitness(child);
//             child.setFitness(fit);
//             
//             newPop.add(child);
//             //System.out.println("REPRODUCED new child with fitness: " + child.getFitness());
// 
//         
//         }
//         
//         
//         System.out.println("\n\n *********************************" 
//             + "\n We have made generation " + this.numberGenerations + "\n *********************************");
//         return newPop;
//         
//     }
    
    private Tree<String, String> getMostFitIndividual() {
        return this.mostFitIndividual;
    }
    
    protected void initializePopulation() {
        Tree<String, String> t;
        assert (this.population == null);
        this.population = new ArrayList<Tree<String, String>>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            t = makeRandomTree();
            this.population.add(t);
        }
    } 
    private boolean terminate() {
        return this.numberGenerations >= NUM_GENERATIONS;
    }     

    protected Tree<String, String> makeRandomTree() {
        int maxDepth = 4;
        includedAttributes.clear();
        double fit = 0;

        
        Tree<String, String> tree = new Tree<String, String>();
        assert (tree != null);
        Node<String, String> root = recursiveRandomTreeBuilder(null, maxDepth, tree);
        assert (root != null);
        tree = root.getManufacturer();
        assert (tree.getRoot() != null);
        assert (tree.getRoot().equals(root));
        fit = this.fitness(tree);
        tree.setFitness(fit);


        
        // System.out.println("made random tree with: " + tree.getNumNodes() 
//             + " nodes and fitness: " + tree.getFitness());
        
        return tree;
        
        
    }
    private Node<String, String> recursiveRandomTreeBuilder(Node<String, String> parent, int maxDepth, Tree<String, String> tree) {
        double prob = rand.nextDouble();
        double depth;
        if (parent == null) {
            depth = 1.0;
        } else {
            depth = (double) (parent.getDepth() + 1);
        }
        
        if (prob < this.calculateProbOfLeaf(depth) /*PROB_OF_LEAF*/ || (parent != null && parent.getDepth() == (maxDepth - 1))) {
            String classification = getRandomClass();
            Node<String, String> leaf;
            assert (classification != null);
            if (parent == null) {
                leaf = tree.makeNode(classification, 0, true, parent); //making a root that is a leaf
            } else {
                leaf = tree.makeNode(classification, (parent.getDepth() + 1), true, parent);
            }
            return leaf;
        } else { //make an internal node
            int numValuesForAttribute = 0;
            String value;
            int attributeIndex;
            Node<String, String> subtree;
            
            String attributeToSplitOn = getRandomAttribute();
            assert (attributeToSplitOn != null);
            //System.out.println(" \n ** attribute to split on is " + attributeToSplitOn + " ** \n");
            attributeIndex = Integer.parseInt(attributeToSplitOn);
            try {
                numValuesForAttribute = masterAttributes.get(attributeIndex).size();
            } catch (NumberFormatException e) {
                System.err.println("ERROR: bad attribute");
                System.exit(2);
            }
            Node<String, String> internalNode;
            if (parent == null) { //we are making root...
                internalNode = tree.makeNode(attributeToSplitOn, 0, false, parent);

            } else {
                internalNode = tree.makeNode(attributeToSplitOn, (parent.getDepth() + 1), false, parent);
            }
            //make children for this internal node:
            for (String v : masterAttributes.get(attributeIndex)) {
                includedAttributes.add(attributeToSplitOn);

                subtree = recursiveRandomTreeBuilder(internalNode, maxDepth, tree);
                //add an edge with value v from the internal node (parent) to the child (subtree).
                tree.connectNodes(internalNode, subtree, v);
            }
            assert (internalNode.getNumChildren() == numValuesForAttribute);
            
            return internalNode;
        
        }
    }
    
    protected double fitness(Tree<String, String> individual) {
        double fitness = 0;
        assert (individual != null);
        tester.test(individual);
        fitness = tester.getAccuracy();
        int numNodes = tester.countNodes(individual.getRoot());
        individual.setNumNodes(numNodes);
        
        if (REDUCE_FITNESS_IF_TREE_TOO_BIG) {
            if (fitness > MINIMUM_FITNESS_ALLOWED_IN_NEXT_GENERATION 
                && numNodes > 25 /*this was brought about by monk-2: too few nodes, not enough diversity*/
                && (double) individual.getNumNodes() > 2*this.averageNumberOfNodesPerTreeThisGeneration) {
                fitness -= .001 * (individual.getNumNodes() - this.averageNumberOfNodesPerTreeThisGeneration);
            }
        } 
        if (INCLUDE_PRECISION_IN_FITNESS) {
            fitness += (0.05)*tester.getPrecision();
        }
        //System.out.println(tester.printPerformanceMetrics());
        
        //TODO: REDUCE FITNESS BASED ON THE NUMBER OF NODES IN TREE PAST A CERTAIN THRESHOLD
        return fitness;
    }
    
   
    protected int/*Tree<String, String>*/ selectIndividual(ArrayList<Tree<String, String>> pop, double greaterThanThisProb) {
        assert (greaterThanThisProb <= this.distributionSum && greaterThanThisProb >= 0);
        //System.out.println("threshold: " + greaterThanThisProb);
        //System.out.println("Total dist sum" + distributionSum);
        double p = (rand.nextDouble() * this.distributionSum);
        while (p < greaterThanThisProb) { //only runs seldomly
            p = (rand.nextDouble() * this.distributionSum);
            //System.out.println("RECALCULATING");
        }
        //System.out.println("WE HAVE MADE A P: " + p);
        double cumulativeProbability = 0; //usually 0, but could be 
        int index = 0;
        boolean found = false;
        // if (greaterThan > 0) {
//         
//         
//         
//         }
        for (Double d : this.populationProbabilityDistribution) {
            cumulativeProbability += d;
            
            //System.out.println("cum sum: " + cumulativeProbability);
            if (p <= cumulativeProbability) {
                found = true;
                break;
            } else {
                index++;
            }
        }
        assert (found);
        //the following assertion did not account for salting
        //assert (pop.get(index).getFitness() == this.populationProbabilityDistribution.get(index));
        return index;//pop.get(index);
    }
   
    protected Tree<String, String> mutateIndividual(Tree<String, String> individual) {
        boolean isRoot = false;
        
        if (individual.getNumNodes() == 1) { //TODO: FIX, THIS IS NOT GUARANTEED TO BE ACCURATE
            return individual;
        }
        
        int r = rand.nextInt(3);
        
        Node<String, String> whomToMutate = getRandomNode(individual);
        if (whomToMutate.equals(individual.getRoot())) {
            isRoot = true;
        }
        // while (true) {
//             whomToMutate = getRandomNode(individual);
//             if (whomToMutate.equals(individual.getRoot() && attempts > 3) {
//                 return individual; //stop trying to find a random node, we are not going to mutate root
//             } else {
//                 break;
//             }
//             attempts++;
//         }
        switch (r) {
            case 0: { //delete a node, replace with random leaf
                        String classification = getRandomClass();
                        if (isRoot) {
                            individual.setRoot(new Node<String, String>(
                                classification, 0, true, null, individual));
                        } else {
                            Node<String, String> parent = whomToMutate.getParent();
                            Node<String, String> n = new Node<String, String>(
                                classification, 0, true, parent, individual);
                            assert (parent != null);
                            HashMap<String, Node<String, String>> siblingsOfNode1 = parent.getChildren(); //one or both could be null...
                   
                            String edgeValue = parent.getEdgeLeadingToChild(whomToMutate);
                            if (edgeValue == null) { //error here somewhere...don't know how it happened
                                return individual;
                            }
                            siblingsOfNode1.put(edgeValue, n); //just update the node that the edge points to...
                            n.setParent(parent);
                            whomToMutate = null;
                            //System.out.println("mutation: deleted node");
                            
                        }
                        return individual;
                    }
            case 1: { //change a leaf node's class, do this three times because why not
                        Node<String, String> n = this.getRandomLeaf(individual);
                        n.setOutputClass(this.getRandomClass());
                        n = this.getRandomLeaf(individual);
                        n.setOutputClass(this.getRandomClass());
                        n = this.getRandomLeaf(individual);
                        n.setOutputClass(this.getRandomClass());
                        return individual;
                    }
            case 2: { //add
                        Node<String, String> n = this.getRandomLeaf(individual);
                        String attribute = this.getRandomAttribute();
                        assert (attribute != null);
                        n.setLeaf(false);
                        n.setAttribute(attribute);
                        Node<String, String> leaf;
                        assert (n.getNumChildren() == 0);
                        for (String val : this.masterAttributes.get(Integer.parseInt(attribute))) {
                            String classification = this.getRandomClass();
                            assert (val != null);
                            assert (classification != null);
                            leaf = individual.makeNode(classification, (n.getDepth() + 1), true, n);
                            assert (leaf != null);
                            individual.connectNodes(n, leaf, val);
                        }
                        assert (n.getNumChildren() == this.masterAttributes.get(Integer.parseInt(attribute)).size());
                        
                        
                        //System.out.println("mutation: added node");
                        return individual;
                    }
              default: {
                            System.exit(1);
                            return null;
                        }
            
        }
        
        //either delete or change attribute:
        
    }
   
      
    protected Tree<String, String> reproduce(Tree<String, String> parent1, Tree<String, String> parent2) {
       Node<String, String> node1 = null;
       Node<String, String> node2 = null;
       Node<String, String> parentOfNode1 = null;;
       Node<String, String> parentOfNode2 = null;;
       HashMap<String, Node<String, String>> siblingsOfNode1 = null;
       HashMap<String, Node<String, String>> siblingsOfNode2 = null;
       boolean node1Root = false;
       boolean node2Root = false;
       Tree<String, String> child;
       Tree<String, String> subtreeToAdd;
         
       //make a copy of parent1, this will be the child...
       child = parent1.makeCopy(parent1.getRoot());
       
       node1 = getRandomNode(child);
       if (node1.equals(child.getRoot())) {
            node1Root = true;
       }
       //picka random internal node in parent2
       node2 = getRandomNode(parent2);
       if (node2.equals(parent2.getRoot())) {
            node2Root = true;
       } 
       //let parent 1 be the child...
       //swap the subtrees: one or both of these parents could be null if node1 or node2 is root...
       
       if (!node1Root) {
           assert (node1 != null && node1 != child.getRoot());
           assert (node2 != null);
           parentOfNode1 = node1.getParent();
           assert (parentOfNode1 != null);
           siblingsOfNode1 = parentOfNode1.getChildren(); //one or both could be null...

           String edgeValue = parentOfNode1.getEdgeLeadingToChild(node1);
           assert (edgeValue != null);
           siblingsOfNode1.put(edgeValue, node2); //just update the node that the edge points to...
           node2.setParent(parentOfNode1);
       } else {
            assert (node2 != null);
            child.setRoot(node2);
        }
            
           
       
       //System.out.println("WE HAVE MADE A CHILD");
       return child;

    }
    private Node<String, String> getRandomNode(Tree<String, String> tree) {
        Node<String, String> n = tree.getRoot();

        if (n.isLeaf()) {
            return n;
        }
        boolean done = false;
        int which = -1;
        String att = "";
        int numValuesForAttribute = -1;
        String val = "";
        int index = -1;
        int depth = 0;
        while (!done) {

            n = getRandomChild(n);

            depth++;
            assert (n != null);
            
            if (n.isLeaf() || rand.nextDouble() < STOP_TRAVERSING) {
                done = true;
            }
        }
        //System.out.println("SEARCHED TO DEPTH: " + depth + " to find random node in tree");
        //System.out.println("found node: " + n.getData());
        return n;
        
    } 
    private Node<String, String> getRandomLeaf(Tree<String, String> tree) {
        Node<String, String> n = tree.getRoot();

        if (n.isLeaf()) {
            return n;
        }
        boolean done = false;
        int which = -1;
        String att = "";
        int numValuesForAttribute = -1;
        String val = "";
        int index = -1;
        int depth = 0;
        while (!done) {

            n = getRandomChild(n);

            depth++;
            assert (n != null);
            
            if (n.isLeaf()) {
                done = true;
            }
        }
        //System.out.println("SEARCHED TO DEPTH: " + depth + " to find random node in tree");
        //System.out.println("found node: " + n.getData());
        assert (n.isLeaf());
        return n;
        
    }
   
    protected boolean replaceIntoNextGeneration(Tree<String, String> individual) {
        return false;
    }
    public void printPopulation() {
        for (Tree<String, String> t : population) {
            //System.out.println(t.toString());
            // System.out.println("tree with: " + t.getNumNodes() 
//             + " nodes and fitness: " + t.getFitness());
        }
    }
    private String getRandomAttribute() {
        int which = rand.nextInt(attributes.size());
        double includeRepeatedAttribute = 0.2;
        double include = rand.nextDouble();
        int counter = 0;
        for (String s : attributes) {
            if (counter == which) {
                //return s;
                if (includedAttributes.contains(s) && include < includeRepeatedAttribute) {
                    return s;
                } else if (includedAttributes.contains(s)) {
                    return getRandomAttribute();
                } else {
                    return s;
                }
            }
            counter++;
        }
        return null;
    }
    private String getRandomClass() {
        int which = rand.nextInt(outputClasses.size());
        int counter = 0;
        for (String s : outputClasses) {
            if (counter == which) {
                return s;
            }
            counter++;
        }
        return null;
    }
    public Node<String, String> getRandomChild(Node<String, String> n) {
        int randIndex = rand.nextInt(n.getNumChildren());
        int counter = 0;
        for (String s : n.getChildren().keySet()) {
            if (counter < randIndex) {
                counter++;
            } else {
                return n.getChild(s);
            }
        }
        return null;
    
    }


}