/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.AdversarialSearch;

import code.board.*;
import code.players.*;
import java.util.ArrayList;
/**
   class that implemements alpha beta pruning
   @author Corby Rosset 
*/
public class AlphaBeta extends AdversarialSearch {

    private int numNodesExpanded;
    private int numNodesExpandedThisMove;

    /**
        constructor
    */
    public AlphaBeta(int depth, int pf) {
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
        @param alpha the value of the highest-valued choice we have found so far along any path for the max player (black)
        @param beta the value of the lowest-valued choice we have found so far along any path for the min player (white)
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached

    */
    public int maxValue(Board board, int depth, int alpha, int beta) throws InvalidMoveException {
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
            board.executeMove(m); //this will change the board
            w = minValue(board, d++, alpha, beta); //increment depth
            board.undoMove(true); //so undo it
            v = Math.max(v, w);
            if (v >= beta && v != -100000000) {
                //System.out.println("pruned in maxValue " +  v);
                return v;
            }
            alpha = Math.max(alpha, v);
        } 
        if (v == -100000000) {
            System.out.println("ERROR, no moves");
            System.exit(1);
        }
        return v;
    }
    /**
        implements the maxValue(State, depth, alpha, beta) function from RN chapter 5
        @param board the state of the game
        @param depth the depth in the state tree we are at
        @param alpha the value of the highest-valued choice we have found so far along any path for the max player (black)
        @param beta the value of the lowest-valued choice we have found so far along any path for the min player (white)
        @return the utility found at either the terminal state, if found, or the expected utility if depth reached

    */
    public int minValue(Board board, int depth, int alpha, int beta) throws InvalidMoveException {
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
        //super.depth++;
        //if not terminal, expand which moves are possible
        moves = super.expandMoves(board);
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
            w = maxValue(board, d++, alpha, beta);
            board.undoMove(true);
            v = Math.min(v, w);
            if (v <= alpha && v != 100000000) {
                //System.out.println("pruned in minValue " +  v);
                return v;
                
            }
            beta = Math.min(beta, v);
        }
        if (v == 100000000) {
            System.out.println("ERROR, no moves");
        }
        return v;
    }
    /**
        not applicable for alphabeta
        @param board the board
        @param depth the depth
        @return the utility
    */
    public int maxValue(Board board, int depth) {
        System.err.println("Not applicable to AlphaBeta");
        System.exit(1);
        return 0;
    }
    /**
        not applicable for alphabeta
        @param board the board
        @param depth the depth
        @return the utility
    */
    public int minValue(Board board, int depth) {
        System.err.println("Not applicable to AlphaBeta");
        System.exit(1);
        return 0;
    }
    @Override
    /**
        will iterate through all the moves for a player in a particular board state and choose the move
        with the best utility for that player (max value for black, min value for white)
        @param board the board
        @param whoseTurn the chip corresponding to whose turn it is
    */
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
                temp = maxValue(board, 1, -100000000, 100000000); //maximize the min
                //initial value of alpha and beta are +/- 10^8 which is smaller in 
                //magnitude than the initial value of optimalValue = 10^9
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
                temp = minValue(board, 1, -100000000, 100000000); //minimize the max
                //initial value of alpha and beta are +/- 10^8 which is smaller in 
                //magnitude than the initial value of optimalValue = 10^9
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
    public int getNumNodesExpanded() {
        return this.numNodesExpanded;
    }
}
