
Copyright 2015
Corby Rosset All rights reserved

--------------------------------------------------------------------------------
1. Included Files
--------------------------------------------------------------------------------

- src/ contains all the source files, the algorithm, and scaffolding

- output/ contains 3 directories: 
	 - Value-Iteration contains runs for quickTests (hard crashing and soft)
	 - Q-learning contains only quickTests for soft crashing
	 - longRuns contain hard crashing tests for Q learning

- quickTests.sh: runs a test suite of both Q and VI. See section 5 below
- run.sh: do not touch this

--------------------------------------------------------------------------------
2. Usage
--------------------------------------------------------------------------------

In order to use this software, you must have a Java runtime environment of at
least version 1.7 installed, it was tested on a system that had:

java version "1.7.0_51" Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)

Compile the code with the following commands ./build-jar.sh And then running it
with: java -jar ./ReinforcementLearning.jar

This will start the RLASH prompt (Reinforcement Learning Agent SHell) which will
accept a series of commands.  To quit the application, end your input (Ctrl-D
under Linux or Mac, Ctrl-Z + Enter under Windows) or issue the command "quit".


--------------------------------------------------------------------------------
3. RLASH Commands
--------------------------------------------------------------------------------

An inline help details most of the commands of the application.  When help is
issued by itself, a summary of each command is displayed.  When the name of a
command follows the help command, detailed help is displayed on that command.

Maps are loaded using the "map" command, such as "map data/L-track.txt".  A map
must be loaded before you can create an agent.  If you load a map for which an
agent was not trained, results are undefined.

Agents are created using variables from a configured environment (the contents
of which can be displayed using the command "env").  For example, the
environment variable epsilon controls the convergence tolerance for the
algorithms (as shown in the pseudocode in the book).  To change an environment
variable, use the "set" command.  For example:

	set epsilon=0.99

Information about the purpose of environment variables can be obtained with the
"vhelp" command.

After configuring the variables to your liking, create the agent with the "make"
command.  The make command requires that you specify the algorithm ("vi" or "q")
you are using.  Note that changes to the environment variables will not affect
an agent after it has been created (although they may affect other behaviors of
the application).

In order to train the agent, use the "iterate" command.  If called alone, the
iterate command iterates through a single pass of the algorithm's learning
routine.  For value iteration, this is one iteration of value learning.  For
Q-learning, this is one pass through the race track.  If "iterate" is called
with a parameter, that parameter must be either a positive integer (which
specifies a number of iterations) or "oo" (which specifies to run until
convergence).

Once the agent is trained, it can be tested using the "sim" command.  Again, a
number of simulations can be specified.  Each simulation is executed using a
COPY of the agent; thus, no side effects will occur from running a simulation.

Q-learning iterations and simulations can be observed by setting the environment
variable "verboseSimulation" to true (via "set verboseSimulation=t").  When this
value is set, an ASCII representation of the race track is written to the
console at each simulation time step.  This value can be modified before or
after agent creation.  In order to obtain an animation-like effect, the
environment variable "verboseSimulationDelay" can be set to a number of
milliseconds between frames.

The metrics of the agent can be obtained using the "metrics" command.  This
indicates whether or not the agent has converged, how many learning iterations
it has performed, and the amount of wall time it has spent learning.  NOTE: If
you train a Q-learning agent with verboseSimulation=t, the wall time you get
from metrics will be meaningless!

Agents can be loaded from and saved to disk using the "load" and "save"
commands.


--------------------------------------------------------------------------------
4. Command-line Interface
--------------------------------------------------------------------------------

If arguments are provided to the application on the command line, they are
interpreted as commands to be issued to the system before a prompt is provided.
For example, running the program as

	java -jar ReinforcementLearning.jar "set epsilon=0.001" "set alpha=0.5" "set
	gamma=0.7"

will initialize environment variables in the above manner by the time an RLASH
prompt is presented.  On the other hand, this command:

	java -jar ReinforcementLearning.jar "set epsilon=0.5" "set gamma=0.5" "map
	data/L-track.txt" "make vi" "i oo" "sim 100" "quit"

could be used to perform a headless test of an agent, although these parameters
are not very interesting.

java -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=0.7"
"set epsilon=0.000000001" "set hardCrashing=f” "make vi" "set
verboseSimulation=t" "iterate oo" 

--------------------------------------------------------------------------------
5. Provided Test Scripts
--------------------------------------------------------------------------------

THERE IS NO NEED TO RUN YOUR OWN TESTS!

There is a test script, quickTests.sh, that will run a series of value iteration
and Q learning tests, and print the transcripts to the output/ directory. The
“run” file merely provides some input to the RLASH interface, don’t touch it. 

WARNING: running “./quickTests.sh” will overwrite files in the output/ directory

The test suite should complete in about 5 minutes or less. 

The parameters are the same for every suite of Q or VI tests, but briefly:
 - gamma is usually 0.9 for all tests
 - epsilon is 1E-9 for VI, or about 1E-5 for Q
 - alpha has no effect for Q learning because I implemented a custom function
 - VI has both hard and soft crashing in the suite, Q has only soft crashing
   note that Q has hard crashing in the longRun/ directory
 - minimumExplorations was 5 for Q learning tests, this was arbitrary

--------------------------------------------------------------------------------
6. Implementation Details and Customizations
--------------------------------------------------------------------------------

* convergence: for both VI and Q-learning, convergence occurs in an iteration
  whenever the maximum update for a utility in that iteration is less than a
  threshold, epsilon. 

* Good values of epsilon are 1E-9 for value iteration, and about 1E-4 or 1E-5
  for Q learning. Any lower than that for Q learning will take too long. You
  might want to use 1E-3 or so for hard crashing. 

* NOTE: no test will ever need more than 10 million iterations, so I capped the
* iteration run 
  at that size. Granted, you could remove it and allow say a Q learning agent to
  converge to an epsilon of 1E-8, but there is diminishing return to lowering
  the threshold for convergence for Q learning. Already, R and O tracks converge
  at around 1 million iterations and they already outperform their value
  iteration counterparts in hard crashing. So I saw no need to let anything run
  beyond 10 million iterations. 

* In the longRuns/ directory under output/, there are hardCrashing=t cases for
* all the tracks.
  It didn't take more than a few minutes to run these tests, but they are not
  included in the quicktests.sh script. 

* custom alpha function: given a frequency N[s, a] representing the number of
  visitations to state-action pair (s, a), alpha(N[s, a]) is: 
      
	  if (N[s, a] > this.minimumExplorationCount) { 
	  	return 1/(1 + 0.1*N[s, a]);
	  } else { 
	  	return this.learningFactor; 
	  } 
	  
  which is basically 1/x function, stretched a bit along the x axis. 
  My guess is that the learning factor is
  irrelevant as long as minimumExplorationCount is reasonably small, because
  it will get washed out. Granted, you must allow enough iteration (meaning
  epsilon is low enough) for the 1/x to really take effect. But the nice
  feature about this is that you can get convergence to epsilons as low as 10E-6. 

* From runs 1 and 2 of the Q learning tests in the suite, changing alpha and
  gamma do not have a lasting impact on performance of the algorithm. Changes in both
  variables led to less than .02 steps difference in performance of Q learning
  on 50 simulations of L-track, which reported about 10.7 steps. 

--------------------------------------------------------------------------------
7. Reflections
--------------------------------------------------------------------------------

A wonderful assignment. I enjoyed how concise the code was for this assignment.
Once you understood the scaffolding, it really wasn’t that bad. 
