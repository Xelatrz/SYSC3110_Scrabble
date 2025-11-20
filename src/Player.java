import java.util.*;

/**
 * Models a player from a game of scrabble, storing the player name, score, and their hand of tiles.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 11/10/2025
 */
public class Player {
    /**
     * The player's name, representing as a string.
     */
    private String name;
    /**
     * Boolean representing whether the player is human or AI
     */
    private boolean human;
    /**
     * The player's score, representing as an integer.
     */
    private int score;
    /**
     * Standard scrabble hand only has a max of  7 tiles.
     */
    private static final int HAND_SIZE = 7;
    /**
     * A List of tiles which is the hand of the player.
     */
    public ArrayList<Tile> hand; //changed to public for testing

    /**
     * Constructs a new Player.
     *
     * @param name The name of the player being constructed
     */
    public Player(String name, boolean human) {
        this.name = name;
        this.human = human;
        score = 0;
        hand = new ArrayList<>();
    }

    /**
     * Returns the name of the player.
     * @return A String with the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's score.
     * @return An integer containing the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's score to a specified value. (CURRENTLY ONLY USED FOR TESTING)
     * @param score An int of the score the player will have.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns if the player's hand is empty.
     * @return True if the hand is empty, false otherwise.
     */
    public boolean emptyHand() {
        return hand.isEmpty();
    }

    /**
     * Display's the hand of the player.
     */
    public void showHand() {
        System.out.print(name + "'s hand: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();
    }

    /**
     * Draws a tile from the bag of tiles and adds it to the player's hand.
     * Returns if the tile was added to the player's hand or not.
     * @param bag A TileBag containing a specific amount of tiles
     * @return True if the tile was added, false if not.
     */
    public boolean drawTile(TileBag bag) {
        //can only have 7 tiles in scrabble
        if (hand.size() < HAND_SIZE) {
            //draw a tile
            Tile tile = bag.drawTile();
            if (tile == null) {
                System.out.println("No tiles left!");
                return false;
            }
            hand.add(tile);
            return true;
        } else {
            System.out.println("Your hand is full");
            return false;
        }
    }

    /**
     * When called this method will fill the player's hand with tiles, until they have a full hand.
     * It will not fill the player's hand if their hand is full, or the bag is empty.
     * @param bag The TileBag which contains the specific tiles and their quantities
     */
    public void fillHand(TileBag bag) {
        while (hand.size() < HAND_SIZE && !bag.isEmpty()) {
            drawTile(bag);
        }
    }

    /**
     * Returns if a word was played on the board or not, allows a player to place tiles on the board
     * and validates if the tiles are correctly placed and if they form a valid word.
     * @param board A Board which is being used for the game
     * @param placedTiles An ArrayList that contains the tiles placed during the turn.
     * @return true if the word was valid and the play was completed, false otherwise.
     */
    public boolean playWord(Board board, ArrayList<PlacedTile> placedTiles) {
        if (placedTiles.isEmpty()) {
            return false;
        }
        int baseRow = placedTiles.getFirst().row;
        int baseCol = placedTiles.getFirst().col;
        boolean sameRow = true;
        boolean sameCol = true;

        boolean success = true;
        StringBuilder word = new StringBuilder();
        boolean firstTurn = board.isEmpty(); //check before placing tiles

        for (PlacedTile placedTile : placedTiles) {
            if (placedTile.row != baseRow) {
                sameRow = false;
            }
            if (placedTile.col != baseCol) {
                sameCol = false;
            }
        }
        if (!sameRow && !sameCol) {
            return false;
        }
        if (sameRow) {
            int startCol = baseCol;
            int endCol = baseCol;

            for (PlacedTile placed : placedTiles) {
                if (placed.col < startCol) {
                    startCol = placed.col;
                }
                if (placed.col > endCol) {
                    endCol = placed.col;
                }
            }
            while (startCol > 0 && board.getTile(baseRow, startCol - 1) != null) {
                startCol--;
            }
            while (endCol < board.SIZE - 1 && board.getTile(baseRow, endCol + 1) != null) {
                endCol++;
            }

            //build the word
            for (int c = startCol; c <= endCol; c++) {
                Tile t = board.getTile(baseRow, c);
                if (t != null) {
                    word.append(t.getLetter());
                }
            }
        } else {
            int startRow = baseRow;
            int endRow = baseRow;
            for (PlacedTile placed : placedTiles) {
                if (placed.row < startRow) {
                    startRow = placed.row;
                }
                if (placed.row > endRow) {
                    endRow = placed.row;
                }
            }
            while (startRow > 0 && board.getTile(startRow - 1, baseCol) != null) {
                startRow--;
            }
            while (endRow < board.SIZE - 1 && board.getTile(endRow + 1, baseCol) != null) {
                endRow++;
            }


            //build the word
            for (int r = startRow; r <= endRow; r++) {
                Tile t = board.getTile(r, baseCol);
                if (t != null) {
                    word.append(t.getLetter());
                }
            }
        }
        //check dictionary
        if (!GameModel.acceptedWords.checkWord(String.valueOf(word).toLowerCase())) {
            success = false;
        }
        /*
        //check connected
        if (!firstTurn){
            if(word.length() <= placedTiles.size() && word.length() > 1) {
                success = false;

            } else if (word.length() == 1 && !isConnected(board, placedTiles)) {
                success = false;
            }
        }

        //first has to touch center space
        if (firstTurn) {
            boolean touchCenter = false;
            for (PlacedTile pt : placedTiles) {
                if (pt.row == Board.CENTER && pt.col == Board.CENTER) {
                    touchCenter = true;
                    break;
                }
            }
            if (!touchCenter) {
                success = false;
            }
        }

         */

        if (success) {
            score += placedTiles.size();
        }
        return success;
    }

    /**
     * Returns the hand of the players
     */
    public ArrayList<Tile> getHand() {
        return hand;
    }

    /**
     * Removes a tile by its index in the player's hand
     * @param index The index of the tile to be removed
     * @return The tile that was removed
     */
    public Tile removeTileByIndex(int index) {
        if (index < 0 || index >= hand.size()) {
            return null;
        }
        return hand.remove(index);
    }

    /**
     * Adds a tile to the player's hand
     * @param t the tile to be added.
     */
    public void addTile(Tile t) {
        if (t!= null) {
            hand.add(t);
        }
    }
}

