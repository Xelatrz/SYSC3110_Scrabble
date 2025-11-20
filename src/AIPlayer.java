/**
 * AIPlayer is a player that doesn't take user input and only plays legal words.
 *
 * @author Taylor Brumwell
 * @version 11/20/2025
 */

import java.util.*;

public class AIPlayer extends Player { //MAKE SURE the methods that loop NUM PLAYERS counts AIPlayers as Players

     public Move findBestMove(Board board) {
         Move bestMove = null;

         //horizontal
         boolean horizontal = true;
         for (int row = 0; row < board.SIZE; row++) {
             Move move = tryLine(board, row, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }

         //vertical
         horizontal = false;
         for (int col = 0; col < board.SIZE; col++) {
             Move move = tryLine(board, col, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }

         return bestMove;
     }

     private Move tryLine(Board board, int i, boolean horizontal) {
         //might not want the following functions in board, but rather in one of the Game files.
         char[] pattern = board.extractPattern(i, horizontal);
         List<Integer> anchors = board.findAnchors(pattern);
         Move bestMove = null;

         for (int anchor: anchors) {
             Move move = tryBuildWords(board, pattern, anchor, i, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }
         return bestMove;
     }

     private Move tryBuildWords(Board board, char[] pattern, int anchor, int i, boolean horizontal) {
         //BUILD THIS
     }
}
