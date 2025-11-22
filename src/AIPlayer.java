/**
 * AIPlayer is a player that doesn't take user input and only plays legal words.
 *
 * @author Taylor Brumwell
 * @version 11/20/2025
 */

import java.lang.reflect.Array;
import java.util.*;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

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

     private Move tryLine(Board board, int lineIndex, boolean horizontal) {
         //might not want the following functions in board, but rather in one of the Game files.
         String[] pattern = board.extractPattern(lineIndex, horizontal);
         List<Integer> anchors = board.findAnchors(pattern);
         Move bestMove = null;

         for (int anchor: anchors) {
             Move move = tryBuildWords(board, pattern, anchor, lineIndex, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }
         return bestMove;
     }

     private Move tryBuildWords(Board board, String[] pattern, int anchor, int lineIndex, boolean horizontal) {
         Move bestMove = null;
         for (int i = 0; i < GameModel.acceptedWords.size(); i++) {
             String word = GameModel.acceptedWords.getWord(i);
             ArrayList<PlacedTile> placedTiles = fitWordToPattern(word, pattern, lineIndex, horizontal);
             if (placedTiles ==  null || placedTiles.isEmpty()) {
                 continue;
             }
             if (!isAnchored(word, anchor)) {
                 continue;
             }
             if (!validateCrossWords(placedTiles, horizontal)) {
                 continue;
             }
             int score = board.simulateScore(placedTiles);
             if (bestMove == null || score > bestMove.score) {
                 bestMove = new Move(placedTiles, score);
             }
         }
         return bestMove;
     }

     //check if connected
     private boolean isAnchored(String word, int anchor) {
        for (int start = 0; start <= Board.SIZE - word.length(); start++) {
            if (anchor >= start && anchor < start + word.length()) {
                return true;
            }
        }
        return false;
     }

     //complete validity check
     private ArrayList<PlacedTile> fitWordToPattern(String word, String[] pattern, int lineIndex, boolean horizontal) {
        for (int start = 0; start <= Board.SIZE - word.length(); start++) {
            boolean conflict = false;
            ArrayList<PlacedTile> tempPlacedTiles = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                int position = start + i;
                String boardChar = pattern[position];
                String wordChar = String.valueOf(word.charAt(i));
                if (!boardChar.equals("_")) {
                    if (!boardChar.equalsIgnoreCase(wordChar)) {
                        conflict = true;
                        break;
                    }
                } else {
                    int row;
                    int col;
                    if (horizontal) {
                        row = lineIndex;
                        col = position; //CHECK, this might be position + 1 depending on board grid
                    } else {
                        row = position; //CHECK, this might be position + 1 depending on board grid
                        col = lineIndex;
                    }
                    tempPlacedTiles.add(new PlacedTile(row, col, new Tile(wordChar)));
                }
            }
            if (!conflict) {
                return tempPlacedTiles;
            }
        }
        return null;
     }

     private boolean validateCrossWords(ArrayList<PlacedTile> placedTiles, boolean horizontal) {

     }
}
