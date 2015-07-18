/*
    Corby Rosset, AI assignment 2 Adversarial Search
    crosset2@jhu.edu
*/
package code;
import java.util.Scanner;
import code.board.Board;
import code.board.Move;
import code.board.Chip;
import code.players.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Driver class for Konane program; see README for description and terms of use
 * @author Corbin Rosset
 */
public class Konane {

  public static void main(String[] args) {
    Board game;
    int boardSize;
    Player[] players = new Player[2];
    Scanner stdin = new Scanner(System.in);
    int d; //search depth;
    //AdversarialSearch search = new alphaBeta(a;lsdkfj;lk
    int turnsBeforeUndoAll;
    int nodesExpandedThisTurn = 0;
    int outputFormat = -1;
    
    int numIterations = 100;

    try {
    
      /* prompt user for game setup */
      System.out.println("What kind of output information do you want?" 
        + "\n0 = none\n1 = chips on board\n2 = utilities and statistics");
      outputFormat = stdin.nextInt(); 
      while (outputFormat < 0 || outputFormat > 2) {
         System.out.println("Not a valid option");
         outputFormat = stdin.nextInt();
      }

      /* prompt user for game setup */
      System.out.println("Let's play Konane!");
      System.out.print("Enter board size: ");
      boardSize = stdin.nextInt();

      /* initialize game */
      game = new Board(boardSize);
      
      /*EXTRA DEBUGGING: HELP UNDO METHOD
      //System.out.println("enter how many turns to play before undo");
      turnsBeforeUndoAll = stdin.nextInt();*/

      for (int i=0; i<2; i++) {
        System.out.print("Enter player type for ");
        if (i == 0)
          System.out.print("Black");
        else
          System.out.print("White");

        System.out.print(" (1=human, 2=minmax, 3=alphabeta, 4=random): ");
        int choice = stdin.nextInt();
        switch (choice) {
          case 1:
            players[i] = new HumanPlayer();
            break;
          case 2:
            System.out.print("\nPlease enter the maximum search depth: "); 
            d = stdin.nextInt();//getSearchDepth();
            players[i] = new MinimaxPlayer(d, outputFormat);
            break;
          case 3:
            System.out.print("\nPlease enter the maximum search depth: "); 
            d = stdin.nextInt();//getSearchDepth();
            players[i] = new AlphaBetaPlayer(d, outputFormat);
            break;
          case 4: 
            players[i] = new RandomPlayer();
            break;
          default:
            System.out.println("bad agent type given, please try again...");
            i--;
        }
      }

      //System.out.println("\n===================");

      /* take turns until gameover */
      while ( game.gameWon() == Chip.NONE ) {  
        // if (game.getTurn() == turnsBeforeUndoAll) {
//             System.out.println(" === Popping has begun ===");
//             System.out.println("current state of the board before popping: ");
//             System.out.println(game);
//             System.out.println(" === === ");
//             for (int u = turnsBeforeUndoAll; u > 0; u--) {
//                 game.undoMove(true);
//                 System.out.println(game);
//             }
//             System.exit(0);
//         }
        if (outputFormat > 0) { //if output more than just win/loss
            System.out.print("Turn " + game.getTurn() + ", ");
            if (game.getTurn()%2 == 0) {
              System.out.println("black to play:");
            } else {
              System.out.println("white to play:");
            }
    
            System.out.println(game);
        }
        
        //reset moves per turn
        //players[game.getTurn()%players.length].resetNodesExpandedPerTurn();
        Move m = players[game.getTurn()%players.length].getMove(game);
        //get moves per turn
        //nodesExpandedThisTurn = players[game.getTurn()%players.length].getNodesExpandedThisTurn();
        if (outputFormat > 0) {
            System.out.println("Move to be made: " + m.toString());
        }
        // game.pushMove(m); //DELETE THIS LATER DELETE DELETE DELETE FOR DEBUGGING ONLY
        game.executeMove(m);
      }
      if (outputFormat > 0) {   
        System.out.println("Game over!  Final board state:\n" + game);
      }

      if (game.gameWon() == Chip.BLACK) {
        System.out.println("\n ** Game won by Black after " + game.getTurn() + " turns\n");
      } else {
        System.out.println("\n ** Game won by White after " + game.getTurn() + " turns\n");
      }
      
      if (outputFormat == 2) {
          System.out.println("\n*** ==================== Statistics ==================== *** \n");
          
          System.out.println("-------------------- Turn Times and Total Nodes Expanded --------------------");
          System.out.println("\n ** Black expanded " + players[0].getNumNodesExpanded() + " nodes and times for each turn in ms were: \n");
          //System.out.println(players[0].getTimePerMove());
          printArrayListWithPrependedMessage("Turn", players[0].getTimePerMove());
          System.out.println("\n ** White expanded " + players[1].getNumNodesExpanded() + " nodes and times in ms were: \n");
          //System.out.println(players[1].getTimePerMove());
          printArrayListWithPrependedMessage("Turn", players[1].getTimePerMove());
          System.out.println("-------------------- Number of Nodes Expanded Per Turn --------------------");
          System.out.println("\n ** Number nodes expanded per turn Black: \n" /*+ players[0].getNodesExpandedPerTurn()*/);
          printArrayListWithPrependedMessage("Turn", players[0].getNodesExpandedPerTurn());
          System.out.println("\n ** Number nodes expanded per turn White: \n" /*+ players[1].getNodesExpandedPerTurn()*/);
          printArrayListWithPrependedMessage("Turn", players[1].getNodesExpandedPerTurn());

      }


    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Caught an exception: \n\t" + e.toString());
    }

  }
  private static void printArrayListWithPrependedMessage(String message, ArrayList<? extends Object> arr) {
    if (arr != null) {
        for (int i = 0 ; i < arr.size(); i++) {
            System.out.println(message + " " + i + ": " + arr.get(i));
            
        }
    }
  }
  // private static int getSearchDepth() throws IOException {
//     int d = -1;
//     Scanner stdin = new Scanner(System.in);
//     Integer s;
//     System.out.print("\nPlease enter the maximum search depth: ");
//     d = stdin.nextInt();
//     return d;
//   }

}
