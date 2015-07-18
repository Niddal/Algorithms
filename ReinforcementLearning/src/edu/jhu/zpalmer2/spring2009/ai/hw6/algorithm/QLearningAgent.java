package edu.jhu.zpalmer2.spring2009.ai.hw6.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import  java.util.concurrent.TimeUnit;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Action;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.State;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.Simulator;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorEvent;
import edu.jhu.zpalmer2.spring2009.ai.hw6.simulator.SimulatorListener;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.DefaultValueHashMap;
import edu.jhu.zpalmer2.spring2009.ai.hw6.util.Pair;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.Terrain;
import edu.jhu.zpalmer2.spring2009.ai.hw6.data.WorldMap;

/**
 * A reinforcement agent which uses the Q-learning technique.
 * 
 * @author Zachary Palmer
 */
public class QLearningAgent implements SimulationBasedReinforcementLearningAgent
{
   private static final long serialVersionUID = 1L;
	/** The number of times the agent will explore a given state-action pair before giving up on it, N. */
   private int minimumExplorationCount;
	/** The discount factor used by this agent to allow control over how important short-term gains are considered. */
   private double discountFactor;
	/** The learning factor for this agent. alpha */
   private double learningFactor;
	/** The convergence tolerance (epsilon). */
   private double convergenceTolerance;
    /** Tracks the maximum change in our perception of the environment during an iteration. */
   double maximumChange = 0;
	/** The record of how frequently each action has been explored from each state. */
   private Map<Pair<State, Action>, Integer> visitEvents; //frequency table
	/** The expected reward for the provided state-action pair. */
   private Map<Pair<State, Action>, Double> expectedReward; //Q(s, a) table
	/** The simulator which is simulating the environment in which this agent is learning. */
   private transient Simulator simulator;
   /*only for setting up state space, the algorithm derives no info from this*/
   private WorldMap world;
   /*magnitude of max velocity*/
   private final int maxVelocityMagnitude = 5;
   /* size of world map in each of its 2 dimensions*/
   private Pair<Integer, Integer> size;
   /*state space start positions for initializing iteration*/
   private Set<Pair<Integer, Integer>> startPositions;
   private Set<Pair<Integer, Integer>> finishPositions;
   /*action space*/
   private Set<Action> legalActions = null;
   /*counter for iterations during training*/
   private int numIterations = 0;
   /* size of state-action pair space*/
   private int sizeOfStateSpace;
   /* all possible state-action pairs*/
   private HashSet<State> states;
   /* same, but in a list, easier for random selection */
   private ArrayList<State> statesList;
   private Random rand;
   /* arbitrary value for what the optimistic utility for a state is, should be <0*/
   private double optimisticUtility;
   /* the policy */
   private QPolicy policy;
   /*flag to turn off training functions when we actually start running */
   private boolean doneIterating;

	/**
	 * General constructor.
	 */
   public QLearningAgent()
   {
      this.discountFactor = 0.99;
      this.learningFactor = 0.5;
      this.convergenceTolerance = 0.01;
      this.visitEvents = new DefaultValueHashMap<Pair<State, Action>, Integer>(0);
      this.expectedReward = new DefaultValueHashMap<Pair<State, Action>, Double>(0.0);
      this.simulator = null;
      this.legalActions = null;
      this.numIterations = 0;
      this.sizeOfStateSpace = 0;
      this.states = new HashSet<State>();
      this.statesList = new ArrayList<State>();
      this.rand = new Random();
      this.optimisticUtility = -0.47;
      this.minimumExplorationCount = 3;
      this.policy = new QPolicy();
      this.doneIterating = false;
   }

   @Override
   public Policy getPolicy()
   {
      return new QPolicy();
   }
	
	/**
	 * Iterates a single learn-as-I-go simulation for this Q learning agent. A
	 * single iteration of this algorithm will walk the agent to a goal state;
	 * thus, lower order iterations are likely to take much longer.  Return
	 * value specifies whether a termination criterion has been met.
	 */
   public boolean iterate() {                
      this.maximumChange = 0; //reset
      //begin simulation
      this.simulator.simulate(this.policy);
      if (numIterations % 100000 == 0) {   
         System.out.println("Iteration " + this.numIterations + " completed with delta: " + this.maximumChange);
      } 
      this.numIterations++;
        
      //termination check
      if (this.maximumChange <= this.convergenceTolerance) {
         this.doneIterating = true;
         return true;
      } else if (this.numIterations > 10000000) {
        this.doneIterating = true;
        return true;
      }
      else {
         return false;
      }
   }
	
