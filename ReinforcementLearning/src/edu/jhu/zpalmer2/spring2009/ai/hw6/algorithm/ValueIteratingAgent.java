package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;
import java.text.DecimalFormat;

import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.DefaultValueHashMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Terrain;


public class ValueIteratingAgent implements ReinforcementLearningAgent
{
   private static final long serialVersionUID = 1L;
	
	/** A mapping between states in that world and their expected utilities. */
   private Map<State, Double> expectedUtilities;
	
	/** The world in which this agent is operating. */
   private WorldMap world;	
	/** The discount factor for this agent. */
   private double discountFactor;
	/** The transition function that this agent uses. */
   private TransitionFunction transitionFunction;
	/** The reward function that this agent uses. */
   private RewardFunction rewardFunction;
	/** The convergence tolerance (epsilon). */
   private double convergenceTolerance;
    
    //====================================================================//
    /* variables associated with the algorithm */
   private final int maxVelocityMagnitude = 5;
   private Pair<Integer, Integer> size;
   private Set<Pair<Integer, Integer>> startPositions;
   private Set<Pair<Integer, Integer>> finishPositions;
   private Map<State, Double> tempExpectedUtilities;
   private Set<Action> legalActions = null;
   private Policy policy; 
   private boolean worldInitialized;
   private int numIterations = 0;
   private String[][] policyGraph;
   private HashMap<State, Action> policyTable;
   private int sizeOfStateSpace;
   private HashSet<State> states;
    //a data structure to hold a mapping from states to actions for every state: this is the policy
    //
	
	/**
	 * Creates a new value iterating agent.
	 * @param world The world in which the agent will learn.
	 */
   public ValueIteratingAgent()
   {
      this.expectedUtilities = new DefaultValueHashMap<State, Double>(0.0);
      this.policy = new ValuePolicy();
      this.world = null;
      this.discountFactor = 0.5;
      boolean hardCrash = false; //????
      //WILL BELOW WORK?? FIX FIX FIX FIX FIX FIX
      this.transitionFunction = new TerrainBasedTransitionFunction(world, hardCrash); 
      this.rewardFunction = null;
      this.convergenceTolerance = 0.000000001;
      this.worldInitialized = false;
      this.tempExpectedUtilities = new DefaultValueHashMap<State, Double>(0.0);
      this.states = new HashSet<State>();
      this.policyGraph = null;
      this.policyTable = new HashMap<State, Action>();
   }

   @Override
   public Policy getPolicy()
   {
      return this.policy;
   }


	/**
	 * Iterate performs a single update of the estimated utilities of each
	 * state.  Return value specifies whether a termination criterion has been
	 * met.
	 */
   @Override
   public boolean iterate() //just makes the utility function
   {
   	// TODO: implement value iteration; this is basically the inside of the
   	// while(!done) loop.
      if (!this.worldInitialized) {
         System.out.println("ERROR: world not initialized");
         System.exit(1);
      }
      this.numIterations++;

      this.expectedUtilities.putAll(this.tempExpectedUtilities);
      this.tempExpectedUtilities.clear();
   
      double delta = 0.0; //the maximum change in teh utility of any state in an iteration
      double newUtilityofState = 0.0;
      double tempUtility = 0.0;
      double weightedUtilityOfBestAction = -Double.MAX_VALUE;
      Action bestAction = null;
      assert (this.states.size() == this.sizeOfStateSpace);
      for(State s : this.states) { //for every state S
         weightedUtilityOfBestAction = -Double.MAX_VALUE; //reset
         assert (s != null);
         for (Action a : this.legalActions) { //find the action that leads to best weighted utility
            tempUtility = calculateWeightedUtility(s, a); //calculates the expected utility of S' based on transition model
            if (tempUtility > weightedUtilityOfBestAction) {
               bestAction = a;  
               weightedUtilityOfBestAction = tempUtility;
            }
         }
         if (discountFactor == 0.0) {
            System.err.println("discount factor zero");
            System.exit(1);
         }
        //U' gets R(s) + gamma * max of all A in actions{sum of (probability of S' given S and A)*(U'(S))}
         newUtilityofState = rewardFunction.reward(s) + this.discountFactor * weightedUtilityOfBestAction;
         this.tempExpectedUtilities.put(s, newUtilityofState); //store new utility in U'
         if (Math.abs(newUtilityofState - this.expectedUtilities.get(s)) > delta) { //if |U'(s) - U(s)| > delta
            delta = Math.abs(newUtilityofState - this.expectedUtilities.get(s));
         }
      }
       
      if (this.numIterations % 20 == 0) {
         System.out.println("after " + this.numIterations + " iterations, delta is " + delta
            + " convergence tolerance is: " + this.convergenceTolerance);
      }
      if (delta < convergenceTolerance) {
         return true;
      } 
      else {
         return false;
      }
   }

