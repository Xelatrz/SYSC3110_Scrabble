import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Models the game of Scrabble.
 * A few modifications are neccessary to fully encompass the entire game of scrabble, including a
 * method to add/remove players and have player's input their own names, a method to exchange all
 * the tiles in the hand for another set of tiles in the bag, and a proper scoring system which
 * accounts for tile weights.
 *
 * @author Taylor Brumwell
 * @author Cole Galway
 * @version 12/05/2025
 */
public class GameModel implements Serializable {
    /**
     * The list of players who will be participating in the game.
     */
    public ArrayList<Player> players;
    /**
     * The dictionary of accepted words for the game.
     */
    public static Dictionary acceptedWords;

    public TileBag bag;
    public Board board;
    public int test;

    /** An integer containing the index of the current player */
    public int currentPlayerIndex = 0;

    public transient List<GameView> views = new ArrayList<>();
    private transient List<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructs a new Game.
     */
    public GameModel() {
        players = new ArrayList<>();
        acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
        bag = new TileBag();
        board = new Board();
    }

    /**
     * Adds a player into the game, ensuring that the amount of players does not exceed
     * the maximum of five.
     * @param player A Player who will be added into the game.
     */
    public void addPlayer(Player player) {
        //doesn't add existing player and doesn't add too many players
        if (players.size() < 5 && !players.contains(player)) {
            players.add(player);
        }
    }

    /**
     * Sets up a new game with all the neccesary components, and resets the grid, player
     * index and other relevant values to defaults.
     */
    public void setupGame() {
        board = new Board();
        bag  = new TileBag();
        currentPlayerIndex = 0;

        for (Player player : players) {
            player.fillHand(bag);
        }
        placedTiles.clear();
        notifyViews();
    }

