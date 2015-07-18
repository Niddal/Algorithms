echo "running 6 tests with value iteration:"
java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=f"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/L-Track-g9-e1E-9-noHard.txt
echo "done with test 1: L track with gamma=0.9, eps=1x10^-9"

java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/L-Track-g9-e1E-9-Hard.txt
echo "done with test 2: L track with gamma=0.9, eps=1x10^-9, hard crashing"

java -ea -jar ./ReinforcementLearning.jar "map data/O-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=f"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/O-Track-g9-e1E-9-noHard.txt
echo "done with test 3: O track with gamma=0.9, eps=1x10^-9"

java -ea -jar ./ReinforcementLearning.jar "map data/O-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/O-Track-g9-e1E-9-Hard.txt
echo "done with test 4: O track with gamma=0.9, eps=1x10^-9, hard crashing"

java -ea -jar ./ReinforcementLearning.jar "map data/R-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=f"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/R-Track-g9-e1E-9-noHard.txt
echo "done with test 5: R track with gamma=0.9, eps=1x10^-9"

java -ea -jar ./ReinforcementLearning.jar "map data/R-track.txt" "set gamma=.9" "set epsilon=0.0000000001" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" <./run >output/Value-Iteration/R-Track-g9-e1E-9-Hard.txt
echo "done with test 6: R track with gamma=0.9, eps=1x10^-9, hard crashing"



echo "running 4 tests with Q learning:"
java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=.8" "set epsilon=0.00001" "set hardCrashing=f" "set alpha=0.66" "set minimumExplorations=5" "make q" "set verboseSimulation=f" "iterate oo" <./run >output/Q-learning/L-Track-g8-a66-e01-noHard.txt
echo "done with test 1: L track with alpha=0.66, gamma=0.8, eps=0.01"

java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=.9" "set epsilon=0.00001" "set hardCrashing=f" "set alpha=0.75" "set minimumExplorations=5" "make q" "set verboseSimulation=f" "iterate oo" <./run >output/Q-learning/L-Track-g9-a75-e01-noHard.txt

echo "done with test 2: L track with alpha=0.75, gamma=0.9, eps=0.01"
java -ea -jar ./ReinforcementLearning.jar "map data/O-track.txt" "set gamma=.9" "set epsilon=0.0001" "set hardCrashing=f" "set alpha=0.75"  "set minimumExplorations=5" "make q" "set verboseSimulation=f" "iterate oo" <./run >output/Q-learning/O-Track-g9-a75-e03-noHard.txt

echo "done with test 3: O track with alpha=0.75, gamma=0.9, eps=0.03"

java -ea -jar ./ReinforcementLearning.jar "map data/R-track.txt" "set gamma=.9" "set epsilon=0.00001" "set hardCrashing=f" "set alpha=0.75"  "set minimumExplorations=5" "make q" "set verboseSimulation=f" "iterate oo" <./run >output/Q-learning/R-Track-g9-a75-e03-noHard.txt
echo "done with test 4: R track with alpha=0.75, gamma=0.9, eps=0.03"