   @Override
   public Set<? extends SimulatorListener> getSimulatorListeners()
   {
      return Collections.singleton(new QLearningListener());
   }

   @Override
   public QLearningAgent duplicate()
   {
      QLearningAgent ret = new QLearningAgent();
      ret.setConvergenceTolerance(this.convergenceTolerance);
      ret.setDiscountFactor(this.discountFactor);
      ret.setLearningFactor(this.learningFactor);
      ret.setMinimumExplorationCount(this.minimumExplorationCount);
      ret.expectedReward.putAll(this.expectedReward);
      ret.visitEvents.putAll(this.visitEvents);
      return ret;
   }

   public int getMinimumExplorationCount()
   {
      return minimumExplorationCount;
   }

   public void setMinimumExplorationCount(int minimumExplorationCount)
   {
      this.minimumExplorationCount = minimumExplorationCount;
      //System.out.println("minimum exploration count set to : " + minimumExplorationCount);
   }

   public double getDiscountFactor()
   {
      return discountFactor;
   }

   public void setDiscountFactor(double discountFactor)
   {
      this.discountFactor = discountFactor;
   }

   public double getLearningFactor()
   {
      return learningFactor;
   }

   public void setLearningFactor(double learningFactor)
   {
      this.learningFactor = learningFactor;
   }

   public double getConvergenceTolerance()
   {
      return convergenceTolerance;
   }

   public void setConvergenceTolerance(double convergenceTolerance)
   {
      this.convergenceTolerance = convergenceTolerance;
   }

   public Simulator getSimulator()
   {
      return simulator;
   }

