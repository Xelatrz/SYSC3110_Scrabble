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
    private GameFrame frame;

    /**
     * Constructs a new Player.
     *
     * @param name The name of the player being constructed
     */
    public Player(String name) {
        this.name = name;
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
        boolean firstTurn = board.isEmpty();

        board.clearTempGrid();

        for (PlacedTile pt : placedTiles) {
            board.placeTempTile(pt.row, pt.col, pt.tile);
        }

        boolean sameRow = placedTiles.stream().allMatch(pt -> pt.row == placedTiles.getFirst().row);
        boolean sameCol = placedTiles.stream().allMatch(pt -> pt.col == placedTiles.getFirst().col);

       if (!sameRow && !sameCol) {
           clearTemp(board, placedTiles);
           return false;
       }

       String mainWord;
       int startR, endR, startC, endC;

       if (sameRow) {
           int row = placedTiles.get(0).row;
           startC = placedTiles.stream().mapToInt(pt -> pt.col).min().getAsInt();
           endC = placedTiles.stream().mapToInt(pt -> pt.col).max().getAsInt();

           while (startC > 0 && board.getTile(row, startC - 1) != null) {
               startC -= 1;
           }
           while (endC < board.SIZE - 1 && board.getTile(row, endC + 1) != null) {
               endC += 1;
           }

           StringBuilder sb = new StringBuilder();
           for (int c = startC; c <= endC; c++) {
               Tile t = board.getTile(row, c);
               if (t == null) {
                   clearTemp(board, placedTiles);
                   return false;
                   }
               sb.append(t.getLetter());
               }
           mainWord = sb.toString();
           startR = endR = row;

       } else {
           int col = placedTiles.get(0).col;

           startR = placedTiles.stream().mapToInt(pt -> pt.row).min().getAsInt();
           endR = placedTiles.stream().mapToInt(pt -> pt.row).max().getAsInt();

           while (startR > 0 && board.getTile(startR - 1, col) != null) {
               startR -= 1;
           }
           while (endR < board.SIZE - 1 && board.getTile(endR + 1, col) != null) {
               endR += 1;
           }

           StringBuilder sb = new StringBuilder();
           for (int r = startR; r <= endR; r++) {
               Tile t = board.getTile(r, col);
               if (t == null) {
                   clearTemp(board, placedTiles);
                   return false;
               }
               sb.append(t.getLetter());
           }
           mainWord = sb.toString();
           startC = endC = col;
       }


        //check dictionary
        if (!GameModel.acceptedWords.checkWord(mainWord.toLowerCase())) {
            clearTemp(board, placedTiles);
            return false;
        }


        //check connected
        if (!firstTurn) {
            boolean touchesExisitng = touchesExistingTile(board,placedTiles);
            if (!touchesExisitng) {
                clearTemp(board, placedTiles);
                return false;
            }
        } else {
            boolean touchesCenter = placedTiles.stream().anyMatch(pt -> pt.row == 7 && pt.col == 7);
            if (!touchesCenter) {
                clearTemp(board, placedTiles);
                return false;
            }
        }


        for (PlacedTile pt: placedTiles) {
            if (sameRow) {
                String cross = buildWordVertical(board, pt.row, pt.col);
                if (cross.length() > 1 && !GameModel.acceptedWords.checkWord(cross.toLowerCase())) {
                    clearTemp(board, placedTiles);
                    return false;
                }
            } else {
                String cross = buildWordHorizontal(board, pt.row, pt.col);
                if (cross.length() > 1 && !GameModel.acceptedWords.checkWord(cross.toLowerCase())) {
                    clearTemp(board, placedTiles);
                    return false;
                }
            }

        }

        board.commitTiles(placedTiles);

        return true;
    }

    private void clearTemp(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile pt: placedTiles) {
            board.placeTempTile(pt.row, pt.col, null);
        }
        board.clearTempGrid();
    }

    private boolean touchesExistingTile(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile pt: placedTiles) {
            int r =  pt.row;
            int c = pt.col;

            if ((r > 0 && board.getPermTile(r-1, c) != null) || (r < board.SIZE - 1 && board.getPermTile(r+1, c) != null) || (c > 0 && board.getPermTile(r, c-1) != null) || (c < board.SIZE - 1 && board.getPermTile(r, c+1) != null)) {
                return true;
            }
        }
        return false;
    }

    private String buildWordVertical(Board board, int row, int col) {
        int r1 = row;
        while (r1 > 0 && board.getTile(r1-1, col) != null) {
            r1 -= 1;
        }
        int r2 = row;
        while (r2 < board.SIZE - 1 && board.getTile(r2+1, col) != null) {
            r2 += 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int r = r1; r <= r2; r++) {
            Tile t = board.getTile(r, col);
            if (t == null) {
                continue;
            }
            sb.append(t.getLetter());
        }
        return sb.toString();
    }

    private String buildWordHorizontal(Board board, int row, int col) {
        int c1 = col;
        while (c1 > 0 && board.getTile(row, c1-1) != null) {
            c1 -= 1;
        }
        int c2 = col;
        while (c2 < board.SIZE - 1 && board.getTile(row, c2+1) != null) {
            c2 += 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int c = c1; c <= c2; c++) {
            Tile t =  board.getTile(row, c);
            if (t == null) {
                continue;
            }
            sb.append(t.getLetter());
        }
        return sb.toString();
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

