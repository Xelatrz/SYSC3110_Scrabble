/**
 * AIPlayer is a player that doesn't take user input and only plays legal words.
 *
 * @author Taylor Brumwell
 * @version 11/24/2025
 */

import java.util.*;

public class AIPlayer extends Player {

    private GameModel model;

    /**
     * AIPlayer constructor.
     * @param name the AIPlayer name.
     * @param model the GameModel accessed by the AIPlayer.
     */
    public AIPlayer(String name, GameModel model) {
        super(name);
        this.model = model;
    }

    /**
     * Finds the ideal play on the AIPlayer's turn.
     * @return the Move corresponding to the ideal play.
     */
     public Move findBestMove() {
         Move bestMove = null;

         //horizontal
         boolean horizontal = true;
         for (int row = 0; row < Board.SIZE; row++) {
             Move move = tryLine(row, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }

         //vertical
         horizontal = false;
         for (int col = 0; col < Board.SIZE; col++) {
             Move move = tryLine(col, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }

         return bestMove;
     }

    /**
     * Checks an individual line for available word plays.
     * @param lineIndex The index of the line being checked.
     * @param horizontal Whether the line is horizontal or not.
     * @return A Move corresponding to the ideal play (if any are available)
     */
     private Move tryLine(int lineIndex, boolean horizontal) {
         String[] pattern = model.board.extractPattern(lineIndex, horizontal);
         List<Integer> anchors = model.board.findAnchors(pattern);
         Move bestMove = null;

         if (anchors.isEmpty()) {
             return null;
         }

         for (int anchor : anchors) {
             Move move = tryBuildWords(pattern, lineIndex, horizontal);
             if (move != null && (bestMove == null || move.score > bestMove.score)) {
                 bestMove = move;
             }
         }
         return bestMove;
     }

    /**
     * Attempt to build valid words on the board.
     * @param pattern The line pattern (including empty tiles).
     * @param lineIndex The index of the line being checked.
     * @param horizontal Whether the line is horizontal or not.
     * @return A Move corresponding to the ideal play (if any are available)
     */
     private Move tryBuildWords(String[] pattern, int lineIndex, boolean horizontal) {
         Move bestMove = null;
         for (int i = 0; i < GameModel.acceptedWords.size(); i++) {
             String word = GameModel.acceptedWords.getWord(i);
             ArrayList<PlacedTile> placedTiles = fitWordToPattern(word, pattern, lineIndex, horizontal);
             if (placedTiles ==  null || placedTiles.isEmpty()) {
                 continue;
             }
             if (!super.touchesExistingTile(model.board, placedTiles)) {
                 continue;
             }
             String primaryWord = buildPrimaryWord(placedTiles, horizontal);
             if (primaryWord.isEmpty()) {
                 continue;
             }
             if (!GameModel.acceptedWords.checkWord(primaryWord.toLowerCase())) {
                 continue;
             }
             if (!validateCrossWords(placedTiles, horizontal)) {
                 continue;
             }

             //make sure at least one tile is placeable
             boolean placedTile = false;
             for (PlacedTile pt: placedTiles) {
                 if (model.board.getPermTile(pt.row, pt.col) == null) {
                     placedTile = true;
                     break;
                 }
             }
             if (!placedTile) {
                 continue;
             }

             int score = model.scorePlacedTiles(placedTiles);
             if (bestMove == null || score > bestMove.score) {
                 bestMove = new Move(placedTiles, score);
             }
         }
         return bestMove;
     }

    /**
     * Attempt to fit a word from the dictionary into the pattern of the line being checked.
     * @param word Valid dictionary word.
     * @param pattern The line pattern (including empty tiles).
     * @param lineIndex The index of the line being checked.
     * @param horizontal Whether the line is horizontal or not.
     * @return An arraylist containing the pattern of the line after fitting the word into the line (if successful).
     */
     private ArrayList<PlacedTile> fitWordToPattern(String word, String[] pattern, int lineIndex, boolean horizontal) {
        int maxStart = Board.SIZE - word.length();
        for (int start = 0; start <= maxStart; start++) {
            boolean conflict = false;
            ArrayList<PlacedTile> tempPlacedTiles = new ArrayList<>();
            ArrayList<Tile> handCopy = new ArrayList<>(this.getHand());

            for (int i = 0; i < word.length(); i++) {
                int position = start + i;
                if (position < 0 || position >= pattern.length) {
                    conflict = true;
                    break;
                }

                String boardChar = pattern[position];
                String wordChar = String.valueOf(word.charAt(i));
                if (!boardChar.equals("_")) {
                    if (!boardChar.equalsIgnoreCase(wordChar)) {
                        conflict = true;
                        break;
                    }
                } else {
                    Tile tile = null;
                    for (Iterator<Tile> it = handCopy.iterator(); it.hasNext(); ) {
                        Tile t = it.next();
                        if (t.getLetter().equalsIgnoreCase(wordChar)) {
                            tile = t;
                            it.remove();
                            break;
                        }
                    }
                    if (tile == null) {
                        conflict = true;
                        break;
                    }
                    int row;
                    int col;
                    if (horizontal) {
                        row = lineIndex;
                        col = position;
                    } else {
                        row = position;
                        col = lineIndex;
                    }
                    tempPlacedTiles.add(new PlacedTile(row, col, tile));
                }
            }
            if (!conflict) {
                return tempPlacedTiles;
            }
        }
        return null;
     }

    /**
     * Attempt to turn the pattern for the primary word being placed into a String.
     * @param placedTiles The line pattern (including empty tiles, and the ideal move).
     * @param horizontal Whether the line is horizontal or not.
     * @return A String containing the word that is attempting to be played.
     */
    private String buildPrimaryWord(ArrayList<PlacedTile> placedTiles, boolean horizontal) {
        if (placedTiles.isEmpty()) {
            return "";
        }

        int row = placedTiles.getFirst().row;
        int col = placedTiles.getFirst().col;
        StringBuilder sb = new StringBuilder();

        if (horizontal) {
            int startCol = col;
            int endCol =  col;
            for (PlacedTile pt: placedTiles) {
                if (pt.col < startCol) {
                    startCol = pt.col;
                }
                if (pt.col > endCol) {
                    endCol = pt.col;
                }
            }
            while (startCol > 0 && model.board.getTile(row, startCol - 1) != null) {
                startCol--;
            }
            while (endCol < Board.SIZE - 1 && model.board.getTile(row, endCol + 1) != null) {
                endCol++;
            }
            for (int c = startCol; c <= endCol; c++) {
                Tile tile = model.board.getPermTile(row, c);
                if (tile != null) {
                    sb.append(tile.getLetter());
                } else {
                    boolean found = false;
                    for (PlacedTile pt: placedTiles) {
                        if (pt.row == row && pt.col == c) {
                            sb.append(pt.tile.getLetter());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return "";
                    }
                }
            }
        } else {
            int startRow = row;
            int endRow =  row;
            for (PlacedTile pt: placedTiles) {
                if (pt.row < startRow) {
                    startRow =  pt.row;
                }
                if (pt.row > endRow) {
                    endRow = pt.row;
                }
            }
            while (startRow > 0 && model.board.getTile(startRow - 1, col) != null) {
                startRow--;
            }
            while (endRow < Board.SIZE - 1 && model.board.getTile(endRow + 1, col) != null) {
                endRow++;
            }
            for (int r = startRow; r <= endRow; r++) {
                Tile tile = model.board.getTile(r, col);
                if (tile != null) {
                    sb.append(tile.getLetter());
                } else {
                    boolean found = false;
                    for (PlacedTile pt: placedTiles) {
                        if (pt.row == r && pt.col == col) {
                            sb.append(pt.tile.getLetter());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return "";
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Check for perpendicular words and make sure they're valid.
     * @param placedTiles The line pattern (including empty tiles, and the ideal move).
     * @param horizontal Whether the line is horizontal or not.
     * @return True if all crosswords (if there are any) are valid, false if there's any invalid crosswords.
     */
    private boolean validateCrossWords(ArrayList<PlacedTile> placedTiles, boolean horizontal) {
        for (PlacedTile pt : placedTiles) {
            StringBuilder sb =  new StringBuilder();
            int row = pt.row;
            int col = pt.col;

            if (horizontal) {
                int startRow = row;
                int endRow =  row;
                while (startRow > 0 && model.board.getTile(startRow - 1, col) != null) {
                    startRow--;
                }
                while (endRow < Board.SIZE - 1 && model.board.getTile(endRow + 1, col) != null) {
                    endRow++;
                }
                for (int r  = startRow; r <= endRow; r++) {
                    Tile tile = model.board.getTile(r, col);
                    if (tile != null) {
                        sb.append(tile.getLetter());
                    }
                }
            } else {
                int startCol = col;
                int endCol =  col;
                while (startCol > 0 && model.board.getTile(row, startCol - 1) != null) {
                    startCol--;
                }
                while (endCol < Board.SIZE - 1 && model.board.getTile(row, endCol + 1) != null) {
                    endCol++;
                }
                for (int c = startCol; c <= endCol; c++) {
                    Tile tile = model.board.getTile(row, c);
                    if (tile != null) {
                        sb.append(tile.getLetter());
                    }
                }
            }
            String crossWord = sb.toString();
            if (crossWord.length() <= 1) {
                continue;
            }
            if (!GameModel.acceptedWords.checkWord(crossWord.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