   public void setSimulator(Simulator simulator)
   {
      this.simulator = simulator;
      //System.out.println("Simulator set: " + simulator);
      if (this.world != null) {
         assert (this.world == simulator.getWorldMap());
      } 
      else {
         this.world = simulator.getWorldMap();
         this.size = world.getSize();
         this.initializeStateSpace();
         this.initializeLegalActions();
         this.startPositions = world.getStartPositions();
         this.finishPositions = world.getFinishPositions();
      }
      
      
   }
   private void initializeStateSpace() {
      //having access to the state space is only used to randomly initialize the start state for the iterate 
      //method, the agent has no other information about the state space other than his local location
      Pair<Integer, Integer> pos = null;
      Pair<Integer, Integer> vel = null;
      State temp = null;
      int counter = 0;
      int stateCounter = 0;
        
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
                     //add new states to the state space data structures
                     this.states.add(temp);
                     statesList.add(temp);
                     stateCounter++;
                  }   
               }
            }
         }
      }
      assert (statesList.size() == states.size());
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
      //System.out.println("initialized legal actions: " + this.legalActions.size());
   }
   private double maxExpectedUtility(State state) {
        //iterate through all actions a' out of state s, find the one with teh best Q[s', a']
      Pair<State, Action> temp = null;
      double bestUtility = -Double.MAX_VALUE;
      for (Action a : QLearningAgent.this.legalActions) {
         temp = new Pair<State, Action>(state, a);
         if (QLearningAgent.this.expectedReward.get(temp) > bestUtility) {
            bestUtility = QLearningAgent.this.expectedReward.get(temp);
         }   
      }
      return bestUtility;
   
   }
   /*
   private void printExpectedUtilities() {
      for (Pair<State, Action> p : this.expectedReward.keySet()) {
         System.out.println(p.toString() + " " + this.expectedReward.get(p));
      }
   }
   */
   private Set<Action> getBestAction(State state) {
        //same as above but reutrn the action a' associated with the highest Q[s', a']
      Pair<State, Action> temp = null;
      Set<Action> ties = new HashSet<Action>(); //for ties...
      double bestUtility = -Double.MAX_VALUE;
      double utility = -Double.MAX_VALUE;
      Action bestAction = null;
      for (Action a : QLearningAgent.this.legalActions) {
         temp = new Pair<State, Action>(state, a); //temp represents the pair (s', a')
         utility = explorationFunction(temp); //use exploration function
         if (utility >= bestUtility) {
            if (utility == bestUtility) {
               ties.add(a);
               assert (ties.size() <= 9);
               assert (bestUtility == utility);
            } 
            else {
               bestUtility = utility;
               ties.clear();
               ties.add(a);
               assert (ties.size() == 1);
            }
         }
            
      }
        
        //System.out.println("actions returned by decision: " + ties.size());
      return ties;
   }
   private double explorationFunction(Pair<State, Action> pair) {
      if (this.visitEvents.get(pair) > this.minimumExplorationCount /*&& this.visitEvents.get(pair) < 100*/) {
         return this.expectedReward.get(pair);
      } 
      else /*if (this.visitEvents.get(pair) < 100) */{
         return this.optimisticUtility;
      }
   }
   private State getRandomState() {
        //perhaps needed for giving the simulator a different starting position;
      int r = this.rand.nextInt(this.sizeOfStateSpace);
      State s =  this.statesList.get(r);
      assert (s != null);
      return s;
   
   }
   private double getAlpha(int n) {
      double d = (double) n;
      //if (d > this.minimumExplorationCount) {
        return 1/(1 + 0.1*d);
      //} else {
      // return this.learningFactor;
      //}
   }

	/**
	 * The policy used by this agent.
	 */
   class QPolicy implements Policy {
      private static final long serialVersionUID = 1L;
   	
   	/** A randomizer used to break ties. */
      private Random random = new Random();
   	
      public QPolicy()
      {
         super();
      }
   
      @Override
      /**
       * Returns the action the agent chooses to take for the given state.
       */
      public Action decide(State state)
      { 
        /*
            policy is: from a given state s, find the action a' with the highest Q(s, a')
            and return that.
        */
        
         Set<Action> ties = QLearningAgent.this.getBestAction(state); //bestAction;
         assert (ties.size() <= 9);
         int r = random.nextInt(ties.size());
         int counter = 0;
         for (Action a : ties) {
            if (counter == r) {
               return a;
            } 
            else {
               counter++;
            }
         }
         return null;
      }
   }

	/**
	 * The listener which learns on behalf of this agent.
	 */
   class QLearningListener implements SimulatorListener {
   	/**
   	 * Called once for every timestep of a simulation; every 
   	 * time an agent takes an action, an "event" occurs.  
   	 * Q-learning needs to do an update after every step, and this
   	 * function is where it takes place.
   	 */
      @Override
      public void simulationEventOccurred(SimulatorEvent event) {
        
         State newState = event.getStep().getResultState();
         State oldState = event.getStep().getState();
         Action a = event.getStep().getAction();
         double reward = event.getStep().getAfterScore() - event.getStep().getBeforeScore();
         assert (reward == -1 || reward == 0);
            
         /*make a state-action pair for the previous state and the action undertaken*/
         Pair<State, Action> pair = new Pair<State, Action>(oldState, a); //(s, a)
            
         /*update frequency table for the (s, a) pair:*/
         QLearningAgent.this.visitEvents.put(pair, new Integer(QLearningAgent.this.visitEvents.get(pair) + 1));
            
         /*update Q[s, a]*/
         double currExpReward = QLearningAgent.this.expectedReward.get(pair); //Q[s, a]
         double alpha = QLearningAgent.this.getAlpha(QLearningAgent.this.visitEvents.get(pair)); //custom function of frequency
         double newUtility = currExpReward + /*QLearningAgent.this.learningFactor*/ alpha * (reward + QLearningAgent.this.discountFactor * QLearningAgent.this.maxExpectedUtility(newState) - currExpReward);

         /*update maximum change*/
         double diff =  Math.abs(newUtility - currExpReward);   
         if (diff > QLearningAgent.this.maximumChange) {
            QLearningAgent.this.maximumChange = diff;
         }
         QLearningAgent.this.expectedReward.put(pair, newUtility);   
         return;
      }
   }
}
