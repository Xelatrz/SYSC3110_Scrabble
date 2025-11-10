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
    private static int HAND_SIZE = 7;
    /**
     * A List of tiles which is the hand of the player.
     */
    public ArrayList<Tile> hand; //changed to public for testing
    /**
     * A list of the tiles that have been placed on the board.
     */
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

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
     * Places a tile onto a specific location on the board based on the player's input.
     * @param board The specific game board which the players are using
     */
    /*
    public void placeTile(Board board) {
        Scanner input = new Scanner(System.in);

        //takes the user input for the tile
        System.out.println("Select a tile: ");
        String selectedLetter = input.nextLine().toUpperCase();

        //finds the tile in the hand
        Tile selectedTile = null;
        for (Tile tile : hand) {
            if (tile.getLetter().equals(selectedLetter)) {
                selectedTile = tile;
                break;
            }
        }
        if (selectedTile == null) {
            System.out.println("That tile isn't in " + name + "'s hand!");
            return;
        }

        //place tile on desired board coordinate
        int row;
        while (true) {
            try {
                System.out.println("Select a row for the tile: ");
                row = Integer.parseInt(input.nextLine().toLowerCase());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, row number must be an integer.");
            }
        }
        int col;
        while (true) {
            try {
                System.out.println("Select a column for the tile: ");
                col = Integer.parseInt(input.nextLine().toLowerCase());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, column number must be an integer.");
            }
        }

        if (board.getTile(row, col) != null) {
            System.out.println("That spot is already occupied.");
            return;
        }

        if (row < 0 || col < 0 || row >= board.SIZE || col >= board.SIZE) {
            System.out.print("That spot is out of bounds.");
            return;
        }

        board.placeTile(row, col, selectedTile);
        hand.remove(selectedTile);
        placedTiles.add(new PlacedTile(row, col, selectedTile));

        System.out.println(name + " placed an " + selectedTile.getLetter() + " at " + row + "," + col + "!");
        board.display();
    }
     */

    /**
     * Removes placed tiles from the board.
     * @param board A Board for the game which is being used by the players
     * @param placedTiles A List of tiles that have been placed on the board
     */
    /*
    private void removePlacedTiles(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            board.removeTile(placedTile.row, placedTile.col, placedTile.tile);
        }
    }
     */
    /**
     * Gives back the tiles that were placed into the player's hand.
     * @param placedTiles A List of the tiles that have been placed on the board.
     */
    /*
    private void returnPlacedTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            hand.add(placedTile.tile);
        }
    }

     */

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
     * Skips a player's turn upon their request
     * Returns if the turn was skipped.
     * @param bag the bag that unwanted tiles will be returned to, and where new tiles will be drawn from
     * @return true if the turn was skipped, false otherwise.
     */
    /*
    public boolean passTurn(TileBag bag) {
        //skip the player's turn
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(name + ", are you sure you'd like to skip your turn? (Y/N)");
            String choice = input.nextLine().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                while (!emptyHand()) {
                    bag.addTiles(hand.getFirst().getLetter(), 1);
                    hand.remove(hand.getFirst());
                }
                fillHand(bag);
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
            System.out.println("Invalid input, please answer yes or no. (Y/N)");
        }
    }
    */
    /**
     * Allows player's to vote to end the game.
     * Returns if the game was voted to be ended.
     * @return true if the game is voted to be ended by unanimous vote, false otherwise.
     */
    /*
    public boolean voteGameOver() {
        Scanner input = new Scanner(System.in);
        System.out.println(name + ", do you want to vote to end game (Y/N)");
        String choice = input.nextLine().toLowerCase();
        while(true) {
            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
            System.out.println("Invalid input, please answer yes or no. (Y/N)");
        }
    }

     */

    /**
     * Checks to see if the placed word is connected to another word.
     * Returns if a word is connected to another word on the board.
     * @param board A Board which is being used for the game.
     * @return true if the word is connected, false otherwise.
     */
    private boolean isConnected(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            int row =  placedTile.row;
            int col =  placedTile.col;

            //check all 4 adjacent spots
            if ((row > 0 && board.grid[row - 1][col] != null) || (row < board.SIZE - 1 && board.grid[row + 1][col] != null)
                    || (col > 0 && board.grid[row][col - 1] != null) ||  (col < board.SIZE - 1 && board.grid[row][col + 1] != null)) {
                return true;
            }
        }
        return false;
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