    /**
     * Return the current player.
     * @return Player who is currently playing; whose turn it is.
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {return null;}
        return players.get(currentPlayerIndex);
    }

    /**
     * Returns the score for tiles placed on the gameboard.
     * @param placedTiles An ArrayList of tiles placed on the board during the turn
     * @return An integer containing the total score, including premium tile multipliers
     */
    public int scorePlacedTiles(ArrayList<PlacedTile> placedTiles) {
        boolean sameRow = placedTiles.stream().allMatch(pt -> pt.row == placedTiles.getFirst().row);
        boolean sameCol = placedTiles.stream().allMatch(pt-> pt.col == placedTiles.getFirst().col);

        //invalid move (should never be called, but to be caught in case
        if (!sameRow && !sameCol) {
            return 0;
        }

        int totalScore = 0;

        StringBuilder word = new StringBuilder();
        int wordMultiplier = 1;
        int wordScore = 0;

        if (sameRow) {
            int row = placedTiles.getFirst().row;
            int col = placedTiles.getFirst().col;

            int start = col;
            while (start > 0 && board.getTile(row, start - 1) != null ) {
                start -= 1;
            }

            int end = col;
            while (end < board.SIZE - 1 && board.getTile(row, end + 1) != null) {
                end += 1;
            }
            for (int c = start; c <= end; c++) {
                Tile t = board.getTile(row, c);
                if (t == null) {
                    return 0; //shouldn't happen but there for safety
                }
                word.append(t.getLetter());

                int letterScore = t.getScore();
                int finalC = c;

                boolean newTile = placedTiles.stream().anyMatch(pt -> pt.row == row && pt.col == finalC);
                Board.Premium premium = board.getPremium(row, c);

                if (newTile) {
                    if (premium == Board.Premium.DOUBLE_LETTER) {
                        letterScore *= 2;
                    } else if (premium == Board.Premium.TRIPLE_LETTER) {
                        letterScore *= 3;
                    } else if (premium == Board.Premium.DOUBLE_WORD) {
                        wordMultiplier *= 2;
                    } else if (premium == Board.Premium.TRIPLE_WORD) {
                        wordMultiplier *= 3;
                    }
                }
                wordScore += letterScore;
            }
        } else {
            int col = placedTiles.getFirst().col;
            int row =  placedTiles.getFirst().row;

            int start = row;
            while (start > 0 && board.getTile(start - 1, col) != null) {
                start -= 1;
            }
            int end = row;
            while (end < board.SIZE - 1 && board.getTile(end + 1, col) != null) {
                end += 1;
            }

            for (int r = start; r <= end; r++) {
                Tile t = board.getTile(r, col);
                if (t == null) {
                    return 0;
                }
                word.append(t.getLetter());
                int letterScore = t.getScore();
                int finalR = r;

                boolean newTile = placedTiles.stream().anyMatch(pt -> pt.row == finalR && pt.col == col);
                Board.Premium premium = board.getPremium(r, col);

                if (newTile) {
                    if (premium == Board.Premium.DOUBLE_LETTER) {
                        letterScore *= 2;
                    } else if (premium == Board.Premium.TRIPLE_LETTER) {
                        letterScore *= 3;
                    } else if (premium == Board.Premium.DOUBLE_WORD) {
                        wordMultiplier *= 2;
                    } else if (premium == Board.Premium.TRIPLE_WORD) {
                        wordMultiplier *= 3;
                    }
                }
                wordScore += letterScore;
            }
        }
        //should only happen in the case of checking the perpendicular words.
        if (!acceptedWords.checkWord(word.toString())) {
            return 0;
        }
        wordScore *= wordMultiplier;
        totalScore += wordScore;

        //checking the perpendicular word
        for (PlacedTile pt: placedTiles) {
            StringBuilder cross = new StringBuilder();
            int crossWordMultiplier = 1;
            int crossScore = 0;

            if (sameRow) {
                int r =  pt.row;
                int col = pt.col;
                int start = r;
                while (start > 0 && board.getTile(start - 1, pt.col)!= null) {
                    start -= 1;
                }
                int end = r;
                while (end < board.SIZE - 1 && board.getTile(end + 1, pt.col)!= null) {
                    end += 1;
                }

                //cross word is only 1 tile long
                if (end - start >= 1) {
                    for (int row = start; row <= end; row++) {
                        Tile t = board.getTile(row, pt.col);
                        cross.append(t.getLetter());
                        int letterScore = t.getScore();

                        int finalRow = row;
                        boolean newTile = placedTiles.stream().anyMatch(p-> p.row == finalRow && p.col == col);
                        Board.Premium premium = board.getPremium(row, col);

                        if (newTile) {
                            if (premium == Board.Premium.DOUBLE_LETTER) {
                                letterScore *= 2;
                            } else if (premium == Board.Premium.TRIPLE_LETTER) {
                                letterScore *= 3;
                            } else if (premium == Board.Premium.DOUBLE_WORD) {
                                crossWordMultiplier *= 2;
                            } else if (premium == Board.Premium.TRIPLE_WORD) {
                                crossWordMultiplier *= 3;
                            }
                        }
                        crossScore += letterScore;
                    }
                    if (cross.length() > 1) {
                        if (!acceptedWords.checkWord(cross.toString())) {
                            return 0;
                        }
                    }
                    crossScore *= crossWordMultiplier;
                    totalScore += crossScore;
                }
            } else {
                int row = pt.row;
                int start = pt.col;
                while (start > 0 && board.getTile(pt.row, start - 1) != null) {
                    start -= 1;
                }
                int end = pt.col;
                while(end < board.SIZE - 1 && board.getTile(pt.row, end + 1) != null) {
                    end += 1;
                }
                if (end - start >= 1) {
                    for (int col = start; col <= end; col++) {
                        Tile t = board.getTile(pt.row, col);
                        cross.append(t.getLetter());
                        int letterScore = t.getScore();

                        int finalCol = col;
                        boolean newTile = placedTiles.stream().anyMatch(p -> p.row == row && p.col == finalCol);
                        Board.Premium premium = board.getPremium(row, col);

                        if (newTile) {
                            if (premium == Board.Premium.DOUBLE_LETTER) {
                                letterScore *= 2;
                            } else if (premium == Board.Premium.TRIPLE_LETTER) {
                                letterScore *= 3;
                            } else if (premium == Board.Premium.DOUBLE_WORD) {
                                crossWordMultiplier *= 2;
                            } else if (premium == Board.Premium.TRIPLE_WORD) {
                                crossWordMultiplier *= 3;
                            }
                        }
                        crossScore += letterScore;
                    }
                }
                if (cross.length() > 1) {
                    if (!acceptedWords.checkWord(cross.toString())) {
                        return 0;
                    }
                }
                crossScore *= crossWordMultiplier;
                totalScore += crossScore;
            }
        }
        return totalScore;
    }