   public ValueIteratingAgent duplicate()
   {
      ValueIteratingAgent ret = new ValueIteratingAgent();
      ret.setConvergenceTolerance(this.convergenceTolerance);
      ret.setDiscountFactor(this.discountFactor);
      ret.setRewardFunction(this.rewardFunction);
      ret.setTransitionFunction(this.transitionFunction);
      ret.setWorld(this.world);
      ret.expectedUtilities.putAll(this.expectedUtilities);
      return ret;
   }
	
   public double getLearningFactor()
   {
      return discountFactor;
   }

   public void setDiscountFactor(double discountFactor)
   {
      this.discountFactor = discountFactor;
      //System.out.println("discout factor set: " + this.discountFactor);
   }

   public TransitionFunction getTransitionFunction()
   {
      return transitionFunction;
   }

   public void setTransitionFunction(TransitionFunction transitionFunction)
   {
      this.transitionFunction = transitionFunction;
      //System.out.println("transition function set: " + this.transitionFunction);
   }

   public RewardFunction getRewardFunction()
   {
      return rewardFunction;
   }

   public void setRewardFunction(RewardFunction rewardFunction)
   {
      this.rewardFunction = rewardFunction;
      //System.out.println("reward function set: " + this.rewardFunction);
   }
	
   public WorldMap getWorld()
   {
      return world;
   }

   public void setWorld(WorldMap world)
   {
      this.world = world;
      this.worldInitialized = true;
      this.size = world.getSize();
      this.initializeStateSpace();
      this.initializeLegalActions();
      this.startPositions = world.getStartPositions();
      this.finishPositions = world.getFinishPositions();
      this.policyGraph = new String[size.getFirst()][size.getSecond()];
   }
	
   public double getConvergenceTolerance()
   {
      return convergenceTolerance;
   }

   public void setConvergenceTolerance(double convergenceTolerance)
   {
      this.convergenceTolerance = convergenceTolerance;
   }

	/**
	 * Represents a policy that this agent would produce.
	 */
   public class ValuePolicy implements Policy
   {
      private static final long serialVersionUID = 1L;   
   	/**
   	 * The action an agent decides to take from a given state 
   	 */
      public Action decide(State state)
      {   
         //optimal policy for a state to choose the action that maximizes the expected utility of the next state
         if (ValueIteratingAgent.this.policyTable.get(state) != null) {
            return ValueIteratingAgent.this.policyTable.get(state);
         }
                  
         Action bestAction = null;
         double weightedUtilityOfBestAction = -Double.MAX_VALUE;
         double tempUtility = 0.0;
         
         for (Action a : ValueIteratingAgent.this.legalActions) { //find the action that leads to best weighted utility
            tempUtility = calculateWeightedUtility(state, a); //calculates the expected utility of S' based on transition model
            if (tempUtility > weightedUtilityOfBestAction) {
               bestAction = a;
               weightedUtilityOfBestAction = tempUtility;   
            }
         }
         assert (bestAction != null);
         ValueIteratingAgent.this.policyTable.put(state, bestAction); 
         return bestAction;
      }
   }
   private void initializeStateSpace() {
      Pair<Integer, Integer> pos = null;
      Pair<Integer, Integer> vel = null;
      State temp = null;
      int counter = 0;
      int stateCounter = 0;
      
      if (!this.worldInitialized) {
         System.out.println("ERROR: world not initialized");
         System.exit(1);
      }  
      for (int i = 0; i < size.getFirst(); i++) {
         for (int j = 0; j < size.getSecond(); j++) {
            pos = new Pair<Integer, Integer>(new Integer(i), new Integer(j));
            if (world.getTerrain(pos) != Terrain.WALL) { //then this is a valid position
                //enumerate all possible velocities
               counter++;
               for (int vx = -1*maxVelocityMagnitude; vx <= maxVelocityMagnitude; vx++) {
                  for (int vy = -1*maxVelocityMagnitude; vy <= maxVelocityMagnitude; vy++) {
                     vel = new Pair<Integer, Integer>(new Integer(vx), new Integer(vy));
                     temp = new State(pos, vel);
                     this.states.add(temp);
                     stateCounter++;
                     //this.tempExpectedUtilities.put(temp, 0.0);
                  }
                    
               }
            }
         }
      }
      //System.out.println("Size of map: " + size.getFirst() * size.getSecond() + " size of state space: " + stateCounter);
      this.sizeOfStateSpace = stateCounter;  
   }
   private void initializeLegalActions() {
      Set<Action> actions = new HashSet<Action>();
      for (int ddx=-1;ddx<=1;ddx++)
      {
         for (int ddy=-1;ddy<=1;ddy++)
         {
            actions.add(new Action(new Pair<Integer,Integer>(ddx,ddy)));
         }
      }
      this.legalActions = Collections.unmodifiableSet(actions);
   }
   private double calculateWeightedUtility(State S, Action A) {
      assert (S != null);
      assert (A != null);
      Set<Pair<State, Double>> resultsOfTransition = this.transitionFunction.transition(S, A);
      double sum = 0.0;
        
      for (Pair<State, Double> p : resultsOfTransition) {
         sum += p.getSecond() * this.expectedUtilities.get(p.getFirst());
      }
      return sum;
   }
   //debugging again
   /*private void printStateSpaceUtilities() {
      DecimalFormat df = new DecimalFormat("#.####");
        
      for (State key : this.expectedUtilities.keySet()) {
         if (world.getTerrain(key.getPosition()) != Terrain.WALL && this.expectedUtilities.get(key) != 0.0 && this.expectedUtilities.get(key) != -1.0) {
            System.out.println(key.toString() + " " + df.format(this.expectedUtilities.get(key)));
         }
      }
   }*/
   
