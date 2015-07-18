/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.AdversarialSearch;

import code.board.*;
import code.players.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
    The abstract class that all search algorithms (meaning alphabeta and minimax)
    inherit from. 
*/
public abstract class AdversarialSearch {
    protected int maxSearchDepth;
    ArrayList<Integer> values;
    ArrayList<Move> moves;
    protected ArrayList<Long> timePerMove = new ArrayList<Long>();
    protected ArrayList<Integer> numNodesExpandedPerMove = new ArrayList<Integer>();
    private int turnNumber; //how many turns have been played, starts at zero
    protected Chip winner = Chip.NONE;
    protected Chip whoseTurn = Chip.NONE;
    protected int printFormat = -1;
    
    /* super constructor for all searches
        @param searchDepth the max depth allowed
    */
    public AdversarialSearch(int searchDepth, int pf) {
        this.maxSearchDepth = searchDepth;
        this.turnNumber = 0;
        this.printFormat = pf;

    }
    
    
    public abstract int maxValue(Board board, int depth) throws InvalidMoveException;
    public abstract int minValue(Board board, int depth) throws InvalidMoveException;
    public abstract int maxValue(Board board, int depth, int alpha, int beta) throws InvalidMoveException;
    public abstract int minValue(Board board, int depth, int alpha, int beta) throws InvalidMoveException;
    
    /**
        will return the best action based on an alternating recursive call of minValue and maxValue, each
        slightly modified for alpha beta or minimax. 
        @param board the game state
        @param whoseTurn the chip color of the player whose turn it is
        @return the move to make
    */
    public abstract Move chooseBestAction(Board board, Chip whoseTurn) throws InvalidMoveException;
    public abstract int getNumNodesExpanded();
    /**
        retuns the list of num nodes expanded per move, the length of the list is the number of turns
        @return the list of moves
    */
    public ArrayList<Integer> getNodesExpandedPerTurn() {
        return this.numNodesExpandedPerMove;
    }    
    