    //a bit of duplicated code, but I think it's necessary

    /**
     * Returns an integer determining which premium tile is in which location.
     * ONLY USED FOR TESTING PURPOSES!
     * 4 = triple word
     * 3 = double word
     * 2 = triple letter
     * 1 = double letter
     * 0 = normal tile
     * @param row The row of the tile of interest
     * @param col The column of the tile of interest
     * @return An integer value between 0-4 corresponding to the various premium tiles.
     */
    public int getPremiumType(int row, int col) {
        //triple word
        if ((row == 0 || row == 7 || row == 14) && (col == 0 || col == 7 || col == 14)) {
            //center space
            if (row == 7 && col == 7) {
                return 3;
            }
            return 4;
        }
        //double word
        if (row == col || row + col == 14) {
            return 3;
        }
        //triple letter
        int[][] tripleLetter = {{1,5}, {1,9}, {5, 1}, {5,5}, {5,9}, {5, 13}, {9, 1}, {9, 5}, {9,9}, {9, 13}, {13, 5}, {13,9}};
        for (int[] p:  tripleLetter) {
            if (p[0] == row && p[1] == col) {
                return 2;
            }
        }
        //double letter
        int[][] doubleLetter = {{0,3}, {0,11}, {2,6}, {2,8}, {3,0}, {3,7}, {3, 14}, {6,2}, {6,6}, {6,8}, {6, 12}, {7,3}
                ,{7,11}, {8,2}, {8,6}, {8,8}, {8,12}, {11, 0}, {11,7}, {11,14}, {12,6}, {12,8}, {14,3}, {14,11}};
        for (int[] p:doubleLetter) {
            if (p[0] == row && p[1] == col) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Adds the view to the game model to be able to obverse changes.
     * @param v The GameView used for the GUI.
     */
    public void addView(GameView v) {
        views.add(v);
    }

    /**
     * Notifies the view of a change in the model/game state
     */
    void notifyViews() {
        for (GameView view : views) {
            view.update(this);
        }
    }

    /**
     * Saves the current game to a specified filename.
     * @param fileName the name of the file where the game data will be stored
     */
    public void saveGame(String fileName) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            test = 1;
            System.out.println("Game saved successfully!");
        } catch(Exception e){
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Loads a game from a specified filename
     * @param fileName the name of the file containing the game data to be loaded
     * @return the gamemodel which was loaded from the data.
     */
    public static GameModel loadGame(String fileName) {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            GameModel loaded = (GameModel) in.readObject();
            System.out.println("Game loaded successfully!");
            return loaded;
        } catch (Exception e){
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles a player undo/redo while it's still their turn.
     * @param stack1 The stack correlating to the operation being run (undo or redo).
     * @param stack2 The stack correlating to the opposite of the operation being run (undo or redo).
     */
    public void revertAction(ArrayList<PlacedTile> stack1, ArrayList<PlacedTile> stack2) {
        if (stack1.isEmpty()) {
            return;
        }
        PlacedTile tile = stack1.getLast();
        stack2.add(tile);
        if (board.getTile(tile.row, tile.col) == null) {
            board.placeTempTile(tile.row, tile.col, tile.tile);
            getCurrentPlayer().hand.remove(tile.tile);
        } else {
            board.removeTempTile(tile.row, tile.col);
            getCurrentPlayer().hand.add(tile.tile);
        }
        stack1.remove(tile);
    }
}
