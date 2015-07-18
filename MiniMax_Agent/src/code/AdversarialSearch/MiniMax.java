/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.AdversarialSearch;

import code.board.*;
import code.players.*;
import java.util.ArrayList;
public class MiniMax extends AdversarialSearch {

/* super variables
    private Graph<Board> graph;
    private int maxSearchDepth;
    private ArrayList<Move> movesToConsider;
    private int turnNumber; //how many turns have been played, starts at zero
*/
    private int numNodesExpanded;
    private int numNodesExpandedThisMove;
    
    /**
        constructor
    */
    public MiniMax(int depth, int pf) {
        super(depth, pf);
        this.numNodesExpanded = 0;
        this.numNodesExpandedThisMove= 0;
    }
    /**
        resets the total number of nodes expanded to zero
    */
    private void resetNodesExpandedPerTurn() {
        this.numNodesExpandedThisMove = 0;
    }

    /**
        implements the maxValue(State, depth, alpha, beta) function from RN chapter 5
        @param board the state of the game
        @param depth the depth in the state tree we are at
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached

    */
    public int maxValue(Board board, int depth) throws InvalidMoveException {
        int v = -100000000;
        int w;
        int temp;
        int d = depth;
        Move m;
        ArrayList<Move> moves = null;

        if (super.isTerminalState(board) || depth > maxSearchDepth) { //??????? black??
            //board.popMove();
            int val = super.getUtility(board, Chip.BLACK); 
            //board.undoMove(true);
            return val;
        }
        //if not terminal, expand which moves are possible
        moves = super.expandMoves(board);
        
        //increment number nodes expanded
        this.numNodesExpanded++;
        this.numNodesExpandedThisMove++;
        
        
        if (moves.size() == 0) {
            System.err.println("Error: no moves left");
            System.exit(1);
        }
        for (int i = 0; i < moves.size(); i++) {
            m = moves.get(i);
            assert (m != null);
            board.pushMove(m);
            board.executeMove(m); //this will change the board, and subsequently the
            w = minValue(board, d++);
            board.undoMove(true);
            v = Math.max(v, w);
        } 
        if (v == -100000000) {
            System.out.println("ERROR, no moves");
        }
        return v;
    }
    /**
        implements the minValue(State, depth, alpha, beta) function from RN chapter 5
        @param board the state of the game
        @param depth the depth in the state tree we are at
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached

    */
    public int minValue(Board board, int depth) throws InvalidMoveException {
        int v = 100000000;
        int w;
        int temp;
        int d = depth;
        Move m;
        ArrayList<Move> moves = null;
        if (super.isTerminalState(board) || depth > maxSearchDepth) { //??????? black??
            int val = super.getUtility(board, Chip.WHITE); 
            return val;

        }
        //if not terminal, expand which moves are possible
        moves = super.expandMoves(board);
        
        //increment number of nodes expanded
        this.numNodesExpanded++;
        this.numNodesExpandedThisMove++;
        
        if (moves.size() == 0) {
            System.err.println("Error: no moves left");
            System.exit(1);
        }
        for (int i = 0; i < moves.size(); i++) {
            m = moves.get(i);
            assert (m != null);
            board.pushMove(m);
            board.executeMove(m);
            w = maxValue(board, d++);
            board.undoMove(true);
            v = Math.min(v, w);
        }
        if (v == 100000000) {
            System.out.println("ERROR, no moves");
        }
        return v;
    }
    /**
        not implemented for minimax, could be in an interface i suppose
        @param board the state of the game
        @param depth the depth in the state tree we are at
        @param alpha the value of the highest-valued choice we have found so far along any path for the max player (black)
        @param beta the value of the lowest-valued choice we have found so far along any path for the min player (white)
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached
    */
    public int maxValue(Board board, int depth, int alpha, int beta) {
        System.err.println("Not applicable to Minimax");
        System.exit(1);
        return 0;
    }
    /**
        not implemented for minimax, could be in an interface i suppose
        @param board the state of the game
        @param depth the depth in the state tree we are at
        @param alpha the value of the highest-valued choice we have found so far along any path for the max player (black)
        @param beta the value of the lowest-valued choice we have found so far along any path for the min player (white)
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached
    */
    public int minValue(Board board, int depth, int alpha, int beta) {
        System.err.println("Not applicable to Minimax");
        System.exit(1);
        return 0;
    }
    
    @Override
    public Move chooseBestAction(Board board, Chip whoseTurn) throws InvalidMoveException {
        //if black, then max, if white then min
        this.moves = expandMoves(board);
        Move moveToMake = null; 
        this.whoseTurn = whoseTurn;
        int optimalValue = 0;
        int temp;
        Move m;
        Move bestMove = null;
        
        /*timing information is collected here (beginning of timing)*/
        long startTime = System.nanoTime();
        
        /*num nodes expanded information - start - reset to zero*/
        this.resetNodesExpandedPerTurn();

        //find utility values for each move by recursing down the tree, then back up...
        if (whoseTurn == Chip.BLACK) {
            optimalValue = -1000000000;
            assert (board.getWhoseTurn());
            for (int i= 0; i < moves.size(); i++) {
                m = moves.get(i);
                board.executeMove(m);
                board.pushMove(m);
                temp = maxValue(board, 1); //maximize the min
                if (temp > optimalValue) {
                    optimalValue = temp;
                    bestMove = m;
                }
                board.undoMove(true);
                if (super.printFormat > 1) {
                    System.out.println("Utility of move (for black): " + temp);
                }
            }
            if (super.printFormat > 1) {
                System.out.println(" - Best Utility for black (max): " + optimalValue); 
            }                 
        } else {
            optimalValue = 1000000000;
            assert (!board.getWhoseTurn() && whoseTurn == Chip.WHITE);
            for (int i= 0; i < moves.size(); i++) {
                m = moves.get(i);
                board.executeMove(m);
                board.pushMove(m);
                temp = minValue(board, 1); //minimize the max
                if (temp < optimalValue) {
                    optimalValue = temp;
                    bestMove = m;
                }
                board.undoMove(true);
                if (super.printFormat > 1) {
                    System.out.println("Utility of move (for white): " + temp);
                }
            }
            if (super.printFormat > 1) {
                System.out.println(" - Best Utility for white (min): " + optimalValue);
            }

        }
        /*timing information is collected here (end of timing)*/
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;
        timePerMove.add(duration);
        /*End timing*/
        
        /*node expanded information - begin */
        numNodesExpandedPerMove.add(numNodesExpandedThisMove);

        if (bestMove == null) {
            throw new InvalidMoveException("ERROR: trying to return null move");
        }
        return bestMove;
    }
    @Override
    /**
        return the number of nodes expanded
        @return the number
    */
    public int getNumNodesExpanded() {
        return this.numNodesExpanded;
    }

}
