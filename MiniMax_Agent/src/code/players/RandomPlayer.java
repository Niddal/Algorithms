/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code.players;

import java.util.Scanner;
import java.util.ArrayList;

import code.players.Player;
import code.board.Board;
import code.board.Move;
import code.board.InvalidMoveException;
import java.util.Random;

/** Human Player class for Konane game
 *
 * @author Corby Rosset
 */
public class RandomPlayer extends Player {

  public Move getMove(Board game) {



    //Scanner stdin = new Scanner(System.in);
    //Move ret = new Move(-1, -1, -1, -1);
    int r1, c1, r2, c2;
    boolean valid = false;
    Random rand = new Random();

    ArrayList<Move> moveList = game.getLegalMoves();
    int randomNum = rand.nextInt(moveList.size());
    return moveList.get(randomNum);

  }
  public int getNumNodesExpanded() {
    return 0;
  }
  public ArrayList<Long> getTimePerMove() {
    return null;
  }
  public ArrayList<Integer> getNodesExpandedPerTurn() {
    return null;
  }
    
}
