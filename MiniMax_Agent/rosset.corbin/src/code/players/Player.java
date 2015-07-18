/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.players;

import code.board.Board;
import code.board.Move;
import code.board.InvalidMoveException;
import java.util.ArrayList;
/** abstract Player class for Konane game
 *
 * @author Corby Rosset
 */
public abstract class Player {

  public Player() {};
  /**
   * Function to ask the player to provide a move; will be called by driver
   *
   * @param game the current state of play (for getting/checking legal moves)
   */
  public abstract Move getMove(Board game) throws InvalidMoveException;
  
  //below are methods for metadata collection (like the NSA)
  public abstract int getNumNodesExpanded();
  public abstract ArrayList<Long> getTimePerMove();
  public abstract ArrayList<Integer> getNodesExpandedPerTurn();
}
