/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.players;
import code.players.Player;
import code.board.Board;
import code.board.Move;
import code.AdversarialSearch.*;
import code.board.*;
import java.util.ArrayList;


/** alpha-beta pruning Player class for Konane game
 *
 * @author Corby Rosset
 */
public class AlphaBetaPlayer extends Player {
    
    private AdversarialSearch alphaBeta;
    
    /**
        constructor
        @param depth the max search depth
        @param pf the print format (0 for none, 1 for board, 2 for board and statistics
    */
    public AlphaBetaPlayer(int depth, int pf) {
        this.alphaBeta = new AlphaBeta(depth, pf);
    }
    
    /**
        returns the best move based on the alpha beta algorithm
        @param game the board instance
        @return the best move based on the algorithm and utility values
    */
    public Move getMove(Board game) throws InvalidMoveException {
        //Move ret = new Move(-1, -1, -1, -1);
        Chip current;


        //return ret;
        if (game.getWhoseTurn()) {
            current = Chip.BLACK;
        } else {
            current = Chip.WHITE;
        }
        return this.alphaBeta.chooseBestAction(game, current);
    }
    /**
        returns the total number of nodes expanded by the algorithm up to the current move.
        Usually this is called after the game is over so as to return the total number of nodes
        expanded by the whole algorithm for the whole game.
        @return the last sentence
    */
    public int getNumNodesExpanded() {
        return alphaBeta.getNumNodesExpanded();
    }
    /**
        returns an arraylist of time in ms. Each element corresponds to the time taken by
        the algorithm to pick the best move for each turn. The number of elements is the number
        of turns.
        @return the list of times taken per move
    */
    public ArrayList<Long> getTimePerMove() {
        return alphaBeta.getTimePerMove();
    }
    /**
        returns an arraylist of the number of nodes expanded per turn, the number of entries
        is the same as the number of turns taken, and the sum of the entries is equal to the
        total number of nodes expanded
        @return the arraylist of number of nodes expanded per turn
    */
    public ArrayList<Integer> getNodesExpandedPerTurn() {
        return this.alphaBeta.getNodesExpandedPerTurn();
    }


    


}
