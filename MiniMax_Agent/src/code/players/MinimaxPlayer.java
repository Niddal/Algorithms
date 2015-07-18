/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.players;

import code.players.Player;
import code.board.Board;
import code.board.Move;
import code.AdversarialSearch.*;
import code.board.Chip;
import code.board.InvalidMoveException;
import java.util.ArrayList;

/** minimax Player class for Konane game
 *
 * @author Ben Mitchell
 */
public class MinimaxPlayer extends Player {

    private AdversarialSearch minimax;
    
    //public MinimaxPlayer() {}; //never call this, only satisfies serialization requirements
    public MinimaxPlayer(int depth, int pf) {
        this.minimax = new MiniMax(depth, pf);
    }
    
    public Move getMove(Board game) throws InvalidMoveException{
        Chip current;
        if (game.getWhoseTurn()) {
            current = Chip.BLACK;
        } else {
            current = Chip.WHITE;
        }
        return this.minimax.chooseBestAction(game, current);
    }
    public int getNumNodesExpanded() {
        return minimax.getNumNodesExpanded();
    }
    public ArrayList<Long> getTimePerMove() {
        return minimax.getTimePerMove();
    }
    public ArrayList<Integer> getNodesExpandedPerTurn() {
        return this.minimax.getNodesExpandedPerTurn();
    }


}