   //also debugging
   /*private State getBestStateForPosition(Pair<Integer, Integer> pos) {
        double bestUtility = -Double.MAX_VALUE;
        State bestState = null;
        State temp = null;
        Pair<Integer, Integer> vel;
        double tempUtility;
        for (int vx = -1*maxVelocityMagnitude; vx < maxVelocityMagnitude; vx++) {
          for (int vy = -1*maxVelocityMagnitude; vy < maxVelocityMagnitude; vy++) {
             vel = new Pair<Integer, Integer>(new Integer(vx), new Integer(vy));
             temp = new State(pos, vel);
             tempUtility = this.expectedUtilities.get(temp);
             if (tempUtility > bestUtility) {
                bestState = temp;
                bestUtility = tempUtility;
             }
          }
            
        }
        assert (bestState != null);
        return bestState;
   }*/
   /* not really necessary, for debugging purposes */
   /*private void createPolicyForEveryState() {
      Action bestAction = null;
      double weightedUtilityOfBestAction = -Double.MAX_VALUE;
      double tempUtility = 0.0;
      for(State s : this.expectedUtilities.keySet() ) { //for every state S
         weightedUtilityOfBestAction = -Double.MAX_VALUE; //reset
         assert (s != null);
         for (Action a : this.legalActions) { //find the action that leads to best weighted utility
            tempUtility = calculateWeightedUtility(s, a); //calculates the expected utility of S' based on transition model
            if (tempUtility > weightedUtilityOfBestAction) {
               bestAction = a;  
               weightedUtilityOfBestAction = tempUtility; 
            }
         }
         this.policyTable.put(s, bestAction);
      }
   }*/
   /* not really necessary, for debugging purposes */
   /*private void createPolicyGraph() {
        State state;
        String s = "";
        String r = "";
        Action a = null;
        for (int y=0;y<this.size.getSecond();y++) {
    	    for (int x=0;x<this.size.getFirst();x++) {
    			String c;
    			Pair<Integer,Integer> p = new Pair<Integer,Integer>(x,y);
    			if (this.startPositions.contains(p))
    			{
    				c = ""; //"|  S  ";
    			} else if (this.finishPositions.contains(p))
    			{
    				c = ""; //"|  F  ";
    			} else
    			{
    				Terrain t = world.getTerrain(p);
                    if (t == Terrain.WALL) {
                        c = ""; //"|  #  ";
                    } else {
                        state = this.getBestStateForPosition(p);
                        r += state.toString() + " action: " + this.policyTable.get(state) + "\n";
                        c = "|" + String.format("%-6.6s", (this.expectedUtilities.get(state).toString()).substring(1, this.expectedUtilities.get(state).toString().length()));
                    }
    				
    			}
    			s += c;
    		}
    		s += "\n";
    	}
        System.out.println(r);
        System.out.println(s);
   
   
   }*/

}