    protected boolean isTerminalState(Board board) {
        Chip w = board.gameWon();
        if (w == Chip.NONE) {
            return false;
        }
        assert (board.getLegalMoves().size() == 0);
        this.winner = w;
        return true;
    }
    /**
        retuns the list of times taken per move, the length of the list is the number of turns
        @return the list of times
    */
    public ArrayList<Long> getTimePerMove() {
        return this.timePerMove;
    }
    /*
        will return a list of all valid moves for a given board state and player
        whose turn it is
    */
    protected ArrayList<Move> expandMoves(Board board) {
        return board.getLegalMoves();
    }
    /*
        returns the utility of a given board state for a particular player
    */
    protected int getUtility(Board board, Chip whoseTurn) throws InvalidMoveException {
        if (isTerminalState(board)) {
            if (this.winner == whoseTurn) {
                if (whoseTurn == Chip.BLACK) {
                    return 1000000;
                } else {
                    return -1000000;
                }
            } else {
                //System.out.println("Utility:  loss");
                if (whoseTurn == Chip.BLACK) {
                    return -1000000; 
                } else {
                    return 1000000;
                }
            }
        } else {
            //ATTEMPT 1
            /*int numPiecesInCorner = getCorners(board, whoseTurn);
            int numPiecesSandwiched = getSandwich(board, whoseTurn);
            int numPiecesAlone = getAlone(board, whoseTurn);
            int numPiecesSuicidal = getSuicidal(board, whoseTurn);
            
            int utility = 200*this.expandMoves(board).size() + 500*numPiecesInCorner 
                + 300*numPiecesSandwiched - 100*numPiecesAlone - 400*numPiecesSuicidal;            
            */
            
            //ATTEMPT 2:
            /*int numPiecesSandwiched = getSandwich(board, whoseTurn);
            int numPiecesInCorner = getCorners(board, whoseTurn);
            int numPieces = this.getNumPieces(board, whoseTurn);
            int numMoves = this.expandMoves(board).size();
            int numOpponentMoves = this.getOpponentMoves(board, whoseTurn);
            
            int utility = -100*numPieces + 200*numMoves - 800*numOpponentMoves + 600*numPiecesInCorner 
                + 600*numPiecesSandwiched;
            */
            //attempt 3:
            int numPiecesSandwiched = getSandwich(board, whoseTurn);
            int numMoves = this.expandMoves(board).size();
            int numPieces = this.getNumPieces(board, whoseTurn);
            
            int utility = 600*numPiecesSandwiched - 200*numPieces;
            if (whoseTurn == Chip.BLACK) { //black maximizes
                return utility;
            } else {
                return -utility;
            }
        }
        
    }
    //how many moves can the opponent make in response to all the current moves that can be made by player
    private int getOpponentMoves(Board board, Chip whoseTurn) throws InvalidMoveException {
       int n = 0;
       ArrayList<Move> moves = board.getLegalMoves();
       if (moves.size() == 0) {
        return 0;
       }

       for (Move m : moves) {
            board.pushMove(m);
            board.executeMove(m);
            n+=board.getLegalMoves().size();
            board.undoMove(true);
       }
       return n;
       
         
    }
    //how many of our pieces are there? Should have been implemented to subtract off the 
    //number of opponents piecs
    private int getNumPieces(Board board, Chip whoseTurn) {
        int n = 0;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getTile(i, j).getChip() == whoseTurn) {
                    n++;
                }
            }
        }
        return n;
    }
    private int getSuicidal(Board board, Chip whoseTurn) {
        Chip enemy;
        int suicidal = 0;
        if (whoseTurn == Chip.BLACK) {
            enemy = Chip.WHITE;
        } else {
            enemy = Chip.BLACK;
        }

         for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getTile(i, j).getChip() == whoseTurn) {
                    if (!board.outOfBounds(i - 1, j) && !board.outOfBounds(i +1, j)
                        && ((board.getTile(i -1, j).getChip() == enemy && board.getTile(i +1, j).getChip() == Chip.NONE)
                        || (board.getTile(i +1, j).getChip() == enemy && board.getTile(i -1, j).getChip() == Chip.NONE))) {
                           suicidal++; 
                    }
                    if (!board.outOfBounds(i, j-1) && !board.outOfBounds(i, j+1)
                        && ((board.getTile(i, j-1).getChip() == enemy && board.getTile(i, j+1).getChip() == Chip.NONE)
                        || (board.getTile(i, j+1).getChip() == enemy && board.getTile(i, j-1).getChip() == Chip.NONE))) {
                           suicidal++; 
                    }
                }

            }
         }
         return suicidal;

    }
    private int getCorners(Board board, Chip whoseTurn) {
        int size = board.getSize();
        int num = 0;
        
        if (board.getTile(0, 0).getChip() == whoseTurn) {
            if (board.getTile(0, 1).getChip() != Chip.NONE || board.getTile(1, 0).getChip() != Chip.NONE) {
                num++;
            }
        } 
        if (board.getTile(0, size - 1).getChip() == whoseTurn) {
            if (board.getTile(0, size -2).getChip() != Chip.NONE || board.getTile(1, size-1).getChip() != Chip.NONE) {
                num++;
            }
        }
        if (board.getTile(size -1 , 0).getChip() == whoseTurn) {
            if (board.getTile(size-2, 0).getChip() != Chip.NONE || board.getTile(size-1, 1).getChip() != Chip.NONE) {
                num++;
            }
        }
        if (board.getTile(size -1, size - 1).getChip() == whoseTurn) {
            if (board.getTile(size-2, size -1).getChip() != Chip.NONE || board.getTile(size-1, size-2).getChip() != Chip.NONE) {
                num++;
            }
        }
        return num;
    }
    private int getSandwich(Board board, Chip whoseTurn) {
        int size = board.getSize();
        Chip enemy;
        int sandwich = 0;
        if (whoseTurn == Chip.BLACK) {
            enemy = Chip.WHITE;
        } else {
            enemy = Chip.BLACK;
        }
        //iterate through every tile in the board
        
        for (int i = 0; i < size; i++) {
             for (int j = 0; j < size; j++) {
                 if (board.getTile(i, j).getChip() == whoseTurn) {
                    //if our chip is adjacent to either an enemy or a wall on all sides, it is sandwiched
                    if (   (board.outOfBounds(i-1, j) || board.getTile(i-1, j).getChip() == enemy)
                         &&(board.outOfBounds(i+1, j) || board.getTile(i+1, j).getChip() == enemy)
                         &&(board.outOfBounds(i, j-1) || board.getTile(i, j-1).getChip() == enemy)
                         &&(board.outOfBounds(i, j+1) || board.getTile(i, j+1).getChip() == enemy)) {
                         sandwich++;
                    }
                 }
             }    
        }
        return sandwich;

    }
    private int getAlone(Board board, Chip whoseTurn) {
        int alone = 0;
        for (int i = 1; i < board.getSize() - 1; i++) {
            for (int j = 1; j < board.getSize() - 1; j++) {
                if (board.getTile(i, j).getChip() == whoseTurn) {
                    //check if ( up and down and left and right are empty) 
                     if (board.getTile(i - 1, j).getChip() == Chip.NONE 
                        && board.getTile(i + 1, j).getChip() == Chip.NONE
                        && board.getTile(i, j - 1).getChip() == Chip.NONE
                        && board.getTile(i, j + 1).getChip() == Chip.NONE) {
                        alone++;
                     }
                }
            }
        }
        return alone;
    }  

}
