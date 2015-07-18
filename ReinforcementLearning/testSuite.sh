#!/bin/bash

VCONVERGE=0.000000000001
QCONVERGE=0.00001

function testVI {

    echo "Testing VI L gamma = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=$1" "set epsilon=$VCONVERGE" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/VI-lower-threshold/L/L-Track-g$1-e1E-11UpperEnd.txt
    echo "done"

    echo "Testing VI O gamma = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/O-track.txt" "set gamma=$1" "set epsilon=$VCONVERGE" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/VI-lower-threshold/O/O-Track-g$1-e1E-11UpperEnd.txt
    echo "done"

    echo "Testing VI R gamma = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/R-track.txt" "set gamma=$1" "set epsilon=$VCONVERGE" "set hardCrashing=t"  "make vi" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/VI-lower-threshold/R/R-Track-g$1-e1E-11UpperEnd.txt
    echo "done"

    echo "done with gamma = $1"
}

function testQ {
    echo "Testing Q L gamma = $2 alpha = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/L-track.txt" "set gamma=$2" "set epsilon=$QCONVERGE" "set hardCrashing=t" "set alpha=$1" "make q" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/Q/L/L-Track-g$2-a$1-e1E-5UpperEnd.txt
    echo "done"

    echo "Testing Q O gamma = $2 alpha = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/O-track.txt" "set gamma=$2" "set epsilon=$QCONVERGE" "set hardCrashing=t" "set alpha=$1" "make q" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/Q/O/O-Track-g$2-a$1-e1E-5UpperEnd.txt
    echo "done"

    echo "Testing Q R gamma = $2 alpha = $1"
    java -ea -jar ./ReinforcementLearning.jar "map data/R-track.txt" "set gamma=$2" "set epsilon=$QCONVERGE" "set hardCrashing=t" "set alpha=$1" "make q" "set verboseSimulation=f" "iterate oo" "sim 50" "quit" >output-paper/Q/R/R-Track-g$2-a$1-e1E-5UpperEnd.txt
    echo "done"

    echo "done testing Q gamma = $2 alpha = $1"

}

echo "changing ALPHA and GAMMA for VI"
for gamma in 0.82 0.84 0.87 0.89 0.91 0.93 0.96 0.97 0.98 0.99 0.999
do
    testVI $gamma
done

#echo "changing ALPHA and GAMMA for Q-learning:"
#for alpha in 0.4 0.45 0.5 0.55 0.6 0.65 0.7 0.75 0.8 0.85 0.9 0.95 1.0
#do
#    for gamma in 0.6 0.7 0.8 0.9
#    do
#        testQ $alpha $gamma
#    done
#done
